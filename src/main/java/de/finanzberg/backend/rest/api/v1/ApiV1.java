package de.finanzberg.backend.rest.api.v1;

import com.sun.net.httpserver.HttpServer;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.rest.api.v1.account.AccountAuth;
import de.finanzberg.backend.rest.api.v1.account.AccountChange;
import de.finanzberg.backend.rest.api.v1.account.AccountCreate;
import de.finanzberg.backend.rest.api.v1.account.AccountDelete;
import de.finanzberg.backend.rest.api.v1.account.AccountSession;

public class ApiV1 {

    private static final String PATH = "api/v1/";

    public static void register(String parent, HttpServer server, Finanzberg finanzberg) {
        server.createContext(parent + PATH + "account/auth", new AccountAuth(finanzberg));
        server.createContext(parent + PATH + "account/change", new AccountChange(finanzberg));
        server.createContext(parent + PATH + "account/create", new AccountCreate(finanzberg));
        server.createContext(parent + PATH + "account/delete", new AccountDelete(finanzberg));
        server.createContext(parent + PATH + "account/session", new AccountSession(finanzberg));
    }
}
