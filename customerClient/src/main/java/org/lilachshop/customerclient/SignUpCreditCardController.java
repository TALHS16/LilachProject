/**
 * Sample Skeleton for 'SignUp4.fxml' Controller Class
 */
package org.lilachshop.customerclient;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.greenrobot.eventbus.EventBus;
import org.lilachshop.entities.CreditCard;
import org.lilachshop.events.CreditCardEvent;

public class SignUpCreditCardController implements Initializable {

    @FXML // fx:id="cardOwnerIDLabel"
    private Text cardOwnerIDLabel; // Value injected by FXMLLoader

    @FXML // fx:id="cardOwnerIDTF"
    private MaskedTextField cardOwnerIDTF; // Value injected by FXMLLoader

    @FXML // fx:id="cardOwnerNameLabel"
    private Text cardOwnerNameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="cardOwnerNameTF"
    private TextField cardOwnerNameTF; // Value injected by FXMLLoader

    @FXML // fx:id="creditCardNumberLabel"
    private Text creditCardNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="creditCardNumberTF"
    private MaskedTextField creditCardNumberTF; // Value injected by FXMLLoader

    @FXML // fx:id="endSignUpBtn"
    private Button endSignUpBtn; // Value injected by FXMLLoader

    @FXML // fx:id="btnBack"
    private Button btnBack; // Value injected by FXMLLoader

    @FXML // fx:id="expDateLabel"
    private Text expDateLabel; // Value injected by FXMLLoader

    @FXML // fx:id="expDateTF"
    private MaskedTextField expDateTF; // Value injected by FXMLLoader


    @FXML
    private Text cvcLabel;


    @FXML
    private MaskedTextField cvcTF;


    @FXML
    void onClickNextBtn(ActionEvent event) {  //moves next to Account choice stage

        CreditCard card = validateCreditCard();
        if(card!=null) {
            CreditCardEvent finalEvent = new CreditCardEvent(card);
            EventBus.getDefault().post(finalEvent);

            Stage stage = CustomerApp.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("SignUp3.fxml"));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(new Scene(root));
            stage.show();

        }
    }

    @FXML
    void onClickBtnBack(ActionEvent event) {        //moves back to Info stage
        Stage stage = CustomerApp.getStage();
        FXMLLoader fxmlLoader = new FXMLLoader(CatalogController.class.getResource("Signup2.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        initalizeTextFieldAndLabelConnection(creditCardNumberTF, creditCardNumberLabel);
        initalizeTextFieldAndLabelConnection(cardOwnerIDTF, cardOwnerIDLabel);
        initalizeTextFieldAndLabelConnection(expDateTF, expDateLabel);
        initalizeTextFieldAndLabelConnection(cvcTF,cvcLabel);


        //Handle NameTF
        cardOwnerNameTF.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String curr_val = cardOwnerNameTF.getText();
                cardOwnerNameLabel.setText(curr_val);
            }
        });
    }

    public void initalizeTextFieldAndLabelConnection(MaskedTextField textField, Text label) {
        textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String curr_val = textField.getMasked_inputed();
                label.setText(curr_val);
            }
        });
    }

    private CreditCard validateCreditCard() {
        CreditCard card = new CreditCard();

        try {
            card.setNumber(creditCardNumberTF.getPlainText());
            card.setOwnerName(cardOwnerNameTF.getText());
            card.setExpDate(expDateTF.getPlainText());
            card.setThreeDigits(cvcTF.getPlainText());
            card.setCardOwnerId(cardOwnerIDTF.getPlainText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alert.setContentText(e.getMessage());
            alert.show();
            return null;
        }
        return card;
    }

}