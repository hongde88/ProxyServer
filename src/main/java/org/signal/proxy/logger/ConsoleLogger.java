package org.signal.proxy.logger;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

import org.signal.proxy.server.Constants;

/**
 * Logs data to console with a given encoding.
 * 
 * @author dnguyen
 *
 */
public class ConsoleLogger implements Logger {
    private final PrintStream stream;
    private final String prefix;
    private final Charset encoding;
    private final boolean addLineFeed;
    private final String lineSeparator;

    /**
     * @param stream      - The stream to write log to.
     * @param prefix      - The prefix for every log entry.
     * @param encoding    - The encoding for the byte data
     * @param addLineFeed - Whether a line feed is appended to each log entry.
     */
    public ConsoleLogger(String prefix, Charset encoding, boolean addLineFeed) {
        this.stream = System.out;
        this.prefix = prefix;
        this.encoding = encoding;
        this.addLineFeed = addLineFeed;
        this.lineSeparator = Constants.DEFAULT_LINE_SEPARATOR;
    }

    /**
     * Logs data to system out.
     * 
     * @param data - data to be logged.
     */
    public void log(String data) {
        String lineFeed = this.addLineFeed ? this.lineSeparator : "";
        String logEntry = String.format("%s: %s%s", this.prefix, data, lineFeed);
        stream.print(logEntry);
    }

    @Override
    public void log(byte[] buffer, int start, int count) throws IOException {
        // This is just a simple implementation for log.
        // Any other types of logger could have different implementations.
        String logData = new String(buffer, start, count, this.encoding);
        String lineFeed = this.addLineFeed ? this.lineSeparator : "";
        String logEntry = String.format("%s: %s%s", this.prefix, logData, lineFeed);
        stream.print(logEntry);
    }

}
