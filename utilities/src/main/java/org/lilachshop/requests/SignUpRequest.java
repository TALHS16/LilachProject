package org.lilachshop.requests;

import org.lilachshop.entities.Customer;

public class SignUpRequest extends Request {
    Customer customer;
    String userName;

    public SignUpRequest(Customer customer) {
        super("create new customer");
        this.customer = customer;
    }

    public SignUpRequest(String request,String userName){
        super(request);
        this.userName=userName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getUserName() {
        return userName;
    }
}
