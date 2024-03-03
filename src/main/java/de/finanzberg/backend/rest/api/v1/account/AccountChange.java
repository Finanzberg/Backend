package de.finanzberg.backend.rest.api.v1.account;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.rest.AuthedWebHandler;

public class AccountChange extends AuthedWebHandler {

    public AccountChange(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        JsonObject request = this.getRequestBody(exchange);
        String name;
        String password;
        String avatar;
        try {
            if (request.has("name")) {
                name = request.get("name").getAsString();
            } else {
                name = user.getName();
            }
            if (request.has("password")) {
                password = request.get("password").getAsString();
            } else {
                password = user.getPassword();
            }
            if (request.has("avatar")) {
                avatar = request.get("avatar").getAsString();
            } else {
                avatar = user.getAvatar();
            }
        } catch (Exception ignored) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        boolean changed = this.finanzberg.getUserLogic().changeUser(name, password, avatar);
        if (!changed) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }
        exchange.sendResponseHeaders(200, 0);
    }
}
