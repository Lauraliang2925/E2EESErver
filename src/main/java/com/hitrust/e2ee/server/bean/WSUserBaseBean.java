package com.hitrust.e2ee.server.bean;

import org.springframework.stereotype.Component;

/**
 * OTP object for WS
 * @author Devin
 *
 */
@Component	
public class WSUserBaseBean{
	/**
	 * webservice user id
	 */
	private String wsuser;

	public String getWsuser() {
		return wsuser;
	}

	public void setWsuser(String wsuser) {
		this.wsuser = wsuser;
	}
	
	
}
