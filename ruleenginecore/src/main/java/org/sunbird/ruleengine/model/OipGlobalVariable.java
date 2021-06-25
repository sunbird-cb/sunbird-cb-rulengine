package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@Entity
@Table(name = "OIP_GLOBAL_VARIABLES")
public class OipGlobalVariable extends AbstractMultiTenantEntity{
	
	public static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	BigInteger id;
	
    @Column(name = "JOB_DETAIL_ID")
	BigInteger jobDetailId;
	
	@Column(name = "GLOBAL_VARIABLE_NAME")
	String globalVariableName;
	
	@Column(name = "GLOBAL_VARIABLE_VALUE")
	String globalVariableValue;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getJobDetailId() {
		return jobDetailId;
	}

	public void setJobDetailId(BigInteger jobDetailId) {
		this.jobDetailId = jobDetailId;
	}

	public String getGlobalVariableName() {
		return globalVariableName;
	}

	public void setGlobalVariableName(String globalVariableName) {
		this.globalVariableName = globalVariableName;
	}

	public String getGlobalVariableValue() {
		return globalVariableValue;
	}

	public void setGlobalVariableValue(String globalVariableValue) {
		this.globalVariableValue = globalVariableValue;
	}

	
	

}
