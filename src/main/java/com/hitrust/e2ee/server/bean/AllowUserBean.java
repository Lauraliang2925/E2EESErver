package com.hitrust.e2ee.server.bean;

import java.util.ArrayList;
import java.util.List;

public class AllowUserBean extends WSUserBean{
	private String role = null;
	private String desc = null;
	private List<String> ip = new ArrayList<String>();
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<String> getIp() {
		return ip;
	}
	public void setIp(List<String> ip) {
		this.ip = ip;
	}
	
	
}
