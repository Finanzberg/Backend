package de.finanzberg.backend.rest.api.v1.account;

import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.rest.AuthedWebHandler;
import de.finanzberg.backend.util.StreamUtils;

public class AccountAvatar extends AuthedWebHandler {

    public AccountAvatar(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        exchange.sendResponseHeaders(200, -1);
        StreamUtils.writeFully(user.getAvatar(), exchange.getResponseBody());
    }
}
