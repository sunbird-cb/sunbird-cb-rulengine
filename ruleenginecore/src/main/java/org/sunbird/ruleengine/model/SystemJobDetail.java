package org.sunbird.ruleengine.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@Entity
@Table(name = "SYSTEM_JOB_DETAIL")
public class SystemJobDetail extends AbstractMultiTenantEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	private BigInteger id;

	@Column(name = "JOB_NAME")
	String jobName;

	@Column(name = "LAST_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastStartTime;

	@Column(name = "LAST_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date lastEndTime;

	@Column(name = "NEXT_RUN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	Date nextRunTime;

	@Version
	Integer version;

	@Column(name = "INTERVAL_TYPE")
	IntervalType intervalType = IntervalType.INTERVAL_AFTER_LAST_FINISH;

	@Column(name = "JOB_INTERVAL")
	Integer interval;

	@Transient
	Date toDateTime;

	Status status;

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
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


	public enum IntervalType {
		INTERVAL_AFTER_LAST_FINISH, INTERVAL_AFTER_LAST_START
	}

}
