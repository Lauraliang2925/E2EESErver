package com.hitrust.e2ee.server.bean;

import org.springframework.stereotype.Component;

/**
 * OTP object for WS
 * @author Devin
 *
 */
@Component	
public class WSUserBean extends WSUserBaseBean{		
	private String secret;
	
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
}
