package org.lilachshop.requests;

import org.lilachshop.entities.Complaint;
import org.lilachshop.entities.Order;

public class SupportComplaintRequest extends Request {
    String reply;
    Complaint complaint;
    Order order;

    public Complaint getComplaint() {
        return complaint;
    }

    public String getReply() {
        return reply;
    }

    public SupportComplaintRequest(String request) {
        super(request);
    }

    public SupportComplaintRequest(String request, String reply, Complaint complaint, Order order) {
        super(request);
        this.reply = reply;
        this.complaint = complaint;
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
