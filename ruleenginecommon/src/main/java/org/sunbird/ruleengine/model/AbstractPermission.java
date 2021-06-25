package org.sunbird.ruleengine.model;



import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * The persistent class for the PERMISSION database table.
 * 
 */
@MappedSuperclass
public abstract class AbstractPermission extends AbstractEntity {
	private static final long serialVersionUID = 1L;



	@Column(name="PERMISSION_NAME")
	private String permissionName;
	
	@Column(name="FUNCTION_NAME")
	private String functionName;

	
	@Column(name="CLIENT_ID")
	BigDecimal clientId;
	

	public String getPermissionName() {
		return this.permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public BigDecimal getClientId() {
		return clientId;
	}

	public void setClientId(BigDecimal clientId) {
		this.clientId = clientId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}




}