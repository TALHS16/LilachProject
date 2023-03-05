package org.lilachshop.panels;

import org.lilachshop.commonUtils.Socket;
import org.lilachshop.requests.DebugRequest;

public class ExamplePanel extends Panel {
    private DebugRequest debugRequest;

    public ExamplePanel(Socket socket, Object controller) {
        super(socket, controller);
    }

    public void sendMessageToServer(String msg) {
        sendToServer(new DebugRequest(msg));
    }
}
