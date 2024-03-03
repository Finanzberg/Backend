package de.finanzberg.backend.rest.api.v1.account;

import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.rest.AuthedWebHandler;
import de.finanzberg.backend.util.StreamUtils;

import java.nio.charset.StandardCharsets;

public class AccountAvatar extends AuthedWebHandler {

    public AccountAvatar(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        byte[] avatar = user.getAvatar().getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "plain/text");
        exchange.sendResponseHeaders(200, avatar.length);
        StreamUtils.write(avatar, exchange.getResponseBody());
    }
}
