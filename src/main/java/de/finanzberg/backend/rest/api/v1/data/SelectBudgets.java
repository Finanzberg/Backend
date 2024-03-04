package de.finanzberg.backend.rest.api.v1.data;

import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.logic.DataLogic;
import de.finanzberg.backend.rest.AuthedWebHandler;
import de.finanzberg.backend.util.StreamUtils;

public class SelectBudgets extends AuthedWebHandler {

    public SelectBudgets(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        DataLogic dataLogic = finanzberg.getDataLogic();

        StreamUtils.writeSaveJson(dataLogic.loadBudgetsJson(user), exchange, 200);
    }
}