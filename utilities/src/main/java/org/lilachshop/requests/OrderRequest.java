package org.lilachshop.requests;

import org.lilachshop.entities.Customer;
import org.lilachshop.entities.Order;

public class OrderRequest extends Request{
    private long storeID;

    Order order;

    long customerID;
    public long getCustomerID() {
        return customerID;
    }

    public OrderRequest(String request, Order order) {
        super(request);
        this.order = order;
    }

    public OrderRequest(String request, long storeID ,String store) {
        super(request);
        this.storeID = storeID;
    }

    public OrderRequest(String request, long customerID) {
        super(request);
        this.customerID = customerID;
    }

    public Order getOrder() {
        return order;
    }

    public long getStoreID() {
        return storeID;
    }
}
