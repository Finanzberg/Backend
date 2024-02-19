package de.finanzberg.backend.rest.api.v1.account;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.rest.AbstractWebHandler;

public class AccountCreate extends AbstractWebHandler {

    public AccountCreate(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleRequest(HttpExchange exchange) throws Throwable {
        JsonObject request = this.getRequestBody(exchange);
        String email;
        String name;
        String password;
        String avatar;
        try {
            email = request.get("email").getAsString();
            name = request.get("name").getAsString();
            password = request.get("password").getAsString();
            avatar = request.get("avatar").getAsString();
        } catch (Exception ignored) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        boolean created = this.finanzberg.getUserLogic().createUser(email, name, password, avatar);
        if (!created) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        exchange.sendResponseHeaders(200, 0);
    }
}
