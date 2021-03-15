package org.signal.proxy.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * A class that provides a one-way data flow from input socket to output socket.
 * 
 * @author dnguyen
 *
 */
public class Tunneler extends Thread {
    private final Socket inSocket;
    private final Socket outSocket;
    private final InputStream inStream;
    private final OutputStream outStream;

    /**
     * 
     * @param inSocket  - The input socket
     * @param outSocket - The output socket
     * @throws IOException - an IO exception
     */
    public Tunneler(Socket inSocket, Socket outSocket) throws IOException {
        this.inSocket = inSocket;
        this.outSocket = outSocket;
        this.inStream = this.inSocket.getInputStream();
        this.outStream = this.outSocket.getOutputStream();
    }

    @Override
    public void run() {
        byte[] buffer = new byte[Constants.DEFAULT_BUFFER_SIZE];
        int bytesRead = 0; // the number of bytes read from the input stream

        try {
            while ((bytesRead = this.inStream.read(buffer)) != -1) {
                this.outStream.write(buffer, 0, bytesRead);
                this.outStream.flush();
            }
        } catch (IOException e) {
            this.close();
        }
    }

    /**
     * Closes the tunnel.
     */
    public void close() {
        try {
            if (!this.inSocket.isClosed()) {
                this.inSocket.close();
            }

            if (!this.outSocket.isClosed()) {
                this.outSocket.close();
            }
        } catch (IOException pass) {
        }
    }
}
