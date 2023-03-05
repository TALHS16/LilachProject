/**
 * Sample Skeleton for 'history.fxml' Controller Class
 */

package org.lilachshop.customerclient;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.lilachshop.entities.Complaint;
import org.lilachshop.entities.Customer;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.myOrderItem;

public class HistoryController implements Initializable {

    List<myOrderItem> myFlowers = new LinkedList<myOrderItem>();
    List<Order> myOrders;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="layout"
    private VBox itemLayout; // Value injected by FXMLLoader

    @FXML // fx:id="name"
    private Label name; // Value injected by FXMLLoader
    private Customer customer;
    private CustomerApp controller;

    @FXML
    void returnToCatalog(MouseEvent event) {
        CustomerApp.getCustomerCatalog();

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText("שלום, " + CustomerApp.getMyCustomer().getName());
        this.myFlowers = CustomerApp.getMyFlowers();
        this.myOrders = CustomerApp.getMyOrders();
        for (Order order: myOrders)
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("historyItem.fxml"));
            try {
                AnchorPane anchorPane = fxmlLoader.load();
                HistoryItemController historyItemController = fxmlLoader.getController();
                //set the photo,name,price and amount from this flower
                historyItemController.setData(order);
                Complaint complaint = order.getComplaint();
                if(complaint != null){
                    historyItemController.disablePostComplaintBtn();
                }
                else{ // no complaint send -> don't show the complaint
                    historyItemController.disableShowComplaintBtn();
                }
                itemLayout.getChildren().add(anchorPane);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
