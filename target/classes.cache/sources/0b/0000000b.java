package com.hitrust.e2ee.server.filter;

import com.hitrust.e2ee.server.ConfigDefine;
import com.hitrust.e2ee.server.ServerEnv;
import com.hitrust.e2ee.server.i18n.Resource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import javax.annotation.Priority;
import javax.json.Json;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Provider
@Priority(1000)
/* loaded from: SessionRequestFilter.class */
public class SessionRequestFilter implements ContainerRequestFilter {
    @Autowired
    private Resource res;
    protected static final Logger LOG = LogManager.getLogger(SessionRequestFilter.class);

    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString("Authorization");
        if (requestContext.getUriInfo().getPath().indexOf("handshake") > -1 && authorizationHeader == null) {
            return;
        }
        LOG.debug("authorizationHeader:" + authorizationHeader);
        String authorizationHeader2 = authorizationHeader.replaceAll("Bearer ", "");
        String appId = isValidToken(authorizationHeader2);
        if (appId != null) {
            requestContext.setProperty("AuthorizationHeader", authorizationHeader2);
            requestContext.setProperty("JWTID", appId);
            return;
        }
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(Json.createObjectBuilder().add("errorCode", 40).add("errorMsg", this.res.getString(String.valueOf(40))).build().toString()).build());
    }

    private String isValidToken(String authorizationHeader) {
        try {
            Claims claims = (Claims) Jwts.parser().setSigningKey(getUsedKey()).parseClaimsJws(authorizationHeader).getBody();
            if ((!ConfigDefine.TOKEN_EXPIRATION_ENABLE.booleanValue() || !claims.getExpiration().before(new Date())) && claims.getIssuer().equals(ServerEnv.COMPANY)) {
                if (!claims.getSubject().equals(ServerEnv.SERVICE)) {
                    return null;
                }
                return claims.getId();
            }
            return null;
        } catch (ExpiredJwtException e) {
            LOG.debug("sessionRequestFilter:ExpiredJwtException", e);
            return null;
        } catch (Exception e2) {
            LOG.error("sessionRequestFilter:Jwts.Parser()", e2);
            return null;
        }
    }

    private Key getUsedKey() {
        return ServerEnv.TOKENKEY;
    }
}