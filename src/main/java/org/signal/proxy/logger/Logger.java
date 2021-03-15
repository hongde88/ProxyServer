package org.signal.proxy.logger;

import java.io.IOException;

/**
 * A logger interface for proxy server
 * 
 * @author dnguyen
 *
 */
public interface Logger {
    /**
     * Logs data in the buffer.
     * 
     * @param buffer - The byte array buffer to be logged.
     * @param start  - The starting offset in the buffer to be logged.
     * @param count  - The number of bytes to be logged.
     * @throws IOException - an IO exception
     */
    void log(byte[] buffer, int start, int count) throws IOException;

    /**
     * Logs a string.
     * 
     * @param buffer - The string to be logged.
     */
    void log(String buffer);
}
