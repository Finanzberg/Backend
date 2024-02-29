package de.finanzberg.backend.rest.api.v1.data;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.rest.AuthedWebHandler;
import de.finanzberg.backend.util.StreamUtils;

public class UploadCSV extends AuthedWebHandler {

    public UploadCSV(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        JsonObject json = StreamUtils.readJsonFully(exchange.getRequestBody());
        String csv = json.get("csv").getAsString();

        // TODO: Parse CSV and store in database

        exchange.sendResponseHeaders(200, -1);
    }
}
