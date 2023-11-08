package com.hitrust.e2ee.server.bean;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

/**
 * Encrypted data
 * @author Devin
 *
 */
@Component	
public class VerifyBean {
	@NotNull
	private String data;
	@NotNull
	private String encKey;
	@NotNull
	private String encDBData;
	
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
	public String getEncDBData() {
		return encDBData;
	}
	public void setEncDBData(String encDBData) {
		this.encDBData = encDBData;
	}
	
}
