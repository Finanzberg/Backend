package de.finanzberg.backend.rest;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractWebHandler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("Finanzberg-Web");

    protected final Finanzberg finanzberg;

    protected AbstractWebHandler(Finanzberg finanzberg) {
        this.finanzberg = finanzberg;
    }

    private static boolean checkCors(HttpExchange exchange, Finanzberg finanzberg) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", finanzberg.getConfig().web.origin);
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
        try (exchange) { // close the exchange automatically after handling
            if (checkCors(exchange, this.finanzberg)) {
                return;
            }
            this.handleRequest(exchange);
        } catch (Throwable throwable) {
            LOGGER.error("Error while handling request", throwable);
            exchange.sendResponseHeaders(500, -1);
        }
    }

    public abstract void handleRequest(HttpExchange exchange) throws Throwable;

    protected JsonObject getRequestBody(HttpExchange exchange) {
        return StreamUtils.readJsonFully(exchange.getRequestBody());
    }

    // Check if session cookie is present and valid
    protected String[] getCookies(HttpExchange exchange) {
        String cookie = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookie == null) {
            return new String[0];
        }
        return cookie.split(";");
    }
}
