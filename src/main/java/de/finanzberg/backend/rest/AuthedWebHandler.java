package de.finanzberg.backend.rest;

import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;

public abstract class AuthedWebHandler extends AbstractWebHandler {

    public AuthedWebHandler(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleRequest(HttpExchange exchange) throws Throwable {
        User user = checkSession(exchange);
        if (user != null) {
            handleAuthedRequest(exchange);
        } else {
            exchange.sendResponseHeaders(401, -1);
        }
    }

    public abstract void handleAuthedRequest(HttpExchange exchange) throws Throwable;
}
