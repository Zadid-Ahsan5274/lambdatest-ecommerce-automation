package com.lambdatest.ecommerce.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads config.properties once and exposes typed getters. Any value can be
 * overridden at runtime via a matching -D system property, e.g.:
 * {@code mvn test -DbaseUrl=https://example.com -Dbrowser=firefox -Dheadless=false}
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found on classpath (src/test/resources).");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    private static String get(String key, String defaultValue) {
        return System.getProperty(key, PROPERTIES.getProperty(key, defaultValue));
    }

    public static String baseUrl() {
        return get("baseUrl", "https://ecommerce-playground.lambdatest.io");
    }

    public static String browser() {
        return get("browser", "chromium");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "true"));
    }

    public static int defaultTimeout() {
        return Integer.parseInt(get("defaultTimeout", "45000"));
    }

    public static double slowMo() {
        return Double.parseDouble(get("slowMo", "0"));
    }
}