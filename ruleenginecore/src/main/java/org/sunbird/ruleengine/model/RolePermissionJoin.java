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

import org.sunbird.ruleengine.model.AbstractRolePermissionJoin;


@Entity 
@Table(name="ROLE_PERMISSION_JOIN")
public class RolePermissionJoin extends AbstractRolePermissionJoin{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ROLE_PERMISSION_ID_SEQUENCE")
	@SequenceGenerator(name="ROLE_PERMISSION_ID_SEQUENCE", sequenceName="ROLE_PERMISSION_ID_SEQUENCE", allocationSize=1)*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "ROLE_ID")
	private BigInteger roleId;
	
	@Column(name = "PERMISSION_ID")
	private BigInteger permissionId;
	
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

	public BigInteger getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(BigInteger permissionId) {
		this.permissionId = permissionId;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}
	
	
}
