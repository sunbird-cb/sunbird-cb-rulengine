package org.sunbird.ruleengine.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "JOB_RUN_DETAIL")
public class JobRunDetail extends AbstractMultiTenantEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "JOB_DETAIL_ID")
	BigInteger jobDetailId;
	
	
	@Column(name = "SUCCESS_COUNT")
	BigInteger successCount;
	
	@Column(name = "FAILURE_COUNT")
	BigInteger failureCount;

	@Column(name = "JOB_NAME")
	String jobName;
	
	@Column(name = "FILE_NAME")
	String fileName;

	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date startTime;

	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date endTime;
	
	
	@Transient
	Date startDate;
	
	
	@Transient
	Date endDate;

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

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public BigInteger getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(BigInteger successCount) {
		this.successCount = successCount;
	}

	public BigInteger getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(BigInteger failureCount) {
		this.failureCount = failureCount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	
	
}