package com.hitrust.e2ee.server.ws;

import com.hitrust.e2ee.server.ConfigDefine;
import com.hitrust.e2ee.server.ServerEnv;
import com.hitrust.e2ee.server.bean.HandShakeBean;
import com.hitrust.e2ee.server.i18n.Resource;
import com.hitrust.e2ee.util.Utility;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.annotation.security.PermitAll;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

@PermitAll
@Path("handshake")
@Consumes({"application/json;charset=utf-8"})
@Produces({"application/json;charset=utf-8"})
@RequestScope
public class HandShakeWS {
	@Autowired
	private Resource res;

	@POST
	public JsonObject handshake(HandShakeBean bean) {
		byte result = 7;
		String token = "";
		if (bean.getWsUser() != null) {
			result = 0;
			token = Jwts.builder().setId(bean.getWsUser()).setSubject("E2EE").setAudience("user")
					.setIssuer("HiTRUST.COM Inc.").setIssuedAt(new Date())
					.setExpiration(Utility.getDate((Integer) null, (Integer) null, (Integer) null,
							ConfigDefine.TOKEN_EXPIRATION[0], ConfigDefine.TOKEN_EXPIRATION[1],
							ConfigDefine.TOKEN_EXPIRATION[2]))
					.signWith(SignatureAlgorithm.HS512, ServerEnv.TOKENKEY).compact();
		}

		Integer expiration = Integer.valueOf(ConfigDefine.TOKEN_EXPIRATION[0].intValue() * 3600
				+ ConfigDefine.TOKEN_EXPIRATION[1].intValue() * 60 + ConfigDefine.TOKEN_EXPIRATION[2].intValue());
		return Json.createObjectBuilder().add("errorCode", result)
				.add("errorMsg", this.res.getString(Integer.valueOf(result))).add("access_token", token)
				.add("expires_in", expiration.intValue()).add("token_type", "Bearer").build();
	}
}