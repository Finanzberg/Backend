package de.finanzberg.backend.rest.api.v1.data;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;
import de.finanzberg.backend.db.Budget;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.logic.parser.AbstractParser;
import de.finanzberg.backend.rest.AuthedWebHandler;
import de.finanzberg.backend.util.StreamUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CreateBudget extends AuthedWebHandler {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM");
    public CreateBudget(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        JsonObject json = StreamUtils.readJsonFully(exchange.getRequestBody());;
        String name = json.get("name").getAsString();
        String monthlyBalance = json.get("monthlyBalance").getAsString();
        String startDate = json.get("startDate").getAsString();

        Budget.save(user, finanzberg, new Budget(finanzberg, name,Integer.valueOf(monthlyBalance),-1,DATE_FORMAT.parse(startDate).toInstant()));
        exchange.sendResponseHeaders(200, -1);
    }
}
