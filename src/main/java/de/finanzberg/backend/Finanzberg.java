package de.finanzberg.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.finanzberg.backend.config.ConfigLoader;
import de.finanzberg.backend.config.FinanzbergConfig;
import de.finanzberg.backend.db.DBManager;
import de.finanzberg.backend.logic.DataLogic;
import de.finanzberg.backend.logic.UserLogic;
import de.finanzberg.backend.logic.parser.ParserRegistry;
import de.finanzberg.backend.rest.WebServer;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.text.DecimalFormat;

public class Finanzberg {

    public static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private static final Logger LOGGER = LoggerFactory.getLogger("Finanzberg");
    private static final Path CONFIG_PATH = Path.of("config.yml");
    private FinanzbergConfig config;
    private DBManager dbManager;
    private ParserRegistry parsers;
    private UserLogic userLogic;
    private DataLogic dataLogic;
    private boolean running = true;

    public void start(long startTime) {
        try {
            LOGGER.info("Loading Finanzberg configuration...");
            this.config = ConfigLoader.loadYamlObject(CONFIG_PATH, FinanzbergConfig.class);

            LOGGER.info("Loading DBManager...");
            dbManager = new DBManager(this);

            LOGGER.info("Loading Parsers");
            parsers = new ParserRegistry(this);

            LOGGER.info("Loading UserLogic...");
            userLogic = new UserLogic(this);

            LOGGER.info("Loading DataLogic...");
            dataLogic = new DataLogic(this);

            LOGGER.info("Starting Webserver...");
            new WebServer(this).startBlocking();

            LOGGER.info("Starting console...");
            new FinanzbergConsole(this).startThread();

            double bootTime = (System.currentTimeMillis() - startTime) / 1000D;
            LOGGER.info("Done ({}s)! To stop it, type \"stop\"", new DecimalFormat("#.##").format(bootTime));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> this.stop(true), "Shutdown Thread"));
        } catch (Throwable throwable) {
            LOGGER.error("Error while booting Finanzberg", throwable);
            stop(false);
        }
    }

    public void stop(boolean cleanExit) {
        if (!this.running) return;
        this.running = false;

        LOGGER.info("Stopping Finanzberg...");

        LOGGER.info("Shutting down logger and system! Goodbye (-_-) . z Z");
        LogManager.shutdown();

        System.exit(cleanExit ? 0 : 1);
    }

    public FinanzbergConfig getConfig() {
        return this.config;
    }

    public DBManager getDBManager() {
        return dbManager;
    }

    public ParserRegistry getParsers() {
        return parsers;
    }

    public UserLogic getUserLogic() {
        return userLogic;
    }

    public DataLogic getDataLogic() {
        return dataLogic;
    }

    public boolean isRunning() {
        return this.running;
    }
}
