package de.finanzberg.backend.rest.api.v1.data;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.db.BankStatement;
import de.finanzberg.backend.db.User;
import de.finanzberg.backend.logic.parser.AbstractParser;
import de.finanzberg.backend.rest.AuthedWebHandler;
import de.finanzberg.backend.util.StreamUtils;

import java.util.List;

public class UploadCSV extends AuthedWebHandler {

    public UploadCSV(Finanzberg finanzberg) {
        super(finanzberg);
    }

    @Override
    public void handleAuthedRequest(HttpExchange exchange, User user) throws Throwable {
        JsonObject json = StreamUtils.readJsonFully(exchange.getRequestBody());
        String csv = json.get("csv").getAsString();
        String bank = json.get("bank").getAsString();
        AbstractParser abstractParser = this.finanzberg.getParsers().get("CSV." + bank);
        if (abstractParser == null) {
            throw new IllegalArgumentException("No parser for bank " + bank);
        }
        List<BankStatement> bankStatements = abstractParser.parseStatements(csv);
        if (bankStatements.isEmpty()) exchange.sendResponseHeaders(400, -1);
        BankStatement.save(user, finanzberg, bankStatements);
        exchange.sendResponseHeaders(200, -1);
    }
}
