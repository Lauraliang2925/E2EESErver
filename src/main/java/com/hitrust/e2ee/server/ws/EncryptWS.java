package com.hitrust.e2ee.server.ws;

import javax.annotation.security.PermitAll;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.hitrust.e2ee.server.bean.EncryptBean;
import com.hitrust.e2ee.server.i18n.Resource;
import com.hitrust.e2ee.server.service.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;


@PermitAll
@Path("data")
@Consumes(MediaType.APPLICATION_JSON+ ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
@RequestScope
public class EncryptWS {
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpServletResponse response;

	@Autowired
	HttpSession session;
	
	@Autowired
	private Resource res;

	@Autowired
	private EncryptService service;

	
	@POST
	@Path("encrypt")
	public JsonObject encrypt(EncryptBean bean) {
		
		int result = 7;//參數不正確
		if( bean.getEncKey() != null && bean.getData() != null ){
			result = service.encrypt(bean);
		}
		
		
    	return Json.createObjectBuilder()
    			.add("errorCode", result)
    			.add("errorMsg", res.getString(result))
    			.add("encrypted", service.getEncrypted())
    			.build();

	}
	
}