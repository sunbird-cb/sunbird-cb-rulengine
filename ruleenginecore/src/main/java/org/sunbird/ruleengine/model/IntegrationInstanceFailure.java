package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@Entity
@Table(name = "INTEGRATION_INSTANCE_FAILURE")
public class IntegrationInstanceFailure extends AbstractMultiTenantEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "STEP_ID")
	BigInteger stepId;

	@Column(name = "JOB_DETAIL_ID")
	BigInteger jobDetailId;

	@Column(name = "NEXT_REQUEST_JSON")
	String nextRequestJson;

	/*@Column(name = "CONFIG_OBJECT_JSON")
	String configObjectJson;*/

	@Column(name = "ERROR_RESPONSE")
	String errorResponse;

	@Column(name = "NO_OF_RETRIES")
	int noOfRetries = 0;

	@Column(name = "DONE")
	boolean done;
	
	@Column(name = "JOB_RUN_ID")
	BigInteger jobRunId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="JOB_RUN_ID", referencedColumnName = "ID", insertable = false, updatable = false)
	JobRunDetail jobRunDetail;

	 
	@Transient
	Object configObjectJsonObject;
	
	@Column(name = "ERROR_CODE")
	String errorCode;
	
	@Column(name = "ERROR_PATTERN_ID")
	BigInteger errorPatternId;

	public String getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(String errorResponse) {
		this.errorResponse = errorResponse;
	}
	
	public BigInteger getJobRunId() {
		return jobRunId;
	}

	public void setJobRunId(BigInteger jobRunId) {
		this.jobRunId = jobRunId;
	}

	@Override
	public BigInteger getId() {
		return id;
	}

	@Override
	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getStepId() {
		return stepId;
	}

	public void setStepId(BigInteger stepId) {
		this.stepId = stepId;
	}

	/*public String getConfigObjectJson() {
		return configObjectJson;
	}

	public void setConfigObjectJson(String configObjectJson) {
		this.configObjectJson = configObjectJson;
	}*/

	public String getNextRequestJson() {
		return nextRequestJson;
	}

	public void setNextRequestJson(String nextRequestJson) {
		this.nextRequestJson = nextRequestJson;
	}

	public int getNoOfRetries() {
		return noOfRetries;
	}

	public void setNoOfRetries(int noOfRetries) {
		this.noOfRetries = noOfRetries;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public BigInteger getJobDetailId() {
		return jobDetailId;
	}

	public void setJobDetailId(BigInteger jobDetailId) {
		this.jobDetailId = jobDetailId;
	}

	public Object getConfigObjectJsonObject() {
		return configObjectJsonObject;
	}

	public void setConfigObjectJsonObject(Object configObjectJsonObject) {
		this.configObjectJsonObject = configObjectJsonObject;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public BigInteger getErrorPatternId() {
		return errorPatternId;
	}

	public void setErrorPatternId(BigInteger errorPatternId) {
		this.errorPatternId = errorPatternId;
	}
	
	
}
