package org.lilachshop.panels;

import org.greenrobot.eventbus.EventBus;
import org.lilachshop.commonUtils.Socket;
import org.lilachshop.panels.ocsf.AbstractClient;

public class LilachClient extends AbstractClient {
    private EventBus bus = null;

    private LilachClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void connectionClosed() {
        System.out.println("Client connection closed.");
    }

    @Override
    protected void connectionEstablished() {
        System.out.println("Client connection established.");
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        bus.post(msg);
    }

    public EventBus getBus() {
        return bus;
    }

    public static LilachClient getClient(Socket socket) {
        LilachClient client;
        client = new LilachClient(socket.getHostname(), socket.getPort());

        if (client.getBus() == null) {
            client.bus = EventBus.builder().build();
        }
        return client;
    }
}
