package org.signal.proxy.logger.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.signal.proxy.logger.ConsoleLogger;
import org.signal.proxy.logger.Logger;
import org.signal.proxy.server.Constants;

public class ConsoleLoggerTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCapturer = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outputStreamCapturer));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(standardOut);
    }

    @Test
    public void testLogWithoutLineFeed() {
        Logger logger = new ConsoleLogger("Test Prefix", Constants.DEFAULT_ENCODING, false);
        logger.log("console logger");

        Assert.assertEquals("Test Prefix: console logger", this.outputStreamCapturer.toString());
    }

    @Test
    public void testLogWithLineFeed() {
        Logger logger = new ConsoleLogger("Test Prefix", Constants.DEFAULT_ENCODING, true);
        logger.log("console logger");

        Assert.assertEquals("Test Prefix: console logger" + Constants.DEFAULT_LINE_SEPARATOR,
                this.outputStreamCapturer.toString());
    }
}
