package org.lilachshop.employeeclient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.lilachshop.entities.Item;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.myOrderItem;

import java.io.IOException;

public class ShowOrderController {

    @FXML
    private HBox hBoxItems;
    Order order;

    public void setData(Order order) throws IOException {
        this.order = order;
        for(myOrderItem myOrderItem:order.getItems()){
            Item item = myOrderItem.getItem();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ItemView.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            ItemViewController itemViewController = fxmlLoader.getController();
            itemViewController.setData(myOrderItem);
            hBoxItems.getChildren().add(anchorPane);

                // todo: present items on screen
        }
    }

}
