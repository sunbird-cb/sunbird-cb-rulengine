package org.sunbird.ruleengine.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * The persistent class for the "ROLE" database table.
 * 
 */
@MappedSuperclass
public abstract class AbstractRole extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public AbstractRole() {
	}

	@Column(name = "ROLE_NAME")
	private String roleName;

	@Column(name = "CLIENT_ID")
	BigDecimal clientId;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public BigDecimal getClientId() {
		return clientId;
	}

	public void setClientId(BigDecimal clientId) {
		this.clientId = clientId;
	}

}