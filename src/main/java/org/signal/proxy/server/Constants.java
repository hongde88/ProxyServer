package org.signal.proxy.server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that contains constants for this project.
 * 
 * @author dnguyen
 *
 */
public class Constants {
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
    public static final int DEFAULT_PROXY_PORT = 8080;
    public static final String DEFAULT_HTTP_LINE_BREAK = "\r\n";
    public static final String HTTP_CONNECT_METHOD = "CONNECT";
    public static final String DEFAULT_HTTP_VERSION = "HTTP/1.1";
    public static final String HTTP_HEADER_HOST_PREFIX = "Host: ";
    public static final int DEFAULT_HTTP_SECURE_PORT = 443;
    public static final int DEFAULT_HTTP_INSECURE_PORT = 80;
    public static final String DEFAULT_HTTP_METHOD_SPLITTER = " ";
    public static final String DEFAULT_HTTP_HOST_SPLITTER = ":";
    public static final Map<String, Boolean> whitelistedHosts = new HashMap<>() {
        private static final long serialVersionUID = 1L;
        {
            put("api.giphy.com", true);
//            put("www.google.com", true);
//            put("www.youtube.com", true);
        }
    };
    public static final String DEFAULT_PROXY_SERVER_PREFIX = "Proxy Server";
    public static final String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");
}
