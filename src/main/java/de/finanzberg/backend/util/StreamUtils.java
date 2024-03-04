package de.finanzberg.backend.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import de.finanzberg.backend.Finanzberg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public class StreamUtils {

    private static final Logger LOGGER = LogManager.getLogger("StreamUtils");

    public static String readFully(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Throwable throwable) {
            LOGGER.error("Failed to read fully", throwable);
            return null;
        }
    }

    public static void writeFully(String string, OutputStream os) {
        try (OutputStreamWriter writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            writer.write(string);
        } catch (Throwable throwable) {
            LOGGER.error("Failed to write fully", throwable);
        }
    }

    public static void write(byte[] bytes, OutputStream os) {
        try {
            os.write(bytes);
        } catch (Throwable throwable) {
            LOGGER.error("Failed to write", throwable);
        }
    }

    public static int bytes(String string) {
        return string.getBytes(StandardCharsets.UTF_8).length;
    }

    public static JsonObject readJsonFully(InputStream is) {
        return Finanzberg.GSON.fromJson(readFully(is), JsonObject.class);
    }

    public static void writeJsonFully(JsonElement json, OutputStream os) {
        writeFully(Finanzberg.GSON.toJson(json), os);
    }

    public static void writeSaveJson(JsonElement json, HttpExchange exchange, int status) throws Throwable {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] bytes = Finanzberg.GSON.toJson(json).getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        write(bytes, exchange.getResponseBody());
    }

    public static byte[] compressGzip(byte[] data) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(data);
            return bos.toByteArray();
        } catch (Throwable throwable) {
            LOGGER.error("Failed to compress gzip", throwable);
            return null;
        }
    }
}