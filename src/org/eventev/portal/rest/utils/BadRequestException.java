package org.eventev.portal.rest.utils;

public class BadRequestException extends Exception {

	private static final long serialVersionUID = 6401517794053507579L;
	
	private int errorCode;
	
	public BadRequestException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
