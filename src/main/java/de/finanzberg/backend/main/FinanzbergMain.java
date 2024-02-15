package de.finanzberg.backend.main;

import de.finanzberg.backend.Finanzberg;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.io.IoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinanzbergMain {

    private static final Logger LOGGER = LoggerFactory.getLogger("Main");

    static {
        System.setProperty("java.util.logging.manager", org.apache.logging.log4j.jul.LogManager.class.getName());
        System.setProperty("java.awt.headless", "true");

        System.setOut(IoBuilder.forLogger("STDOUT").setLevel(Level.INFO).buildPrintStream());
        System.setErr(IoBuilder.forLogger("STDERR").setLevel(Level.ERROR).buildPrintStream());
    }

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            Thread.currentThread().setName("Startup Thread");

            LOGGER.info("Booting Finanzberg instance...");
            Finanzberg finanzberg = new Finanzberg();
            finanzberg.start(startTime);
        } catch (Throwable throwable) {
            LOGGER.error("Error while booting Finanzberg", throwable);
            LogManager.shutdown();
            System.exit(1);
        }
    }
}
