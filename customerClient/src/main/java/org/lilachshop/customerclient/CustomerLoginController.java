package org.lilachshop.customerclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.Customer;
import org.lilachshop.entities.myOrderItem;
import org.lilachshop.panels.CustomerAnonymousPanel;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class CustomerLoginController implements Initializable {
    private static Panel panel;

    @FXML
    private Button CreateAccountBtn;

    @FXML
    private Button LoginBtn;

    @FXML
    private PasswordField Password;

    @FXML
    private TextField UserName;

    @FXML
    void onCreateAccountBtnClick(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panel = OperationsPanelFactory.createPanel(PanelEnum.CUSTOMER_ANONYMOUS, CustomerApp.getSocket(), this); // this should be the default panel according to customer/employee
        if (panel == null) {
            throw new RuntimeException("Panel creation failed!");
        }
    }

    @FXML
    void onLoginClick(ActionEvent event) {
        String userName = UserName.getText();
        String userpassword = Password.getText();
        ((CustomerAnonymousPanel) panel).sendCustomerLoginRequest(userName, userpassword);
//        ((CustomerAnonymousPanel) panel).sendCatalogRequestToServer();
    }

    @Subscribe
    public void handleMessageReceivedFromClient(Object msg) {
        System.out.println("message about login was received from server");

        Platform.runLater(() -> {
            if (msg.getClass().equals(String.class)) {
                if (String.valueOf(msg).equals("client not exist")) {
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    a.setHeaderText("שם המשתמש או הסיסמה אינם נכונים");
                    a.setTitle("התחברות");
                    a.setContentText("");
                    a.show();
                    UserName.setText("");
                    Password.setText("");
                }
                else if(String.valueOf(msg).equals("client account disabled")){
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setHeaderText("החשבון חסום. לא ניתן לבצע התחברות");
                    a.setTitle("התחברות");
                    a.setContentText("");
                    a.show();
                    UserName.setText("");
                    Password.setText("");
                }
            }else {
                CustomerApp.setMyFlowers(new LinkedList<myOrderItem>());
                CustomerApp.setMyCustomer((Customer) msg);
                CustomerApp.getCustomerCatalog();
            }
        });
    }
}
