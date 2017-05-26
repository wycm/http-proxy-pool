package com.wy.hpp.proxy.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 加载配置文件
 */
public class Config {
    private static final Properties properties = new Properties();
    static {
        try {
            properties.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name){
        return properties.getProperty(name);
    }
}
