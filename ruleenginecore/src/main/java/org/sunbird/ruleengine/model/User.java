package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sunbird.ruleengine.model.AbstractUser;

@Entity
@Table(name = "USER_")
public class User extends AbstractUser{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="USER_ID_SEQUENCE")
	@SequenceGenerator(name="USER_ID_SEQUENCE", sequenceName="USER_ID_SEQUENCE", allocationSize=1)*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Transient
	private String name;

	@Transient
	private String updatePassword;

	@Column(name = "SALT")
	private String salt;

	@Column(name = "ACTIVE")
	private boolean active;
	
	private boolean searchInactiveAlso=false;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdatePassword() {
		return updatePassword;
	}

	public void setUpdatePassword(String updatePassword) {
		this.updatePassword = updatePassword;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSearchInactiveAlso() {
		return searchInactiveAlso;
	}

	public void setSearchInactiveAlso(boolean searchInactiveAlso) {
		this.searchInactiveAlso = searchInactiveAlso;
	}
	
}
