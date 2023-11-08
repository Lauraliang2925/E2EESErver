package com.hitrust.e2ee.server.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hitrust.e2ee.server.bean.EncryptBean;

@Component
@Scope("prototype")
public class EncryptService {
	private static Logger LOG = Logger.getLogger(EncryptService.class);
	
	@Autowired
	E2EEService e2eeService;
	
	public String encrypted = "1";
	
	public int encrypt(EncryptBean bean)
	{
		LOG.debug(String.format("Encrypted[%s]", bean.getData()));
		this.encrypted = e2eeService.encrypt(bean.getData(), bean.getEncKey());
		int result = 0;
		if( StringUtils.isEmpty(this.encrypted) || this.encrypted.length() < 32){
			result = 83;//加密失敗
		}
		return result;
	}


	public String getEncrypted() {
		return encrypted;
	}
}
