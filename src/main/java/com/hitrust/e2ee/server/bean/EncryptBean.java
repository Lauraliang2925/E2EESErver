package com.hitrust.e2ee.server.bean;

import org.springframework.stereotype.Component;

/**
 * Encrypted data
 * @author Devin
 *
 */
@Component	
public class EncryptBean{		
	private String data;
	private String encKey;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getEncKey() {
		return encKey;
	}

	public void setEncKey(String encKey) {
		this.encKey = encKey;
	}
	
	

}
