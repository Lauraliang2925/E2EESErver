package com.hitrust.e2ee.server.bean;

import org.springframework.stereotype.Component;

@Component
public class HandShakeBean {
	private String wsUser;

	public String getWsUser() {
		return this.wsUser;
	}

	public void setWsUser(String wsUser) {
		this.wsUser = wsUser;
	}
}