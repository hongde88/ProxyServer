package org.signal.proxy.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import org.signal.proxy.logger.Logger;

/**
 * A proxy server class that handles HTTP tunneling
 * 
 * @author dnguyen
 *
 */
public class Server extends Thread {
    private final int port;
    private final ExecutorService threadPool;
    private final Logger logger;
    private ServerSocket serverSocket;
    private long requestCount;

    /**
     * 
     * @param port       - a local port for the proxy server.
     * @param threadPool - a thread pool to handle server tasks.
     * @param logger     - a logger to log data to system out.
     * @throws IOException - an IO exception.
     */
    public Server(int port, ExecutorService threadPool, Logger logger) {
        this.port = port;
        this.threadPool = threadPool;
        this.logger = logger;
        this.requestCount = 0;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            this.logger.log("Proxy server started on port " + this.port);
            while (true) {
                Socket clientSocket = this.serverSocket.accept();
                if (clientSocket != null) {
                    this.threadPool.execute(new ServerTaskHandler(clientSocket, logger));
                    requestCount++;
                    this.logger.log(requestCount + " request(s) processed");
                }
            }
        } catch (IOException e) {
            this.logger.log("Errors occurred: " + e.getMessage());
        }
    }
}
