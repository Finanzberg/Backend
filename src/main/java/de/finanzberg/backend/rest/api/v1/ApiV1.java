package de.finanzberg.backend.rest.api.v1;

import com.sun.net.httpserver.HttpServer;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.rest.api.v1.account.AccountAuth;
import de.finanzberg.backend.rest.api.v1.account.AccountAvatar;
import de.finanzberg.backend.rest.api.v1.account.AccountChange;
import de.finanzberg.backend.rest.api.v1.account.AccountCreate;
import de.finanzberg.backend.rest.api.v1.account.AccountDelete;
import de.finanzberg.backend.rest.api.v1.account.AccountSession;
import de.finanzberg.backend.rest.api.v1.data.Saldo;
import de.finanzberg.backend.rest.api.v1.data.SelectBudgets;
import de.finanzberg.backend.rest.api.v1.data.SelectStatements;
import de.finanzberg.backend.rest.api.v1.data.UploadCSV;

public class ApiV1 {

    private static final String PATH = "api/v1/";

    public static void register(String parent, HttpServer server, Finanzberg finanzberg) {
        // Account
        server.createContext(parent + PATH + "account/auth", new AccountAuth(finanzberg));
        server.createContext(parent + PATH + "account/avatar", new AccountAvatar(finanzberg));
        server.createContext(parent + PATH + "account/change", new AccountChange(finanzberg));
        server.createContext(parent + PATH + "account/create", new AccountCreate(finanzberg));
        server.createContext(parent + PATH + "account/delete", new AccountDelete(finanzberg));
        server.createContext(parent + PATH + "account/session", new AccountSession(finanzberg));

        // Data
        server.createContext(parent + PATH + "data/saldo", new Saldo(finanzberg));
        server.createContext(parent + PATH + "data/budgets", new SelectBudgets(finanzberg));
        server.createContext(parent + PATH + "data/statements", new SelectStatements(finanzberg));
        server.createContext(parent + PATH + "data/upload/csv", new UploadCSV(finanzberg));
    }
}
