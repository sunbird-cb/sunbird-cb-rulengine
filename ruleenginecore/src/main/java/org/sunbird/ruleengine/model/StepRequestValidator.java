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
@Table(name = "STEP_REQUEST_VALIDATOR")
public class StepRequestValidator extends AbstractMultiTenantEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "STEP_ID")
	private BigInteger stepId;
	
	@Column(name = "REQUEST_VALIDATION")
	private String requestValidation;
	
	@Column(name = "SEQUENCE")
	private BigInteger sequence;
	
	@Column(name = "RESULT_FLAG")
	ResultFlag resultFlag;
	
	public enum ResultFlag {
		SUCCESS, FAILURE
	}
	
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getRequestValidation() {
		return requestValidation;
	}

	public void setRequestValidation(String requestValidation) {
		this.requestValidation = requestValidation;
	}

	public BigInteger getSequence() {
		return sequence;
	}

	public void setSequence(BigInteger sequence) {
		this.sequence = sequence;
	}

	public ResultFlag getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(ResultFlag resultFlag) {
		this.resultFlag = resultFlag;
	}

	public BigInteger getStepId() {
		return stepId;
	}

	public void setStepId(BigInteger stepId) {
		this.stepId = stepId;
	}
	
}
