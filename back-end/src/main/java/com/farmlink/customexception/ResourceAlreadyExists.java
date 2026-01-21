package com.farmlink.customexception;

public class ResourceAlreadyExists extends RuntimeException {
	public ResourceAlreadyExists(String errMesg) {
		super(errMesg);
	}
}