package com.farmlink.customexception;

public class AuthenticationException extends RuntimeException {
	public AuthenticationException(String errMesg) {
		super(errMesg);
	}
}
