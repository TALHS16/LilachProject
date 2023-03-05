package org.lilachshop.employeeclient;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.lilachshop.entities.Item;
import org.lilachshop.entities.myOrderItem;

import java.util.Objects;

public class ItemViewController {
    @FXML
    private Label ItemPrice;
    @FXML
    private Label Qantity;
    @FXML
    private Label itemName;

    public void setData(myOrderItem myOrderItem){
        ItemPrice.setText(String.valueOf(myOrderItem.getItem().getPrice()));
        itemName.setText(String.valueOf(myOrderItem.getName()));
        Qantity.setText(String.valueOf(myOrderItem.getCount()));
    }
}
