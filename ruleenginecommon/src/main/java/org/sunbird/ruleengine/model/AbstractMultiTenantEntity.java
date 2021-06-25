package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractMultiTenantEntity extends AbstractEntity {
	

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="CLIENT_ID", updatable=false)
	private BigInteger clientId;

	public BigInteger getClientId() {
		return clientId;
	}

	public void setClientId(BigInteger clientId) {
		this.clientId = clientId;
	}
	
	

	
}
