/**
 * Sample Skeleton for 'OrderTablePopUp.fxml' Controller Class
 */

package org.lilachshop.employeeclient;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.lilachshop.commonUtils.MailSender;
import org.lilachshop.commonUtils.Utilities;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.OrderStatus;
import org.lilachshop.entities.myOrderItem;
import org.lilachshop.panels.SignedInEmployeePanel;

public class OrderTablePopUpController {
    @FXML
    private Button update;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="choiceStatus"
    public ChoiceBox<OrderStatus> choiceStatus; // Value injected by FXMLLoader

    @FXML // fx:id="customerName1"
    private Text customerName1; // Value injected by FXMLLoader

    @FXML // fx:id="layout"
    private VBox layout; // Value injected by FXMLLoader

    @FXML // fx:id="orderId"
    private Text orderId; // Value injected by FXMLLoader

    @FXML // fx:id="orderTotalPrice"
    private Text orderTotalPrice; // Value injected by FXMLLoader
    private Order order;


    @FXML
    void onUpdate(ActionEvent event) {
        if (!choiceStatus.getSelectionModel().getSelectedItem().equals(order.getOrderStatus())) {
            order.setOrderStatus(choiceStatus.getSelectionModel().getSelectedItem());
            ((SignedInEmployeePanel) OrderTableController.panel).sendUpdatedOrder(order);
            Thread thread = new Thread(() -> {
                if (choiceStatus.getSelectionModel().getSelectedItem().equals(OrderStatus.DELIVERED)) {
                    MailSender.sendMailToCustomer(order);
                }
            });
            thread.start();
            ((Stage)update.getScene().getWindow()).close();

        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("עדכון הזמנה");
            alert.setContentText("סטטוס ההזמנה נשאר זהה");
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            ButtonType button = new ButtonType("אישור");
            alert.getButtonTypes().setAll(button);
            alert.show();
        }

    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert choiceStatus != null : "fx:id=\"choiceStatus\" was not injected: check your FXML file 'OrderTablePopUp.fxml'.";
        assert customerName1 != null : "fx:id=\"customerName1\" was not injected: check your FXML file 'OrderTablePopUp.fxml'.";
        assert layout != null : "fx:id=\"layout\" was not injected: check your FXML file 'OrderTablePopUp.fxml'.";
        assert orderId != null : "fx:id=\"orderId\" was not injected: check your FXML file 'OrderTablePopUp.fxml'.";
        assert orderTotalPrice != null : "fx:id=\"orderTotalPrice\" was not injected: check your FXML file 'OrderTablePopUp.fxml'.";

    }

    public void setData(OrderTableEntry order) {
        this.order = order.getOrder();
        choiceStatus.getSelectionModel().select(order.getOrder().getOrderStatus());
        for (myOrderItem itemsType : order.getOrder().getItems()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("item_order.fxml"));
            try {
                AnchorPane anchorPane = fxmlLoader.load();
                ItemOrderController itemOrderController = fxmlLoader.getController();
                //set the photo,name,price and amount from this flower
                itemOrderController.setData(itemsType);
                layout.getChildren().add(anchorPane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customerName1.setText(order.getOwnerName());
        orderId.setText(Long.toString(order.getId()));
        orderTotalPrice.setText(Double.toString(order.getTotalPrice()));
        List<OrderStatus> orderStatuses = new LinkedList<>();
        orderStatuses.add(OrderStatus.CANCELED);
        orderStatuses.add(OrderStatus.DELIVERED);
        orderStatuses.add(OrderStatus.PENDING);
        choiceStatus.setItems(FXCollections.observableArrayList(orderStatuses));


    }
}

