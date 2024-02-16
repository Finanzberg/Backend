package de.finanzberg.backend;

import de.finanzberg.backend.config.ConfigLoader;
import de.finanzberg.backend.config.FinanzbergConfig;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.text.DecimalFormat;

public class Finanzberg {

    private static final Logger LOGGER = LoggerFactory.getLogger("Finanzberg");
    private static final Path CONFIG_PATH = Path.of("config.yml");
    private FinanzbergConfig config;

    public void start(long startTime) {
        try {
            LOGGER.info("Loading Finanzberg configuration...");
            this.config = ConfigLoader.loadYamlObject(CONFIG_PATH, FinanzbergConfig.class);

            double bootTime = (System.currentTimeMillis() - startTime) / 1000D;
            LOGGER.info("Done ({}s)! To stop it, type \"stop\"", new DecimalFormat("#.##").format(bootTime));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> this.stop(true), "Shutdown Thread"));
        } catch (Throwable throwable) {
            LOGGER.error("Error while booting Finanzberg", throwable);
            stop(false);
        }
    }

    public void stop(boolean cleanExit) {
        LOGGER.info("Stopping Finanzberg...");

        LOGGER.info("Shutting down logger and system! Goodbye (-_-) . z Z");
        LogManager.shutdown();

        System.exit(cleanExit ? 0 : 1);
    }

    public FinanzbergConfig getConfig() {
        return config;
    }
}
