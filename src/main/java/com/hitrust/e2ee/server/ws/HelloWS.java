package com.hitrust.e2ee.server.ws;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;


@PermitAll
@Path("/hello")
public class HelloWS {
	@Context
	HttpServletRequest request;

	@GET
	public String sayHelloWorld() {
		return "Hello world, this is 中文";
	}

	@POST
	public String postHello() {
		String out = "hello world, this is 中文";
		return out;
	}
	
	@PUT
	public String putHello() {
		String out = "hello world, this is 中文";
		return out;
	}
}