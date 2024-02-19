package de.finanzberg.backend.rest.api.v1.account;

import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.rest.AuthedWebHandler;

public class AccountSession extends AuthedWebHandler {

    public AccountSession(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange) throws Throwable {
        exchange.sendResponseHeaders(200, 0);
    }
}
