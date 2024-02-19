package de.finanzberg.backend.rest;

import com.sun.net.httpserver.HttpServer;
import de.finanzberg.backend.Finanzberg;
import de.finanzberg.backend.rest.api.v1.ApiV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WebServer extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger("WebServer");
    private final Finanzberg finanzberg;
    private final CompletableFuture<Void> startFuture = new CompletableFuture<>();

    public WebServer(Finanzberg finanzberg) {
        super("WebServer");
        this.finanzberg = finanzberg;
    }

    @Override
    public void run() {
        try {
            HttpServer server;
            try {
                InetSocketAddress addr = new InetSocketAddress("0.0.0.0", this.finanzberg.getConfig().web.port);
                server = HttpServer.create(addr, 0);
                LOGGER.info("Webserver started on {}:{}", addr.getAddress().getHostAddress(), addr.getPort());
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            LOGGER.info("Creating contexts...");

            server.createContext("/", new RootHandler(this.finanzberg));
            ApiV1.register("/", server, this.finanzberg);

            AtomicInteger threadCount = new AtomicInteger(1);
            server.setExecutor(Executors.newCachedThreadPool(r -> {
                Thread thread = new Thread(r, "WebServer-" + threadCount.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }));

            server.start();
            this.startFuture.complete(null);
        } catch (Throwable throwable) {
            LOGGER.error("Error while starting WebServer", throwable);
            this.startFuture.completeExceptionally(throwable);
        }
    }

    public void startBlocking() {
        this.start();
        try {
            this.startFuture.get();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
