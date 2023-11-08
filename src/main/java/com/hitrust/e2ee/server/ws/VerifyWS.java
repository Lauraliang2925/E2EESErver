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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

import com.hitrust.e2ee.server.bean.VerifyBean;
import com.hitrust.e2ee.server.i18n.Resource;
import com.hitrust.e2ee.server.service.VerifyService;

@PermitAll
@Path("data")
@Consumes(MediaType.APPLICATION_JSON+ ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
@RequestScope
public class VerifyWS {
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpServletResponse response;

	@Autowired
	HttpSession session;
	
	@Autowired
	private Resource res;

	@Autowired
	private VerifyService service;
	
	@POST
	@Path("verify")
	public JsonObject verify(VerifyBean bean) {
		int result = 7;//參數不正確
		if( bean.getEncKey() != null && bean.getData() != null && 
				bean.getEncDBData() != null){
			result = service.verify(bean);
		}
				
    	return Json.createObjectBuilder()
    			.add("errorCode", result)
    			.add("errorMsg", res.getString(String.valueOf(result)))
    			.add("pass", service.isPass() )
    			.build();

	}
	
}