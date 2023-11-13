package com.hitrust.e2ee.server.service;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hitrust.e2ee.server.ServerEnv;
import com.hitrust.e2ee.server.bean.WSUserBean;
import com.hitrust.e2ee.util.Utility;

@Scope("prototype")
@Component
/* loaded from: LoginService.class */
public class LoginService {
    private static Logger LOG = LogManager.getLogger(LoginService.class);
    private JsonArray trust;

    public LoginService(String filepath) {
        this.trust = null;
        try {
            JsonReader reader = Json.createReader(new StringReader(new String(Utility.readFile(filepath), ServerEnv.DEF_ENCODING)));
            this.trust = reader.readArray();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public int login(WSUserBean bean) {
        LOG.debug(String.format("wsuser[%s]", bean.getWsuser()));
        LOG.debug(String.format("secret[%s]", bean.getSecret()));
        int rtn = 1;
        if (this.trust != null) {
            int i = 0;
            while (true) {
                if (i < this.trust.size()) {
                    JsonObject obj = this.trust.getJsonObject(i);
                    if (!bean.getWsuser().equals(obj.getString("name")) || !bean.getSecret().equals(obj.getString("secret"))) {
                        i++;
                    } else {
                        rtn = 0;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return rtn;
    }
}