package com.automation.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static ThreadLocal<Properties> prop = ThreadLocal.withInitial(Properties::new);;

    public static void loadConfig(boolean isCI) {
        prop.get().clear();

        loadFile("common.properties");
        loadFile("pim.properties");
        loadFile("performance.properties");
        loadFile("recruitment.properties");
        if (isCI) {
            loadFile("ci.properties");
        }
    }

    private static void loadFile(String path) {
        try {
            InputStream inp = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream(path);

            if (inp == null) {
                throw new RuntimeException("Cannot find config file: " + path);
            }

            prop.get().load(inp);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + path, e);
        }
    }

    public static String getProperty(String key) {
        return prop.get().getProperty(key);
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(prop.get().getProperty(key));
    }
}
