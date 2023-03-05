package org.lilachshop.requests;

import org.lilachshop.entities.myOrderItem;

import java.util.List;

public class CartRequest extends Request {
    List<myOrderItem> orderItemList;

    public CartRequest(String request, List<myOrderItem> orderItemList) {
        super(request);
        this.orderItemList = orderItemList;
    }

    public List<myOrderItem> getOrderItemList() {
        return orderItemList;
    }
}
