package org.lilachshop.customerclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.lilachshop.events.Signup1Event;
import org.greenrobot.eventbus.Subscribe;
import org.lilachshop.panels.CustomerAnonymousPanel;
import org.lilachshop.panels.OperationsPanelFactory;
import org.lilachshop.panels.Panel;
import org.lilachshop.panels.PanelEnum;

public class SignUpStageUserPasswordController implements Initializable {

    static private Panel panel = null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnNext;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TextField userNameTF;

    Alert alert = new Alert(Alert.AlertType.ERROR);


    @FXML
    void onClickBtnNext(ActionEvent event) {
        System.out.println("Next btn clicked on");
        //Validate Password
        if (passwordTF.getText().length() < 5) {
            alert.setContentText("סיסמא לא תקינה - סיסמא צריכה להכיל לפחות 5 ספרות");
            alert.show();
            passwordTF.clear();
        } else {
            //Validate UserName
            if (userNameTF.getText().isBlank()) {
                alert.setContentText("שדה שם משתמש ריק, אנא מלא שנית");
                alert.show();
            } else {
                CustomerAnonymousPanel customerAnonymousPanel = (CustomerAnonymousPanel) panel;
                if (customerAnonymousPanel == null) {
                    throw new RuntimeException("Bad panel output in Signup Stage 3 controller");
                }
                System.out.println("Panel is set to request if userName exists in the system");
                customerAnonymousPanel.checkIfUserNameTaken(userNameTF.getText());
            }
        }
    }

    @FXML
    void onClickBtnBack(ActionEvent event) {
        //TODO:Pop-up- Are you sure you want to leave SignUp?
        Stage stage = CustomerApp.getStage();
        FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("SignUpLogin.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Subscribe
    public void onCheckIfUserNameTaken(Boolean taken) {
        if (!taken) {
            moveToStage2Signup();
        } else {
            Platform.runLater(() -> {
                alert.setContentText("שם המשתמש שבחרת תפוס, אנא בחר שם משתמש חדש");
                alert.show();
                userNameTF.clear();
                userNameTF.setPromptText("שם משתמש שבחרת תפוס");
            });
        }
    }

    public void moveToStage2Signup() {
        Signup1Event event1 = new Signup1Event(userNameTF.getText(), passwordTF.getText());
        EventBus.getDefault().post(event1);

        Platform.runLater(() -> {
            Stage stage = CustomerApp.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("SignUp2.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                System.out.println("Something went wrong on move from stage1 to stage2");
                e.printStackTrace();
            }
            stage.setScene(new Scene(root));
            stage.show();
        });

    }

    @FXML
    void initialize() {
        assert btnNext != null : "fx:id=\"btnNext\" was not injected: check your FXML file 'SignUp1.fxml'.";
        assert passwordTF != null : "fx:id=\"passwordTF\" was not injected: check your FXML file 'SignUp1.fxml'.";
        assert userNameTF != null : "fx:id=\"userNameTF\" was not injected: check your FXML file 'SignUp1.fxml'.";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (panel != null) {
            panel.closeConnection();
            panel = null;
        }
        panel = OperationsPanelFactory.createPanel(PanelEnum.CUSTOMER_ANONYMOUS, CustomerApp.getSocket(), this);
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        alert.setHeaderText("שגיאה במילוי פרטי הרשמה");
    }
}
