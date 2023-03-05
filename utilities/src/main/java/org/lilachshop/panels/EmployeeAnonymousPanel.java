package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;
import org.lilachshop.requests.EmployeeLoginRequest;

public class EmployeeAnonymousPanel extends Panel {
    public EmployeeAnonymousPanel(Socket socket, Object controller) {
        super(socket, controller);
    }

    public void sendLoginRequest(String userName, String password) {
        sendToServer(new EmployeeLoginRequest("client login", userName, password));
    }
}
