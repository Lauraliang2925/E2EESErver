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

@Component
@Scope("prototype")
public class LoginService {
	private static Logger LOG = LogManager.getLogger(LoginService.class);
	
	private JsonArray trust = null;

	public LoginService(String filepath)
	{
		JsonReader reader = null;
		try {
			reader = Json.createReader(new StringReader(new String(Utility.readFile(filepath), ServerEnv.DEF_ENCODING)));
			trust = reader.readArray();
		} catch (IOException e) {
			LOG.error(e);
		}
		
		
	}
	
	public int login(WSUserBean bean)
	{
		LOG.debug(String.format("wsuser[%s]", bean.getWsuser()));
		LOG.debug(String.format("secret[%s]", bean.getSecret()));
		int rtn = 1;
		if (trust != null)
		{
			for (int i = 0 ; i < trust.size(); i++)
			{
				JsonObject obj = trust.getJsonObject(i);
				if (bean.getWsuser().equals(obj.getString("name")))
				{
					if (bean.getSecret().equals(obj.getString("secret")))
					{
						rtn = 0;
						break;
/*						if (obj.containsKey("ip"))
						{
							request.getLocalAddr()
							
						}*/
					}
				}
			}
		}
		
		return rtn;
	}
}
