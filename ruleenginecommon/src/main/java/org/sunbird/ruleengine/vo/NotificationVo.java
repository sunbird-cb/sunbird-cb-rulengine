package org.sunbird.ruleengine.vo;

import java.math.BigInteger;

import org.sunbird.ruleengine.model.Notification;

public class NotificationVo extends Notification{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

private BigInteger id;
	
	

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}
}
