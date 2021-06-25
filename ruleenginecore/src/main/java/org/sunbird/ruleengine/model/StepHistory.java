package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@Entity
@Table(name = "STEP_HISTORY")
public class StepHistory extends AbstractMultiTenantEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "STEP_ID")
	BigInteger stepId;

	/*
	 * @Column(name = "CONFIG_OBJECT_JSON") String configObjectJson;
	 */

	@Column(name = "REQUEST")
	String request;

	@Column(name = "RESPONSE")
	String response;

	@Column(name = "HEADER")
	String header;

	@Column(name = "JOB_ID")
	BigInteger jobId;

	@Column(name = "UNIQUE_KEY_TEMPLATE")
	String uniqueKeyTemplate;

	@Enumerated(EnumType.STRING)
	@Column(name = "STEP_HISTORY_STATUS")
	Status status;

	@Column(name = "JOB_RUN_ID")
	BigInteger jobRunId;

	@Column(name = "SEARCH_CRITERIA")
	String searchCriteria;

	public String getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public BigInteger getId() {
		return id;
	}

	public BigInteger getJobRunId() {
		return jobRunId;
	}

	public void setJobRunId(BigInteger jobRunId) {
		this.jobRunId = jobRunId;
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

	/*
	 * public String getConfigObjectJson() { return configObjectJson; }
	 * 
	 * public void setConfigObjectJson(String configObjectJson) {
	 * this.configObjectJson = configObjectJson; }
	 */

	public enum Status {
		SUCCESS, FAILURE
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public BigInteger getJobId() {
		return jobId;
	}

	public void setJobId(BigInteger jobId) {
		this.jobId = jobId;
	}

	public String getUniqueKeyTemplate() {
		return uniqueKeyTemplate;
	}

	public void setUniqueKeyTemplate(String uniqueKeyTemplate) {
		this.uniqueKeyTemplate = uniqueKeyTemplate;
	}

}
