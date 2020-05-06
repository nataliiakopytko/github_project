package com.github.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
    public static final String pathToGithubPropertyFile = "src/test/resources/properties/github.properties";
    public static final String pathToTestrailPropertyFile = "src/test/resources/properties/testrail.properties";
    private static final String pathToBrowserPropertyFile = "src/test/resources/properties/browser.properties";
    private static final String pathToApplicationPropertyFile = "src/test/resources/properties/application.properties";

    public static String getBaseGithubUrl() {
        return getProperty(pathToGithubPropertyFile, "baseUrl");
    }

    public static String getProperty(String pathToPropertyFile, String key) {
        return getValuePipeline(pathToPropertyFile, key);
    }

    public static Properties getPropertyFile(String pathToPropertyFile) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(pathToPropertyFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static String getPropertyValueFromFile(String pathToPropertyFile, String key) {
        return getPropertyFile(pathToPropertyFile).getProperty(key);
    }

    private static String getValuePipeline(String pathToPropertyFile, String key) {
        String localProperty = getPropertyValueFromFile(pathToPropertyFile, key);
        if (localProperty != null) {
            return localProperty;
        }
        return "Property Value is not defined";
    }
}