package org.lilachshop.employeeclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.entities.Employee;
import org.lilachshop.panels.OperationsPanelFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.EmployeeAnonymousPanel;
import org.lilachshop.panels.PanelEnum;


public class EmployeeLoginController implements Initializable {

    static private Panel panel;
    FXMLLoader fxmlLoader;
    Parent root1;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button logInBtn;

    @FXML
    private Text errorLogin;

    @FXML
    private Text errorLogin2;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TextField userNameTF;

    @Subscribe
    public void handleMessageReceivedFromClient(String msg) {
        System.out.println("Got message!");
    }

    @FXML
    void initialize() {
        assert logInBtn != null : "fx:id=\"logInBtn\" was not injected: check your FXML file 'EmployeeLogin.fxml'.";
        assert passwordTF != null : "fx:id=\"passwordTF\" was not injected: check your FXML file 'EmployeeLogin.fxml'.";
        assert userNameTF != null : "fx:id=\"userNameTF\" was not injected: check your FXML file 'EmployeeLogin.fxml'.";

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel = OperationsPanelFactory.createPanel(PanelEnum.EMPLOYEE_ANONYMOUS, EmployeeApp.getSocket(), this);
        EventHandler<KeyEvent> enterKeyHandler = event -> {
            if (event.getCode() == KeyCode.ENTER) {
                tryLogEmployee(null);
            }
        };
        userNameTF.setOnKeyPressed(enterKeyHandler);
        passwordTF.setOnKeyPressed(enterKeyHandler);
    }


    @FXML
    void userNameClick(MouseEvent event) {
        errorLogin.setVisible(false);
        errorLogin2.setVisible(false);
    }


    @FXML
    void tryLogEmployee(ActionEvent event) {
        String userName = userNameTF.getText();
        String password = passwordTF.getText();
        ((EmployeeAnonymousPanel) panel).sendLoginRequest(userName, password);
    }

    @Subscribe
    public void handleMessageReceived(Object msg) {
        Platform.runLater(() -> {
            if (msg.getClass().equals(String.class)) {
                if (msg.equals("Employee is connected already")) {
                    errorLogin2.setVisible(true);
                } else {
                    errorLogin.setVisible(true);
                }
            } else {
                assert msg.getClass().equals(Employee.class) : "Employee should be gotten here.";
                DashBoardController controller = fxmlLoader.getController();
                controller.employee = ((Employee) msg);
                controller.setData((Employee) msg);
                Stage stage = EmployeeApp.getStage();
                stage.setScene(new Scene(root1));
                stage.show();
            }

        });
    }

    public static Panel getPanel() {
        return panel;
    }


}
