package com.hitrust.e2ee.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.lang.CharEncoding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/* loaded from: StartServer.class */
public class StartServer extends ResourceConfig {
	private static Logger LOG = LogManager.getLogger(StartServer.class);

	public StartServer() throws IOException {
		String[] strArr;
		String[] strArr2;
		String log4jxml = String.valueOf(getWebInfPath()) + File.separator + "classes" + File.separator
				+ Env.getEnvironment() + File.separator + "log4j.xml";
		System.out.println(log4jxml);
		
		LOG.info("Start Server");
		LOG.info("Start Init ServerEnv");
		ServerEnv.init(Env.getEnvironment());
		LOG.info("End Init ServerEnv");
		LOG.info("Start Init classes");
		register(RequestContextFilter.class);
		if (ConfigDefine.INIT_CLASS != null) {
			for (String _class : ConfigDefine.INIT_CLASS) {
				try {
					register(Class.forName(_class));
					LOG.info(String.format("register %s Success", _class));
				} catch (Exception ex) {
					LOG.error(ex.getMessage());
				}
			}
		}
		LOG.info("End Init classes");
		LOG.info("Start Init packages");
		if (ConfigDefine.INIT_PACKAGE != null) {
			for (String _class2 : ConfigDefine.INIT_PACKAGE) {
				try {
					packages(_class2);
					LOG.info(String.format("packages %s Success", _class2));
				} catch (Exception ex2) {
					LOG.error(ex2.getMessage());
				}
			}
		}
		LOG.info("End Init packages");
		LOG.info("Start Server Success");
	}

	private String getWebInfPath() throws UnsupportedEncodingException {
		URL url = StartServer.class.getResource("StartServer.class");
		String className = URLDecoder.decode(url.getFile(), CharEncoding.UTF_8);
		String filePath = className.substring(0, className.indexOf("WEB-INF") + "WEB-INF".length());
		return filePath;
	}
}