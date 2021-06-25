package org.sunbird.ruleengine.vo;

import java.math.BigInteger;

import org.sunbird.ruleengine.model.User;

public class UserVo extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger id;

	private String PermissionName;

	private boolean searchInactiveAlso=true;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getPermissionName() {
		return PermissionName;
	}

	public void setPermissionName(String permissionName) {
		PermissionName = permissionName;
	}

	public boolean isSearchInactiveAlso() {
		return searchInactiveAlso;
	}

	public void setSearchInactiveAlso(boolean searchInactiveAlso) {
		this.searchInactiveAlso = searchInactiveAlso;
	}

}
