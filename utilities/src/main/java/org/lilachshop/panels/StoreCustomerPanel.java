package org.lilachshop.panels;


import org.lilachshop.commonUtils.Socket;
import org.lilachshop.entities.*;
import org.lilachshop.requests.*;

import java.util.List;

public class StoreCustomerPanel extends RegisteredCustomerPanel {
    public StoreCustomerPanel(Socket socket, Object controller) {
        super(socket, controller);
    }

    public void sendComplaintToServer(Complaint complaint, Order order) {
        sendToServer(new UserComplaintRequest("post new complaint", complaint, order));
    }

    public void sendGetCatalogRequestToServer(long id_store) {
        sendToServer(new CatalogRequest("get catalog", id_store));
    }

    public void sendGetFilteredCatalog(long id_catalog, int minPrice, int maxPrice, Color color, ItemType type) {
        sendToServer(new CatalogRequest("get catalog by filter", id_catalog, minPrice, maxPrice, color, type));
    }

    public void sendGetAllOrdersToServer(long customerId) {
        sendToServer(new OrderRequest("get all clients orders", customerId));
    }

    public void sendNewOrderCreationToServer(Order myOrder) {
        sendToServer(new OrderRequest("create new order", myOrder));
    }


    public void sendSignOutRequestToServer(User myCustomer) {
        sendToServer(new SignOutRequest(SignOutRequest.Messages.SIGN_OUT, myCustomer));
    }

    public void getAllStores() {
        sendToServer((new StoreRequest("get all stores")));
    }

    public void refreshCart(List<myOrderItem> orderItemList) {
        sendToServer(new CartRequest("refresh cart items", orderItemList));
    }

}
