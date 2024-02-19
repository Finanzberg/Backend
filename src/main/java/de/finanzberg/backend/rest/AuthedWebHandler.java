package de.finanzberg.backend.rest;

import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;

import java.util.UUID;

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

    protected User checkSession(HttpExchange exchange) {
        String[] cookies = getCookies(exchange);
        for (String c : cookies) {
            if (c.startsWith("session=")) {
                String session = c.substring(8);
                return this.finanzberg.getUserLogic().getUser(UUID.fromString(session));
            }
        }
        return null;
    }

    public abstract void handleAuthedRequest(HttpExchange exchange) throws Throwable;
}
