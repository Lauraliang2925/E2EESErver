package com.hitrust.e2ee.server.i18n;

import com.hitrust.e2ee.server.Env;
import com.hitrust.e2ee.server.ServerEnv;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("singleton")
@Component
/* loaded from: Resource.class */
public class Resource {
    protected static final Logger LOG = LogManager.getLogger(Resource.class);
    private Properties props = new Properties();
    @Autowired
    private Env env;

    public Resource() {
        try {
            String path = String.valueOf(Env.getEnvironment()) + File.separator + "i18n" + File.separator + "messages.properties";
            LOG.debug(String.format("path[%s]", path));
            this.props.putAll(readProperties(path));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Not load config file", e);
        }
    }

    public boolean isEmpty() {
        return this.props.isEmpty();
    }

    public String getProperty(String key) {
        return this.props.getProperty(key);
    }

    public String getString(String key) {
        return this.props.getProperty(key);
    }

    public String getString(Integer key) {
        return this.props.getProperty(String.valueOf(key));
    }

    public Boolean getBoolean(String key) {
        if (StringUtils.isBlank(this.props.getProperty(key))) {
            return false;
        }
        return Boolean.valueOf(Boolean.parseBoolean(this.props.getProperty(key)));
    }

    public Integer getInt(String key) {
        if (StringUtils.isBlank(this.props.getProperty(key))) {
            return null;
        }
        return Integer.valueOf(Integer.parseInt(this.props.getProperty(key)));
    }

    public String[] getStringArray(String key) {
        if (StringUtils.isBlank(this.props.getProperty(key))) {
            return null;
        }
        return this.props.getProperty(key).split(",");
    }

    private Properties readProperties(String filename) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        LOG.debug(String.format("ServerEnv.DEF_ENCODING[%s]", ServerEnv.DEF_ENCODING));
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(loader.getResourceAsStream(filename), ServerEnv.DEF_ENCODING);
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