package org.sunbird.ruleengine.common.util;

public class JobException extends Exception {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	String message;
	String body;

	public JobException(String message, String body) {
		super();
		this.message = message;
		this.body = body;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
