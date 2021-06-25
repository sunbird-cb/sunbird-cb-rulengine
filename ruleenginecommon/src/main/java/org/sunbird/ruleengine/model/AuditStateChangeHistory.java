package org.sunbird.ruleengine.model;



import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="AUDIT_STATE_CHANGE_HISTORY")
public class AuditStateChangeHistory extends AbstractMultiTenantEntity implements Serializable{

	
	
	@Id	
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="AUDIT_STATE_CHA_HIS_SEQ")
	@SequenceGenerator(name="AUDIT_STATE_CHA_HIS_SEQ", sequenceName="AUDIT_STATE_CHA_HIS_SEQ", allocationSize=1)
	private BigInteger id;
	
	
	@Column(name="ORG_ID")
	private BigInteger orgId;
	private static final long serialVersionUID = 1723925210620674919L;

	public AuditStateChangeHistory() {
		super();
	}
	


	public BigInteger getId() {
		return id;
	}



	public void setId(BigInteger id) {
		this.id = id;
	}



	public BigInteger getOrgId() {
		return orgId;
	}



	public void setOrgId(BigInteger orgId) {
		this.orgId = orgId;
	}



	public BigInteger getAuditActionId() {
		return auditActionId;
	}



	public void setAuditActionId(BigInteger auditActionId) {
		this.auditActionId = auditActionId;
	}


	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}


	

	@Column(name = "AUDIT_ACTION_ID")
	private BigInteger auditActionId;
	

	@Column(name = "FIELD_NAME")
	private String fieldName;
	
	@Column(name = "FIELD_VALUE")
	private String fieldValue;
	
}
