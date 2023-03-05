/**
 * Sample Skeleton for 'OrderStage4.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.lilachshop.entities.CreditCard;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.myOrderItem;
import org.lilachshop.panels.StoreCustomerPanel;

public class OrderStage4Controller {
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private Label cardNumLabel;

    @FXML
    private TextField creditNumTF;

    @FXML
    private TextField cvcTF;

    @FXML
    private TextField expDateTF;

    @FXML
    private Label idCardLabel;

    @FXML
    private TextField idCardTF;

    @FXML
    private Label name;

    @FXML
    private Button next;

    @FXML
    private Label ownerNameLabel;

    @FXML
    private TextField ownerNameTF;

    @FXML
    private Button prev;

    @FXML
    private Label expDateLabel;


    @FXML
    private Label cvcLabel;

    Order myOrder;

    @FXML
    void endOrder(ActionEvent event) {
        CreditCard card = validateCreditCard();
        if (card != null) {
            myOrder.setCreditCard(card);
            myOrder.setStore(CustomerApp.getMyStore());
            ((StoreCustomerPanel)CustomerApp.getPanel()).sendNewOrderCreationToServer(myOrder);
            Alert a = new Alert(Alert.AlertType.NONE);
            a.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("אישור הזמנה");
            a.setTitle("אישור הזמנה");
            a.setContentText("ההזמנתך אושרה בהצלחה");
            a.initModality(Modality.APPLICATION_MODAL);
            Optional<ButtonType> result = a.showAndWait();
            if (result.get() == ButtonType.OK) {
                CustomerApp.setMyFlowers(new LinkedList<myOrderItem>());
                CustomerApp.getCustomerCatalog();
            }
        }

    }

    @FXML
    void gotoPrev(ActionEvent event) {
        Stage stage = CustomerApp.getStage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(CartController.class.getResource("OrderStage3.fxml"));
            Parent root = fxmlLoader.load();
            OrderStage3Controller orderStage3Controller = fxmlLoader.getController();
            orderStage3Controller.showInfo(myOrder);
            stage.setScene(new Scene(root));
            stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void returnToCatalog(MouseEvent event) {
        CustomerApp.getCustomerCatalog();

    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cardNumLabel != null : "fx:id=\"cardNumLabel\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert creditNumTF != null : "fx:id=\"creditNumTF\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert cvcTF != null : "fx:id=\"cvcTF\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert expDateTF != null : "fx:id=\"expDateTF\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert idCardLabel != null : "fx:id=\"idCardLabel\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert idCardTF != null : "fx:id=\"idCardTF\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert name != null : "fx:id=\"name\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert next != null : "fx:id=\"next\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert ownerNameLabel != null : "fx:id=\"ownerNameLabel\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert ownerNameTF != null : "fx:id=\"ownerNameTF\" was not injected: check your FXML file 'OrderStage4.fxml'.";
        assert prev != null : "fx:id=\"prev\" was not injected: check your FXML file 'OrderStage4.fxml'.";

        //set Actions to creditCard Display
        creditNumTF.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String curr_val = creditNumTF.getText();
                cardNumLabel.setText(curr_val);
            }
        });
        expDateTF.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String curr_val = expDateTF.getText();
                expDateLabel.setText(curr_val);
            }
        });

        ownerNameTF.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String curr_val = ownerNameTF.getText();
                ownerNameLabel.setText(curr_val);
            }
        });

        idCardTF.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String curr_val = idCardTF.getText();
                idCardLabel.setText(curr_val);
            }
        });

        cvcTF.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String curr_val = cvcTF.getText();
                cvcLabel.setText(curr_val);
            }
        });

        //set TextFields to customer data
        name.setText("שלום, " + CustomerApp.getMyCustomer().getName());

        creditNumTF.setText(CustomerApp.getMyCustomer().getCard().getNumber().toString());
        cardNumLabel.setText(CustomerApp.getMyCustomer().getCard().getNumber().toString());

        expDateTF.setText(CustomerApp.getMyCustomer().getCard().getExpDateStringFormat());
        expDateLabel.setText(CustomerApp.getMyCustomer().getCard().getExpDateStringFormat());

        ownerNameTF.setText(CustomerApp.getMyCustomer().getCard().getOwnerName());
        ownerNameLabel.setText(CustomerApp.getMyCustomer().getCard().getOwnerName());

        idCardTF.setText(CustomerApp.getMyCustomer().getCardOwnerId());
        idCardLabel.setText(CustomerApp.getMyCustomer().getCardOwnerId());

        cvcTF.setText(CustomerApp.getMyCustomer().getCard().getThreeDigits());
        cvcLabel.setText(CustomerApp.getMyCustomer().getCard().getThreeDigits());
    }

    private CreditCard validateCreditCard() {;
        CreditCard card = new CreditCard();
        try {
            card.setNumber(creditNumTF.getText());
            card.setOwnerName(ownerNameTF.getText());
            card.setExpDate(expDateTF.getText());
            card.setThreeDigits(cvcTF.getText());
            card.setCardOwnerId(idCardTF.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
            return null;
        }
        return card;
    }

    public void showInfo(Order order) {
        myOrder = order;
    }
}
