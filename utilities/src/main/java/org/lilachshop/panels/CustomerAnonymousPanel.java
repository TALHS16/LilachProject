package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;
import org.lilachshop.requests.*;
import org.lilachshop.entities.*;


public class CustomerAnonymousPanel extends Panel {

    public CustomerAnonymousPanel(Socket socket, Object controller) {
        super(socket, controller);
    }

    public void sendGetGeneralCatalogRequestToServer() {
        sendToServer(new CatalogRequest("get catalog", 1));
//        sendToServer(new DebugRequest("write catalog"));
    }

    public void sendCustomerLoginRequest(String userName, String password) {
        sendToServer(new CustomerLoginRequest("customer login request", userName, password));
    }

    public void sendGetFilteredCatalog(long id_catalog, int minPrice, int maxPrice, Color color, ItemType type) {
        sendToServer(new CatalogRequest("get catalog by filter", id_catalog, minPrice, maxPrice, color, type));
    }

    public void sendSignUpRequest(Customer customer) {
        sendToServer(new SignUpRequest(customer));
    }

    public void getAllStores() {
        sendToServer((new StoreRequest("get all stores")));
    }

    public void checkIfUserNameTaken(String username) {
        sendToServer(new SignUpRequest("check if username taken", username));
    }
}
