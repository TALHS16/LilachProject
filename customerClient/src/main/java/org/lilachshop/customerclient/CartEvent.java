package org.lilachshop.customerclient;

import javafx.scene.layout.AnchorPane;
import org.lilachshop.entities.myOrderItem;

public class CartEvent {
    public AnchorPane object;
    public myOrderItem updateFlower;
    String action;

    //to Update list items and prices
    public CartEvent(AnchorPane anchorPane, myOrderItem flower, String doAction ) {
        object = anchorPane;
        updateFlower = flower;
        action = doAction;
    }

}
