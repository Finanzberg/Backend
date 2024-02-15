package de.finanzberg.backend.main;

import org.apache.logging.log4j.Level;
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

    }
}
