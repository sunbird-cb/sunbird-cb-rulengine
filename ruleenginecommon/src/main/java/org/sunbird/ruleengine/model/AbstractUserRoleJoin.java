package org.sunbird.ruleengine.model;



import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * The persistent class for the USER_ROLE_JOIN database table.
 * 
 */
@MappedSuperclass
public abstract class AbstractUserRoleJoin extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	

	@Column(name="ORG_ID")
	private BigInteger orgId;
	
	@Column(name="CLIENT_ID")
	BigDecimal clientId;
	

	
	public AbstractUserRoleJoin() {
	}

	public BigInteger getOrgId() {
		return this.orgId;
	}

	public void setOrgId(BigInteger orgId) {
		this.orgId = orgId;
	}

	public BigDecimal getClientId() {
		return clientId;
	}

	public void setClientId(BigDecimal clientId) {
		this.clientId = clientId;
	}

	
}