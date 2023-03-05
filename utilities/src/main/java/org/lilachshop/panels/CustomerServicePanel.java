package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;
import org.lilachshop.entities.Complaint;
import org.lilachshop.entities.Order;
import org.lilachshop.requests.SupportComplaintRequest;

public class CustomerServicePanel extends SignedInEmployeePanel {
    public CustomerServicePanel(Socket socket, Object controller) {
        super(socket, controller);
    }

    public void GetAllClientsComplaintsRequestToServer() {
        sendToServer(new SupportComplaintRequest("get all complaints"));
    }

    public void ReplyToComplaintRequestToServer(Complaint complaint, String reply, Order order) {
        sendToServer(new SupportComplaintRequest("reply to customer complaint", reply, complaint, order));
    }
}
