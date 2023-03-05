package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;
import org.lilachshop.entities.Order;
import org.lilachshop.entities.User;
import org.lilachshop.requests.OrderRequest;
import org.lilachshop.requests.ReportsRequest;
import org.lilachshop.requests.SignOutRequest;
import org.lilachshop.requests.StoreRequest;

public class SignedInEmployeePanel extends Panel {

    public SignedInEmployeePanel(Socket socket, Object controller) {
        super(socket, controller);
    }

    public void sendSignOutRequestToServer(User employee) {
        sendToServer(new SignOutRequest(SignOutRequest.Messages.SIGN_OUT, employee));
    }

    public void sendUpdatedOrder(Order order){
        sendToServer(new OrderRequest("update order", order));
    }

    public void sendGetOrdersFromServer(Long storeID) {
        sendToServer(new OrderRequest("get store orders",storeID,"store"));
    }

    public void getAllStores() {
        sendToServer((new StoreRequest("get all stores")));
    }
    public void getAllStoresForReports() {
        sendToServer((new StoreRequest("get all stores")));
    }
}
