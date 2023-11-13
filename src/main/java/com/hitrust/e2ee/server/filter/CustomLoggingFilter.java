package com.hitrust.e2ee.server.filter;

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

import com.hitrust.e2ee.server.ServerEnv;

@Priority(1)
/* loaded from: CustomLoggingFilter.class */
public class CustomLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOG = LogManager.getLogger(CustomLoggingFilter.class);

    @Override // javax.ws.rs.container.ContainerRequestFilter
    public void filter(ContainerRequestContext requestContext) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(requestContext.getSecurityContext().getUserPrincipal() == null ? "unknown" : requestContext.getSecurityContext().getUserPrincipal());
        sb.append(" - Path: ").append(requestContext.getUriInfo().getPath());
        sb.append(" - Method: ").append(requestContext.getMethod());
        sb.append(" - Header: ").append(requestContext.getHeaders());
        sb.append(" - Entity: ").append(getEntityBody(requestContext));
        LOG.debug("HTTP REQUEST : " + sb.toString());
    }

    private String getEntityBody(ContainerRequestContext requestContext) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = requestContext.getEntityStream();
        StringBuilder b = new StringBuilder();
        try {
            ReaderWriter.writeTo(in, out);
            byte[] requestEntity = out.toByteArray();
            if (requestEntity.length == 0) {
                b.append("").append("\n");
            } else {
                b.append(new String(requestEntity, ServerEnv.DEF_ENCODING)).append("\n");
            }
            requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
        } catch (IOException e) {
        }
        return b.toString();
    }

    @Override // javax.ws.rs.container.ContainerResponseFilter
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Header: ").append(responseContext.getHeaders());
        sb.append(" - Entity: ").append(responseContext.getEntity());
        LOG.debug("HTTP RESPONSE : " + sb.toString());
    }
}