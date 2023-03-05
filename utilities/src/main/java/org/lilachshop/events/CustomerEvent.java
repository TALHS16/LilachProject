package org.lilachshop.events;

import org.lilachshop.entities.Customer;

import java.io.Serializable;
import java.util.List;

public class CustomerEvent implements Serializable {

    private final List<Customer> customers;

    public CustomerEvent(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
