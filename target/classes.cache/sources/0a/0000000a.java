package com.hitrust.e2ee.server.filter;

import com.hitrust.e2ee.server.ServerEnv;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.message.internal.ReaderWriter;

@Priority(1)
/* loaded from: CustomLoggingFilter.class */
public class CustomLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOG = LogManager.getLogger(CustomLoggingFilter.class);

    public void filter(ContainerRequestContext requestContext) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(requestContext.getSecurityContext().getUserPrincipal() == null ? "unknown" : requestContext.getSecurityContext().getUserPrincipal());
        sb.append(" - Path: ").append(requestContext.getUriInfo().getPath());
        sb.append(" - Method: ").append(requestContext.getMethod());
        sb.append(" - Header: ").append(requestContext.getHeaders());
        sb.append(" - Entity: ").append(getEntityBody(requestContext));
        LOG.debug("HTTP REQUEST : " + sb.toString());
        sb.delete(0, sb.length());
    }

    private String getEntityBody(ContainerRequestContext requestContext) {
        InputStream in;
        StringBuilder sb = new StringBuilder();
        try {
            in = requestContext.getEntityStream();
        } catch (IOException e) {
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ReaderWriter.writeTo(in, out);
            byte[] requestEntity = out.toByteArray();
            out.reset();
            out.close();
            if (requestEntity.length == 0) {
                sb.append("").append("\n");
            } else {
                sb.append(new String(requestEntity, ServerEnv.DEF_ENCODING)).append("\n");
            }
            requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
            if (in != null) {
                in.close();
            }
            String rtnString = sb.toString();
            sb.delete(0, sb.length());
            return rtnString;
        } catch (Throwable th) {
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Header: ").append(responseContext.getHeaders());
        sb.append(" - Entity: ").append(responseContext.getEntity());
        LOG.debug("HTTP RESPONSE : " + sb.toString());
        sb.delete(0, sb.length());
    }
}