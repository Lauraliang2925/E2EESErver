package com.hitrust.e2ee.server.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hitrust.e2ee.server.bean.EncryptBean;

@Component
@Scope("prototype")
public class EncryptService {
	private static Logger LOG = LogManager.getLogger(EncryptService.class);
	
	@Autowired
	E2EEService e2eeService;
	
	public String encrypted = CustomBooleanEditor.VALUE_1;
	
	public int encrypt(EncryptBean bean)
	{
		LOG.debug(String.format("Encrypted[%s]", bean.getData()));
		this.encrypted = e2eeService.encrypt(bean.getData(), bean.getEncKey());
		
		return 0;
	}


	public String getEncrypted() {
		return encrypted;
	}
}
