package org.lilachshop.server;

import org.jetbrains.annotations.Nullable;
import org.lilachshop.entities.Item;
import org.lilachshop.entities.Catalog;

import java.net.InetAddress;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        try {
            int port = 3000;
            try {
                port = args.length > 0 ? Integer.parseInt(args[0]) : port;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            InetAddress IP = InetAddress.getLocalHost();
            String host_name = IP.getHostName();
            System.out.println("RUNNING SERVER ON " + host_name + ":" + port + " ...");
            System.out.println("SERVER: INITIALIZING SERVER");
            LilachServer server = new LilachServer(port);
            System.out.println("SERVER: LISTENING");
            server.listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
