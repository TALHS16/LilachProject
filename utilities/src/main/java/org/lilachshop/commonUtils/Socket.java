package org.lilachshop.commonUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Socket {
    public static final int DEFAULT_PORT = 3000;
    public static final String DEFAULT_HOST = "localhost";

    private String ip = null;
    private String hostname;
    private int port;

    public Socket() {
        hostname = DEFAULT_HOST;
        port = DEFAULT_PORT;
        System.out.println("No socket was provided.\nDefaulting to: " + this);
    }

    public Socket(String ipOrHostNameOrSocket) throws UnknownHostException {
        port = 0;
        if (ipOrHostNameOrSocket.contains(":")) {
            String[] splitArgs = ipOrHostNameOrSocket.split(":");
            if (splitArgs[0].contains(".")) {
                ip = splitArgs[0];
            } else hostname = splitArgs[0];
            try {
                port = Integer.parseInt(splitArgs[1]);
            } catch (NumberFormatException e) {
                port = DEFAULT_PORT;
            }
        } else if (ipOrHostNameOrSocket.contains(".")) {
            ip = ipOrHostNameOrSocket;
            hostname = InetAddress.getByName(ip).getHostName();
            port = DEFAULT_PORT;
        } else {
            hostname = ipOrHostNameOrSocket.isEmpty() ? "localhost" : ipOrHostNameOrSocket;
            ip = InetAddress.getByName(hostname).getHostAddress();
            port = DEFAULT_PORT;
        }
        System.out.println("Connecting to " + this);
    }

    public Socket(int port) {
        this.hostname = DEFAULT_HOST;
        this.port = port;
        System.out.println("Connecting to" + this);
    }

    public Socket(String ip, int port) {
        this.ip = ip;
        this.port = port;
        System.out.println("Connecting to " + this);
    }

    @Override
    public String toString() {
        return hostname + ":" + port;
    }

    public String getIp() {
        return ip;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
