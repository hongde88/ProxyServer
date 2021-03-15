package org.signal.proxy.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * A class to parse client HTTP request headers.
 * 
 * @author dnguyen
 *
 */
public class ClientConnectHandler {
    private String remoteHost;
    private int remotePort;
    private String method;
    private boolean isHttps;
    private String httpVersion;
    private byte[] bytes;
    private final ByteArrayOutputStream readData;
    private Socket clientSocket;

    /**
     * 
     * @param clientSocket - a client socket.
     */
    public ClientConnectHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.readData = new ByteArrayOutputStream(Constants.DEFAULT_BUFFER_SIZE);
    }

    /**
     * Parses the client headers.
     */
    public void parse() {
        try {
            String header = this.readLine();

            if (header == null || header.isEmpty()) {
                throw new Exception();
            }

            String[] request = header.split(Constants.DEFAULT_HTTP_METHOD_SPLITTER);
            this.method = request.length >= 1 ? request[0] : "";
            this.httpVersion = request.length >= 3 ? request[2] : Constants.DEFAULT_HTTP_VERSION;
            this.isHttps = this.method.equalsIgnoreCase(Constants.HTTP_CONNECT_METHOD);

            for (String line = this.readLine(); line != null && !line.isEmpty(); line = this.readLine()) {
                if (line.startsWith(Constants.HTTP_HEADER_HOST_PREFIX)) {
                    String[] hostAndPort = line.split(Constants.DEFAULT_HTTP_HOST_SPLITTER);
                    this.remoteHost = hostAndPort[1].trim();
                    if (hostAndPort.length == 3) {
                        this.remotePort = Integer.parseInt(hostAndPort[2]);
                    } else if (this.isHttps) {
                        this.remotePort = Constants.DEFAULT_HTTP_SECURE_PORT;
                    } else {
                        this.remotePort = Constants.DEFAULT_HTTP_SECURE_PORT;
                    }
                }
            }

            this.bytes = this.readData.toByteArray();
        } catch (Exception e) {
            this.remoteHost = null;
            this.remotePort = 0;
            this.bytes = null;
        }
    }

    /**
     * Responds to a CONNECT request.
     * 
     * @param connectedToRemote - If it is connected to the remote server.
     * @throws IOException - An IO exception.
     */
    public void respondToConnect(boolean connectedToRemote) throws IOException {
        OutputStreamWriter writer = this.getOutputWriter();

        if (connectedToRemote) {
            writer.write(this.httpVersion + " 200 Connection Established" + Constants.DEFAULT_HTTP_LINE_BREAK);
            writer.write("Proxy-agent: SignalProxy/1.0" + Constants.DEFAULT_HTTP_LINE_BREAK);
            writer.write(Constants.DEFAULT_HTTP_LINE_BREAK);
            writer.flush();
        } else {
            writer.write(this.httpVersion + " 502 Bad Gateway" + Constants.DEFAULT_HTTP_LINE_BREAK);
            writer.write("Proxy-agent: SignalProxy/1.0" + Constants.DEFAULT_HTTP_LINE_BREAK);
            writer.write(Constants.DEFAULT_HTTP_LINE_BREAK);
            writer.flush();
            writer.close();
        }
    }

    /**
     * Responds to an unsupported request i.e not whitelisted host or not CONNECT.
     * 
     * @throws IOException
     */
    public void respondToUnsupportedRequest() throws IOException {
        OutputStreamWriter writer = this.getOutputWriter();

        writer.write(this.httpVersion + " 412 Precondition Failed" + Constants.DEFAULT_HTTP_LINE_BREAK);
        writer.write("Proxy-agent: SignalProxy/1.0" + Constants.DEFAULT_HTTP_LINE_BREAK);
        writer.write(Constants.DEFAULT_HTTP_LINE_BREAK);
        writer.flush();
        writer.close();
    }

    /**
     * Gets an output writer for writing a response.
     * 
     * @return - an instance output stream writer.
     * @throws IOException
     */
    private OutputStreamWriter getOutputWriter() throws IOException {
        return new OutputStreamWriter(this.clientSocket.getOutputStream());
    }

    /**
     * Reads line from the input stream.
     * 
     * @return - A string read from the input stream.
     * @throws IOException
     */
    private String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStream inStream = this.clientSocket.getInputStream();
        int data;

        while ((data = inStream.read()) != -1) {
            this.readData.write(data);
            sb.append((char) data);
            int sbLen = sb.length();
            if (sbLen >= 2 && sb.substring(sbLen - 2).equals(Constants.DEFAULT_HTTP_LINE_BREAK)) {
                return sb.substring(0, sbLen - 2);
            }
        }

        return sb.length() == 0 ? null : sb.toString();
    }

    /**
     * @return the remote host
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * @return the remote port
     */
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @return the isHttps
     */
    public boolean isHttps() {
        return isHttps;
    }

    /**
     * @return the bytes
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * @return the httpVersion
     */
    public String getHttpVersion() {
        return httpVersion;
    }
}
