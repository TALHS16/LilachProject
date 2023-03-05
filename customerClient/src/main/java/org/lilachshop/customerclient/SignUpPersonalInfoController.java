/**
 * Sample Skeleton for 'SignUp2.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.events.Signup2Event;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class SignUpPersonalInfoController implements Initializable {

    @FXML // fx:id="addressTF"
    private TextField addressTF; // Value injected by FXMLLoader

    @FXML // fx:id="cityTF"
    private TextField cityTF; // Value injected by FXMLLoader

    @FXML // fx:id="firstNameTF"
    private TextField firstNameTF; // Value injected by FXMLLoader

    @FXML // fx:id="lastNameTF"
    private TextField lastNameTF; // Value injected by FXMLLoader

    @FXML // fx:id="nextBtn"
    private Button nextBtn; // Value injected by FXMLLoader

    @FXML // fx:id="phoneTF"
    private MaskedTextField phoneTF; // Value injected by FXMLLoader

    @FXML // fx:id="emailTF"
    private TextField emailTF; // Value injected by FXMLLoader

    Alert alert;

    String phone;

    @FXML
    void onClickNextBtn(ActionEvent event) { //move next to creditCard Stage
        if (ValidateInput()) {
            Signup2Event event2 = new Signup2Event(firstNameTF.getText(),
                    lastNameTF.getText(), phone, cityTF.getText(),
                    addressTF.getText(), emailTF.getText());
            EventBus.getDefault().post(event2);
            Stage stage = CustomerApp.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("SignUp4.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    void onClickBtnBack(ActionEvent event) {
        Stage stage = CustomerApp.getStage();
        FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("Signup1.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean ValidateInput() {
        phone = phoneTF.getMasked_inputed().replace("_", "").replace("-", "");
        if (!FieldInHebrewOrDisplayError(firstNameTF, "שם פרטי אינו תקין - אנא מלא שוב בעברית")) {
            return false;
        }
        if (!FieldInHebrewOrDisplayError(lastNameTF, "שם משפחה אינו תקין - אנא מלא שוב בעברית")) {
            return false;
        }
        if (!checkValidPhoneNumber(phone)) {
            alert.setContentText("טלפון אינו תקין - אנא מלא שוב");
            alert.show();
            phoneTF.clear();
            return false;
        }
        if (!FieldInHebrewOrDisplayError(cityTF, "שם עיר אינו תקין - אנא מלא שוב בעברית")) {
            return false;
        }
        if (!Utilities.containHebrewOrNumber(addressTF.getText())) {
            alert.setContentText("כתובת אינה תקינה - אנא מלא שוב בעברית");
            alert.show();
            addressTF.clear();
            return false;
        }
        if (!checkValidEmail(emailTF.getText())) {
            alert.setContentText("כתובת דואר אלקטרוני אינה תקינה.");
            alert.setContentText("אנא הכנס כתובת דואר אלקטרוני תקינה.");
            alert.show();
            emailTF.clear();
            return false;
        }
        return true;
    }


    boolean FieldInHebrewOrDisplayError(TextField F, String msg) {
        if (!Utilities.containHebrew(F.getText()) || (F.getText().isBlank())) {
            alert.setContentText(msg);
            alert.show();
            F.clear();
            return false;
        }
        return true;
    }

    private boolean checkValidEmail(String str) {
        {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

            Pattern pat = Pattern.compile(emailRegex);
            if (str == null)
                return false;
            return pat.matcher(str).matches();
        }
    }

    private boolean checkValidPhoneNumber(String phoneNumber) {
        System.out.println(phoneNumber);
        if (phoneNumber.isBlank())
            return false;
        Pattern pattern = Pattern.compile("^[0-9]\\d*$");
        int length = phoneNumber.length();
        return pattern.matcher(phoneNumber).matches() &&
                ((length == 10 && phoneNumber.startsWith("05")) || (length == 9 && phoneNumber.startsWith("0")));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
    }
}
