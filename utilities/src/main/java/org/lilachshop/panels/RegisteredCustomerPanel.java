package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;
import org.lilachshop.entities.Complaint;
import org.lilachshop.entities.Order;
import org.lilachshop.requests.OrderRequest;
import org.lilachshop.requests.UserComplaintRequest;

public class RegisteredCustomerPanel extends Panel {
    public RegisteredCustomerPanel(Socket socket, Object controller) {
        super(socket, controller);
    }

    public void sendComplaintToServer(Complaint complaint, Order order) {
        sendToServer(new UserComplaintRequest("post new complaint", complaint, order));
    }

    public void cancelOrderRequest(Order order) {
        sendToServer(new OrderRequest("cancel order", order));
    }
}
