package com.hitrust.e2ee.server;

public class ConfigDefine {
	public final static String ENV = ServerEnv.getString("enviroment");
//	public final static String ENCODING = ServerEnv.getString("encoding");
	public final static String TIMEZONE = ServerEnv.getString("timezone");
	public final static String[] INIT_CLASS = ServerEnv.getStringArray("init.class", ",");
	public final static String[] INIT_PACKAGE = ServerEnv.getStringArray("init.package", ",");
	public final static Boolean TOKEN_EXPIRATION_ENABLE = ServerEnv.getBoolean("token.expiration.enable");
	public final static Integer[] TOKEN_EXPIRATION = ServerEnv.getIntArray("token.expiration"," ");
}
