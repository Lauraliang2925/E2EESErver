package com.hitrust.e2ee.server;

public class Env {
	private static String environment;

	public Env(String env) {
		environment = env;
	}

	public static String getEnvironment() {
		return environment;
	}
}