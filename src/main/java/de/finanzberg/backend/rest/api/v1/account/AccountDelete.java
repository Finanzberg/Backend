package de.finanzberg.backend.rest.api.v1.account;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.rest.AuthedWebHandler;

public class AccountDelete extends AuthedWebHandler {

    public AccountDelete(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        JsonObject request = this.getRequestBody(exchange);
        String email;
        String password;
        try {
            email = request.get("email").getAsString();
            password = request.get("password").getAsString();
        } catch (Exception ignored) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        boolean deleted = this.finanzberg.getUserLogic().deleteUser(email, password);
        if (!deleted) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        exchange.sendResponseHeaders(200, 0);
    }
}
