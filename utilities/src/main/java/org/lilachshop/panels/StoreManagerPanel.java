package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;
import org.lilachshop.requests.ReportsRequest;

public class StoreManagerPanel extends StoreEmployeePanel {
    public StoreManagerPanel(Socket socket, Object controller) {
        super(socket, controller);
    }

    public void getStoreComplaint(long storeID) {
        sendToServer(new ReportsRequest("get store complaints", storeID));
    }

    public void getStoreOrders(long storeID) {
        sendToServer(new ReportsRequest("get store orders", storeID));
    }

    public void getStoreCatalog(long storeID) {
        sendToServer(new ReportsRequest("get store catalog", storeID));
    }
}
