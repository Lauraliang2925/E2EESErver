package com.hitrust.e2ee.server.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import javax.annotation.Priority;
import javax.json.Json;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hitrust.e2ee.server.ConfigDefine;
import com.hitrust.e2ee.server.ServerEnv;
import com.hitrust.e2ee.server.i18n.Resource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

/**
 * Servlet Filter implementation class SessionRequestFilter
 */

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SessionRequestFilter implements ContainerRequestFilter {

	@Autowired
	private Resource res;

	protected final static Logger LOG = LogManager.getLogger(SessionRequestFilter.class);

	public void filter(ContainerRequestContext requestContext) throws IOException {

		String authorizationHeader = requestContext.getHeaderString("Authorization");
		boolean test = true;
//		if (test)
//			return;
		if (requestContext.getUriInfo().getPath().indexOf("handshake") > -1 
				&& authorizationHeader == null) {
			return;
		}

		LOG.debug("authorizationHeader:" + authorizationHeader);

		//int keyType = requestContext.getUriInfo().getPath().indexOf("adm");

		authorizationHeader = authorizationHeader.replaceAll("Bearer ", "");
		String appId = null;

		if ((appId = isValidToken(authorizationHeader)) != null) {
			requestContext.setProperty("AuthorizationHeader", authorizationHeader);
			requestContext.setProperty("JWTID", appId);
		} else {
			requestContext
					.abortWith(
							Response.status(Response.Status.UNAUTHORIZED)
									.entity(Json.createObjectBuilder().add("errorCode", 40)
											.add("errorMsg", res.getString(String.valueOf(40))).build().toString())
									.build());
		}

	}

	private String isValidToken(String authorizationHeader) {
		Claims claims = null;
		try {

			claims = Jwts.parser().setSigningKey(getUsedKey()).parseClaimsJws(authorizationHeader).getBody();

			if ((ConfigDefine.TOKEN_EXPIRATION_ENABLE ? claims.getExpiration().before(new Date()) : false)
					|| !claims.getIssuer().equals(ServerEnv.COMPANY)
					|| !claims.getSubject().equals(ServerEnv.SERVICE)) {
				return null;
			}
		} catch (ExpiredJwtException e) {
			LOG.debug("sessionRequestFilter:ExpiredJwtException", e);
			return null;
		} catch (Exception e) {
			LOG.error("sessionRequestFilter:Jwts.Parser()", e);
			return null;
		}
		return claims.getId();
	}

	private Key getUsedKey() {
		return ServerEnv.TOKENKEY;
	}
}
