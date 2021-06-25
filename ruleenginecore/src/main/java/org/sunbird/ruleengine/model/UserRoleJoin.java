package org.sunbird.ruleengine.model;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sunbird.ruleengine.model.AbstractUserRoleJoin;



@Entity
@Table(name="USER_ROLE_JOIN")
public class UserRoleJoin extends AbstractUserRoleJoin{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="USER_ROLE_JOIN_ID_SEQUENCE")
	@SequenceGenerator(name="USER_ROLE_JOIN_ID_SEQUENCE", sequenceName="USER_ROLE_JOIN_ID_SEQUENCE", allocationSize=1)*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "ROLE_ID")
	private BigInteger roleId;

	@Column(name = "USER_ID")
	private BigInteger userId;


	@javax.persistence.Transient
	List<BigInteger> list =new ArrayList<>();



	public List<BigInteger> getList() {
		return list;
	}

	public void setList(List<BigInteger> list) {
		this.list = list;
	}

	public BigInteger getRoleId() {
		return roleId;
	}

	public void setRoleId(BigInteger roleId) {
		this.roleId = roleId;
	}

	public BigInteger getUserId() {
		return userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}




}
