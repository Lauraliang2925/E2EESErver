package com.hitrust.e2ee.server.service;

import com.hitrust.e2ee.server.bean.VerifyBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
/* loaded from: VerifyService.class */
public class VerifyService {
    private static Logger LOG = LogManager.getLogger(VerifyService.class);
    @Autowired
    E2EEService e2eeService;
    private boolean pass;

    public int verify(VerifyBean bean) {
        this.pass = this.e2eeService.verify(bean.getData(), bean.getEncKey(), bean.getEncDBData()).booleanValue();
        return 0;
    }

    public boolean isPass() {
        return this.pass;
    }
}