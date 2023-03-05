package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;
import org.lilachshop.entities.User;
import org.lilachshop.requests.Request;
import org.lilachshop.requests.SignOutRequest;

import java.io.IOException;

public abstract class Panel {
    private final LilachClient lilachClient;
    private Object controller;

    public Panel(Socket socket, Object controller) {
        lilachClient = LilachClient.getClient(socket);
        lilachClient.getBus().register(controller);
        this.controller = controller;
        try {
            lilachClient.openConnection();
        } catch (IOException e) {
            System.out.println("Client failed to open connection to server.");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        lilachClient.getBus().unregister(controller);
        try {
            lilachClient.closeConnection();
        } catch (IOException e) {
            System.out.println("Client failed to close connection to server.");
            e.printStackTrace();
        }
    }

    protected void sendToServer(Request request) {
        try {
            lilachClient.sendToServer(request);
        } catch (IOException e) {
            System.out.println("Unable to send request to server.");
            e.printStackTrace();
        }
    }

}

