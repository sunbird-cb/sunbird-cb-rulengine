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
import javax.persistence.Version;
import javax.validation.constraints.Pattern;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

/**
 * The persistent class for the JOB_DETAIL database table.
 * 
 */
@Entity
@Table(name = "JOB_DETAIL")
public class JobDetail extends AbstractMultiTenantEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	/*
	 * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	 * "JOB_DETAIL_SEQ")
	 * 
	 * @SequenceGenerator(name = "JOB_DETAIL_SEQ", sequenceName =
	 * "JOB_DETAIL_SEQ", allocationSize = 1)
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "JOB_NAME")
	String jobName;

	@Column(name = "LAST_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastStartTime;
	
	@Column(name = "LAST_SCHEDULED_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastScheduledStartTime;


	@Column(name = "LAST_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastEndTime;

	@Column(name = "NEXT_RUN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date nextRunTime;

	@Column(name = "RETRY_ENABLED")
	boolean retryEnabled;

	@Version
	Integer version;

	@Column(name = "INTERVAL_TYPE")
	IntervalType intervalType = IntervalType.INTERVAL_AFTER_LAST_FINISH;

	@Column(name = "TIME_INTERVAL_TYPE")
	TimeIntervalType timeIntervalType = TimeIntervalType.RELATIVE;

	@Column(name = "JOB_INTERVAL")
	Integer interval;

	@Transient
	Date toDateTime;

	@Column(name = "JOB_STATUS")
	Status status;

	@Column(name = "JOB_CODE")
	@Pattern(regexp = "[a-zA-Z_]+", message = "Please enter valid job code")
	String jobCode;


	@Column(name = "AGENT_JOB")
	Boolean agentJob=Boolean.FALSE;
	
	@Column(name = "CLIENT_ID")
	private BigInteger clientId;
	
	public enum Status {
		STOPPED, RUNNING
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(Date lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	public Date getLastEndTime() {
		return lastEndTime;
	}

	public void setLastEndTime(Date lastEndTime) {
		this.lastEndTime = lastEndTime;
	}

	public Date getNextRunTime() {
		return nextRunTime;
	}

	public void setNextRunTime(Date nextRunTime) {
		this.nextRunTime = nextRunTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(Date toDateTime) {
		this.toDateTime = toDateTime;
	}

	@Override
	public BigInteger getId() {
		return id;
	}

	@Override
	public void setId(BigInteger id) {
		this.id = id;
	}

	public IntervalType getIntervalType() {
		return intervalType;
	}

	public void setIntervalType(IntervalType intervalType) {
		this.intervalType = intervalType;
	}
	
	public enum TimeIntervalType {
		MONTHLY,RELATIVE 
	}

	public enum IntervalType {
		INTERVAL_AFTER_LAST_FINISH, INTERVAL_AFTER_LAST_START
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isRetryEnabled() {
		return retryEnabled;
	}

	public void setRetryEnabled(boolean retryEnabled) {
		this.retryEnabled = retryEnabled;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public Date getLastScheduledStartTime() {
		return lastScheduledStartTime;
	}

	public void setLastScheduledStartTime(Date lastScheduledStartTime) {
		this.lastScheduledStartTime = lastScheduledStartTime;
	}

	public TimeIntervalType getTimeIntervalType() {
		return timeIntervalType;
	}

	public void setTimeIntervalType(TimeIntervalType timeIntervalType) {
		this.timeIntervalType = timeIntervalType;
	}

	public Boolean getAgentJob() {
		return agentJob;
	}

	public void setAgentJob(Boolean agentJob) {
		this.agentJob = agentJob;
	}

	public void setClientId(BigInteger clientId) {
		this.clientId = clientId;
	}
	
	public BigInteger getClientId() {
		return clientId;
	}
	
	
	

}