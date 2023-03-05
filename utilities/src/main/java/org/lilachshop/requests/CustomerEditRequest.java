package org.lilachshop.requests;

import org.lilachshop.entities.Customer;
import org.lilachshop.events.CustomerEvent;

public class CustomerEditRequest extends Request {
    private Customer customer;

    private long customerID;

    public enum Messages {
        GET_ALL_CUSTOMERS,
        CREATE_UPDATE_CUSTOMER,
        DELETE_CUSTOMER_BY_ID;
    }

    public CustomerEditRequest(Messages request) {
        super(request.name());
    }

    public CustomerEditRequest(Messages request, Customer customer) {
        super(request.name());
        this.customer = customer;
    }

    public CustomerEditRequest(Messages request, long customerID) {
        super(request.name());
        this.customerID = customerID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public long getCustomerID() {
        return customerID;
    }
}
