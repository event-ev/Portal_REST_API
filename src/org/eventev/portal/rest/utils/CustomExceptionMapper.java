package org.eventev.portal.rest.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eventev.portal.rest.utils.exceptions.BadRequestException;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

	@Context
	private HttpHeaders headers;

	public Response toResponse(Exception e) {
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(new MyException(e))
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	public Response toResponse(BadRequestException e) {
		return Response.status(Status.BAD_REQUEST)
				.entity(new MyException(e))
				.type(MediaType.APPLICATION_JSON).build();
	}

}

class MyException {

	public String errorCode, exceptionType, message;

	public MyException() {

	}

	public MyException(Exception e) {
		e.printStackTrace();
		this.exceptionType = e.getClass().getCanonicalName();
		this.message = e.getMessage();
		
		StringWriter sw = new StringWriter();
		PrintWriter ps = new PrintWriter(sw);
		e.printStackTrace(ps);
		sw.toString();
		this.errorCode = "NULL";
		// TODO: Write Stacktrace database and set code
	}
	
	public MyException(BadRequestException e) {
		this.errorCode = Integer.valueOf(e.getErrorCode()).toString();
		this.exceptionType = e.getClass().getCanonicalName();
		this.message = e.getMessage();
	}

}