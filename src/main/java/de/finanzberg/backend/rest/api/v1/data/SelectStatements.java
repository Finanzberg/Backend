package de.finanzberg.backend.rest.api.v1.data;

import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.logic.DataLogic;
import de.finanzberg.backend.rest.AuthedWebHandler;
import de.finanzberg.backend.util.StreamUtils;

public class SelectStatements extends AuthedWebHandler {

    public SelectStatements(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        DataLogic dataLogic = finanzberg.getDataLogic();
        JsonArray response = dataLogic.loadStatementsJson(user);

        StreamUtils.writeSaveJson(response, exchange, 200);
    }
}
