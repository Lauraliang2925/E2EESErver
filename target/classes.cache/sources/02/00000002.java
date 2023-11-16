package com.hitrust.e2ee.server;

import io.jsonwebtoken.impl.crypto.MacProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* loaded from: ServerEnv.class */
public class ServerEnv {
    private static final String VERSION = "20170821-1500-01";
    public static final String SERVICE = "E2EE";
    public static final String COMPANY = "HiTRUST.COM Inc.";
    private static final String SERVER_NAME = "E2EEServer";
    public static final String COMPUTER_NAME = "E2EEServer";
    private static Logger LOG = LogManager.getLogger(ServerEnv.class);
    public static final Charset DEF_ENCODING = StandardCharsets.UTF_8;
    private static final String DEF_CFG = String.valueOf(File.separator) + "E2EEServer.properties";
    private static Properties props = new Properties();
    public static final Key TOKENKEY = MacProvider.generateKey();

    public static void init(String env) {
        LOG.info("Server Version : 20170821-1500-01");
        try {
            props.putAll(readProperties(String.valueOf(env) + DEF_CFG));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Not load config file", e);
        }
    }

    public static boolean isEmpty() {
        return props.isEmpty();
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static String getString(String key) {
        return props.getProperty(key);
    }

    public static Boolean getBoolean(String key) {
        if (StringUtils.isBlank(props.getProperty(key))) {
            return false;
        }
        return Boolean.valueOf(Boolean.parseBoolean(props.getProperty(key)));
    }

    public static Integer getInt(String key) {
        if (StringUtils.isBlank(props.getProperty(key))) {
            return null;
        }
        return Integer.valueOf(Integer.parseInt(props.getProperty(key)));
    }

    public static String[] getStringArray(String key, String split) {
        if (StringUtils.isBlank(props.getProperty(key))) {
            return null;
        }
        return props.getProperty(key).split(split);
    }

    public static Integer[] getIntArray(String key, String split) {
        if (StringUtils.isBlank(props.getProperty(key))) {
            return null;
        }
        String[] tmp = props.getProperty(key).split(split);
        Integer[] result = new Integer[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            result[i] = Integer.valueOf(Integer.parseInt(tmp[i]));
        }
        return result;
    }

    private static Properties readProperties(String filename) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(loader.getResourceAsStream(filename), DEF_ENCODING);
            properties.load(isr);
            if (isr != null) {
                isr.close();
            }
            return properties;
        } catch (Throwable th) {
            if (isr != null) {
                isr.close();
            }
            throw th;
        }
    }
}