package com.hitrust.e2ee.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class StartServer extends ResourceConfig {
	private static Logger LOG = LogManager.getLogger(StartServer.class);

	/**
	 * Register JAX-RS application components.
	 * 
	 * @throws IOException
	 */
	public StartServer() throws IOException {
//		String envPath = getWebInfPath() + File.separator + "classes" + File.separator + "env";
//		String env = new String(Utility.readFile(envPath));
		String log4jxml = getWebInfPath() + File.separator + "classes" + File.separator + Env.getEnvironment()
				+ File.separator + "log4j.xml";
		System.out.println(log4jxml);
//		DOMConfigurator.configure(log4jxml);
		LOG.info("Start Server");
		// ApplicationContext rootCtx = ContextLoader.getCurrentWebApplicationContext();
		// ApplicationContext applicationContext = new
		// ClassPathXmlApplicationContext("applicationContext.xml");
		LOG.info("Start Init ServerEnv");
		ServerEnv.init(Env.getEnvironment());
		LOG.info("End Init ServerEnv");

		LOG.info("Start Init classes");
		register(RequestContextFilter.class);
//		register(SessionRequestFilter.class);
//		register(CustomLoggingFilter.class);

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
			for (String _class : ConfigDefine.INIT_PACKAGE) {
				try {
					packages(_class);
					LOG.info(String.format("packages %s Success", _class));
				} catch (Exception ex) {
					LOG.error(ex.getMessage());
				}
			}
		}
		LOG.info("End Init packages");
		LOG.info("Start Server Success");
	}

	private String getWebInfPath() throws UnsupportedEncodingException {

		String filePath = "";

		URL url = StartServer.class.getResource("StartServer.class");
		String className = url.getFile();
		className = URLDecoder.decode(className, "UTF-8");
		filePath = className.substring(0, className.indexOf("WEB-INF") + "WEB-INF".length());
		return filePath;
	}
}