package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractOrgnizationableEntity extends AbstractMultiTenantEntity {

	@Column(name = "ORG_ID", updatable=false)
	private BigInteger orgId;

	public BigInteger getOrgId() {
		return orgId;
	}

	public void setOrgId(BigInteger orgId) {
		this.orgId = orgId;
	}

}
