package com.lambdatest.ecommerce.utils;

/**
 * Loads config.properties once and exposes typed getters. Any value can be
 * overridden at runtime via a matching -D system property, e.g.:
 * {@code mvn test -DbaseUrl=https://example.com -Dbrowser=firefox -Dheadless=false}
 */

import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();
    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null)
                throw new IllegalStateException("config.properties file not found on classpath");
            PROPERTIES.load(input);
        }

        catch (IOException e) {
            System.out.println("Failed to load config.properties file " + e.getMessage());
        }
    }

    private ConfigReader() {

    }

    private static String get(String key, String defaultValue) {
        return System.getProperty(key, PROPERTIES.getProperty(key, defaultValue));
    }

    public static String baseURL() {
        return get("baseURL", "https://ecommerce-playground.lambdatest.io/");
    }

    public static String browser() {
        return get("browser", "chromium");
    }

    public static Boolean headless() {
        return Boolean.parseBoolean(get("headless", "true"));
    }

    public static int defaultTimeout() {
        return Integer.parseInt(get("defaultTimeout", "45000"));
    }

    public static Double slowMo() {
        return Double.parseDouble(get("slowMo", "0"));
    }
}
