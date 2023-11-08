package com.hitrust.e2ee.server.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hitrust.e2ee.server.bean.VerifyBean;

@Component
@Scope("prototype")
public class VerifyService {
	private static Logger LOG = LogManager.getLogger(VerifyService.class);
	
	@Autowired
	E2EEService e2eeService;
	
	private boolean pass;
	
	public int verify(VerifyBean bean)
	{
		pass = e2eeService.verify(bean.getData(), bean.getEncKey(), bean.getEncDBData());
		return 0;
	}

	public boolean isPass() {
		return pass;
	}
}
