package org.lilachshop.requests;

import org.lilachshop.entities.Complaint;
import org.lilachshop.entities.Order;

public class UserComplaintRequest extends Request {
    Complaint  complaint;
    Order order;
    public Complaint getComplaint() {
        return complaint;
    }

    public UserComplaintRequest(String request, Complaint complaint, Order order) {
        super(request);
        this.complaint = complaint;
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public UserComplaintRequest(String request) {
        super(request);
    }
}
