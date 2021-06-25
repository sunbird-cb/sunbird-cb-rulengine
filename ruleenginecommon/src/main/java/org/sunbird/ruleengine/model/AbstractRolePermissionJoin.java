package org.sunbird.ruleengine.model;



import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * The persistent class for the ROLE_PERMISSION_JOIN database table.
 * 
 */
@MappedSuperclass
public abstract class AbstractRolePermissionJoin extends AbstractEntity {
	private static final long serialVersionUID = 1L;





	@Column(name="ORG_ID")
	private BigDecimal orgId;
	

	@Column(name="CLIENT_ID")
	BigDecimal clientId;

	

	public AbstractRolePermissionJoin() {
	}


	
	public BigDecimal getOrgId() {
		return this.orgId;
	}

	public void setOrgId(BigDecimal orgId) {
		this.orgId = orgId;
	}


	public BigDecimal getClientId() {
		return clientId;
	}


	public void setClientId(BigDecimal clientId) {
		this.clientId = clientId;
	}


}