package com.hitrust.e2ee.server.ws;

import java.util.Date;

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

import com.hitrust.e2ee.server.ConfigDefine;
import com.hitrust.e2ee.server.ServerEnv;
import com.hitrust.e2ee.server.bean.WSUserBean;
import com.hitrust.e2ee.server.i18n.Resource;
import com.hitrust.e2ee.server.service.LoginService;
import com.hitrust.e2ee.util.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@PermitAll
@Path("login")
@Consumes(MediaType.APPLICATION_JSON+ ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
@RequestScope
public class LoginWS {
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpServletResponse response;

	@Autowired
	HttpSession session;
	
	@Autowired
	private Resource res;
	
	@Autowired
	private LoginService service;

	@POST
	public JsonObject login(WSUserBean bean) {
		String token = "";//(String)request.getAttribute("AuthorizationHeader");
		int result = service.login(bean);
		if (0 == result)
		{
			token  = Jwts.builder()
					  .setId(bean.getWsuser())
					  .setSubject(ServerEnv.SERVICE)
					  .setAudience("user")						  
					  .setIssuer(ServerEnv.COMPANY)
					  .setIssuedAt(new Date())
					  .setExpiration(Utility.getDate(null
							  ,null
							  ,null
							  ,ConfigDefine.TOKEN_EXPIRATION[0]
							  ,ConfigDefine.TOKEN_EXPIRATION[1]
							  ,ConfigDefine.TOKEN_EXPIRATION[2]))
					  .signWith(SignatureAlgorithm.HS512, ServerEnv.TOKENKEY)
					  .compact();
		}
		else
			token = "";
		Integer expiration = ConfigDefine.TOKEN_EXPIRATION[0] * 3600 + ConfigDefine.TOKEN_EXPIRATION[1] * 60 + ConfigDefine.TOKEN_EXPIRATION[2];
    	return Json.createObjectBuilder().add("errorCode", result).add("errorMsg", res.getString(result)).add("access_token",token ).add("expires_in", expiration).add("token_type", "Bearer").build();

	}
}