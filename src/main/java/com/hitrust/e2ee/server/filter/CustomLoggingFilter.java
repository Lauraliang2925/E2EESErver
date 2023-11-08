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
public class CustomLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
	private final static Logger LOG = LogManager.getLogger(CustomLoggingFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("User: ").append(requestContext.getSecurityContext().getUserPrincipal() == null ? "unknown"
				: requestContext.getSecurityContext().getUserPrincipal());
		sb.append(" - Path: ").append(requestContext.getUriInfo().getPath());
//        sb.append(" - Path: ").append(requestContext.getUriInfo().getRequestUri());
		sb.append(" - Method: ").append(requestContext.getMethod());
		sb.append(" - Header: ").append(requestContext.getHeaders());
		sb.append(" - Entity: ").append(getEntityBody(requestContext));
		LOG.debug("HTTP REQUEST : " + sb.toString());
		sb.delete(0, sb.length());
	}

	private String getEntityBody(ContainerRequestContext requestContext) {
		final StringBuilder sb = new StringBuilder();

		try (InputStream in = requestContext.getEntityStream();) {
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
		} catch (IOException ex) {
			// Handle logging error
		}

		String rtnString = sb.toString();
		sb.delete(0, sb.length());

		return rtnString;
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Header: ").append(responseContext.getHeaders());
		sb.append(" - Entity: ").append(responseContext.getEntity());
		LOG.debug("HTTP RESPONSE : " + sb.toString());
		sb.delete(0, sb.length());
	}
}