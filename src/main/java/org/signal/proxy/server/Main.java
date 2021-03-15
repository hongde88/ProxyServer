package org.signal.proxy.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.signal.proxy.logger.ConsoleLogger;
import org.signal.proxy.logger.Logger;

/**
 * A class that initializes and runs the proxy server.
 * 
 * @author dnguyen
 *
 */
public class Main {
    public static void main(String[] args) {
        int proxyPort = Constants.DEFAULT_PROXY_PORT;
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Logger logger = new ConsoleLogger(Constants.DEFAULT_PROXY_SERVER_PREFIX, Constants.DEFAULT_ENCODING, true);

        Server proxyServer = new Server(proxyPort, threadPool, logger);
        proxyServer.start();
    }
}
