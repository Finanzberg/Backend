package de.finanzberg.backend.rest;

import com.sun.net.httpserver.HttpServer;
import de.finanzberg.backend.Finanzberg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WebServer extends Thread {

    private final Finanzberg finanzberg;

    public WebServer(Finanzberg finanzberg) {
        super("WebServer");
        this.finanzberg = finanzberg;
    }

    @Override
    public void run() {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", this.finanzberg.getConfig().web.port), 0);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        server.createContext("/", new RootHandler());


        AtomicInteger threadCount = new AtomicInteger(1);
        server.setExecutor(Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r, "WebServer-" + threadCount.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }));

        server.start();
    }

}
