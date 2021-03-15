package org.signal.proxy.server;

import java.net.Socket;

import org.signal.proxy.logger.Logger;

/**
 * A class that represents a task handler.
 * 
 * @author dnguyen
 *
 */
public class ServerTaskHandler implements Runnable {
    private Socket clientSocket;
    private Logger logger;

    /**
     * 
     * @param clientSocket - A socket to client.
     * @param logger       - A logger to log data.
     */
    public ServerTaskHandler(Socket clientSocket, Logger logger) {
        this.clientSocket = clientSocket;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            handleRequest();
        } catch (Exception e) {
            this.logger.log("Error occurred in handling a request");
            e.printStackTrace();
        }
    }

    /**
     * Only handles requests with CONNECT and white listed host.
     * 
     * @throws Exception - an exception.
     */
    private void handleRequest() throws Exception {
        ClientConnectHandler ccHandler = new ClientConnectHandler(this.clientSocket);
        ccHandler.parse();

        boolean isConnectRequest = ccHandler.isHttps();
        boolean isWhitelistedHost = Constants.whitelistedHosts.containsKey(ccHandler.getRemoteHost());

        if (isConnectRequest && isWhitelistedHost) {
            String remoteHost = ccHandler.getRemoteHost();
            int remotePort = ccHandler.getRemotePort();
            if (remoteHost != null && remotePort != 0) {
                boolean connectedToRemote = false;
                Socket serverSocket = null;

                try {
                    serverSocket = new Socket(remoteHost, remotePort);
                    connectedToRemote = true;
                    this.logger.log("Connected to " + remoteHost + " on port " + remotePort);
                } catch (Exception e) {
                }

                ccHandler.respondToConnect(connectedToRemote);

                if (connectedToRemote) {
                    this.logger.log("Connected to the remote server");
                    this.handleTunnel(this.clientSocket, serverSocket);
                } else {
                    this.logger.log("Cannot connect to the remote server");
                }
            } else {
                this.logger.log("Invalid remote host/port");
            }
        } else {
            byte[] data = ccHandler.getBytes();
            String requestHeaders = data != null && data.length > 0 ? new String(data, Constants.DEFAULT_ENCODING)
                    : "Empty request or failed to parse client request";
            this.logger.log("Only handle a CONNECT request with a whitelisted host, but received:"
                    + Constants.DEFAULT_LINE_SEPARATOR + requestHeaders);
            ccHandler.respondToUnsupportedRequest();
        }
    }

    /**
     * Handles tunneling between client and remote server.
     * 
     * @param clientSocket - The client socket.
     * @param serverSocket - The remote server socket.
     * @throws Exception - an exception.
     */
    private void handleTunnel(Socket clientSocket, Socket serverSocket) throws Exception {
        Tunneler clientToServerTunneler = new Tunneler(clientSocket, serverSocket);
        Tunneler serverToClientTunneler = new Tunneler(serverSocket, clientSocket);

        clientToServerTunneler.start();
        serverToClientTunneler.start();
        this.logger.log("Started tunneling");

        clientToServerTunneler.join();
        serverToClientTunneler.join();
        this.logger.log("Finished tunneling");

        this.logger.log("Close tunnels");
        clientToServerTunneler.close();
        serverToClientTunneler.close();
    }
}
