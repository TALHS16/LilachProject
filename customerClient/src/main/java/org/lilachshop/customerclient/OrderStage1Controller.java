/**
 * Sample Skeleton for 'OrderStage1.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.lilachshop.entities.Order;

public class OrderStage1Controller {

    Order myOrder;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    public void setCardField(TextArea cardField) {
        this.cardField = cardField;
    }

    @FXML // fx:id="cardField"
    private TextArea cardField; // Value injected by FXMLLoader

    @FXML // fx:id="name"
    private Label name; // Value injected by FXMLLoader

    @FXML // fx:id="next"
    private Button next; // Value injected by FXMLLoader

    @FXML
    void gotoSignUpOrPersonalArea(MouseEvent event) {

    }

    @FXML
    void returnToCatalog(MouseEvent event) {
        CustomerApp.getCustomerCatalog();
    }

    @FXML
    void gotoNext(ActionEvent event)
    {
        myOrder.setGreetingCard(cardField.getText());
        Stage stage = CustomerApp.getStage();
        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader(CartController.class.getResource("OrderStage2.fxml"));
            Parent root = fxmlLoader1.load();
            OrderStage2Controller orderStage2Controller = fxmlLoader1.getController();
            orderStage2Controller.showInfo(myOrder);
            stage.setScene(new Scene(root));
            stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, CustomerApp::onCloseWindowEvent);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        name.setText("שלום, " + CustomerApp.getMyCustomer().getName());


    }

    public void showInfo(Order order) {
        myOrder = order;
    }
}
