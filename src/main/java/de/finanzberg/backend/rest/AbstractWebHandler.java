package de.finanzberg.backend.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class AbstractWebHandler implements HttpHandler {

    private static boolean checkCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        exchange.getResponseHeaders().add("Access-Control-Max-Age", "86400");
        try {
            if (exchange.getRequestMethod().equals("OPTIONS")) {

                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return true;
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return false;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (checkCors(exchange)) {
            return;
        }
        handleRequest(exchange);
    }

    public abstract void handleRequest(HttpExchange exchange) throws IOException;
}
