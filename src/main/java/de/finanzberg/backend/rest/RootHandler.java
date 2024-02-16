package de.finanzberg.backend.rest;

import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.util.StreamUtils;

import java.io.IOException;

public class RootHandler extends AbstractWebHandler {

    @Override
    public void handleRequest(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        StreamUtils.writeFully("Hello World!",exchange.getResponseBody());

        exchange.getResponseBody().close();
    }
}
