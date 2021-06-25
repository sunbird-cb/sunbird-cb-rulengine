package org.sunbird.ruleengine.vo;

import java.math.BigInteger;

import org.sunbird.ruleengine.model.UserRoleJoin;



public class UserRoleJoinVo extends UserRoleJoin{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigInteger id;
	private String roleName;

	private String firstName;
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

}
