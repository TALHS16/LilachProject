package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;

import org.lilachshop.requests.ReportsRequest;

public class ChainManagerPanel extends StoreManagerPanel {
    public ChainManagerPanel(Socket socket, Object controller) {
        super(socket, controller);
    }
    public void getAllOrders(){
        sendToServer(new ReportsRequest("get all stores orders"));
    }

    public void getAllComplaints() {
        sendToServer((new ReportsRequest("get all complaints")));
    }
}
