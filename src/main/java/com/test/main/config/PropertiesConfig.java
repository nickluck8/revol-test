package com.test.main.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfig {

    public static Properties getProperties(){
        Properties properties = new Properties();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "app.properties";

        try {
            properties.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
