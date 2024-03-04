package de.finanzberg.backend.rest.api.v1.account;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.rest.AbstractWebHandler;
import de.finanzberg.backend.util.StreamUtils;

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

        User user = this.finanzberg.getUserLogic().login(email, password);
        if (user == null) {
            exchange.sendResponseHeaders(500, -1); // this should never happen
            return;
        }
        Headers headers = exchange.getResponseHeaders();
        headers.add("Set-Cookie", "session=" + user.getSession().toString() + "; Path=/; SameSite=None; Secure");

        JsonObject response = new JsonObject();

        response.add("user", user.toJson(false));

        exchange.sendResponseHeaders(201, 0);
        StreamUtils.writeJsonFully(response, exchange.getResponseBody());
    }
}
