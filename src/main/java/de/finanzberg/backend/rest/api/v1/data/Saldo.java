package de.finanzberg.backend.rest.api.v1.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.logic.DataLogic;
import de.finanzberg.backend.rest.AuthedWebHandler;
import de.finanzberg.backend.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Saldo extends AuthedWebHandler {

    public Saldo(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        DataLogic dataLogic = finanzberg.getDataLogic();
        List<BankStatement> statements = dataLogic.loadStatements(user);
        if (statements.isEmpty()) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        statements.sort(Comparator.comparingLong(o -> o.getDate().toEpochMilli()));
        JsonObject response = new JsonObject();

        Instant start = statements.get(0).getDate();
        Instant end = statements.get(statements.size() - 1).getDate();

        int days = (int) ((end.getEpochSecond() - start.getEpochSecond()) / 86400);

        Map<Long, Double> map = new HashMap<>(days);
        Map<Long, Double> results = new HashMap<>();

        for (BankStatement statement : statements) {
            long date = statement.getDate().getEpochSecond();
            Double previous = map.getOrDefault(date, statement.getBalance());

            map.put(date, (previous + statement.getBalance()) / 2d);
        }

        double value = 0d;
        for (int i = 0; i < days; i++) {
            long epochSecond = start.plusSeconds(i * 86400L).getEpochSecond();
            value = map.getOrDefault(epochSecond, value);
            results.put(epochSecond, value);
        }

        JsonArray labels = new JsonArray(statements.size());
        JsonArray data = new JsonArray(statements.size());

        results.forEach((date, balance) -> {
            labels.add(date);
            data.add(balance);
        });

        response.add("labels", labels);
        response.add("data", data);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        String json = Finanzberg.GSON.toJson(response);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(200, bytes.length);

        StreamUtils.write(bytes, exchange.getResponseBody());
    }
}
