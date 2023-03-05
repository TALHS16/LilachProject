package org.lilachshop.events;

import org.lilachshop.entities.myOrderItem;

import java.io.Serializable;
import java.util.List;

public class UpdateCartEvent implements Serializable {
    List<myOrderItem> myOrderItemList;
    String action;

    public UpdateCartEvent(String action, List<myOrderItem> myOrderItemList) {
        this.action = action;
        this.myOrderItemList = myOrderItemList;
    }
    public String getAction() {
        return action;
    }

    public List<myOrderItem> getMyOrderItemList() {
        return myOrderItemList;
    }
}
