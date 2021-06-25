package org.sunbird.ruleengine.model;

import java.math.BigInteger;
import java.util.List;

public class JobMonitoringData {
	public String jobName;
	public String startTime;
	public String endTime;
	public Integer interval;
	public String execStartTime;
	public String execEndTime;
	public BigInteger batchCount;
	public BigInteger successCount;
	public BigInteger failureCount;
	public List<ErrorCodeCount> errorCodeCount;
	public String jobExecutionStatus;
	
	public JobMonitoringData(String jobName, String startTime, String endTime, Integer interval, String execStartTime,
			String execEndTime, BigInteger batchCount, BigInteger successCount, BigInteger failureCount,
			List<ErrorCodeCount> errorCodeCount, String jobExecutionStatus) {
		
		this.jobName = jobName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.interval = interval;
		this.execStartTime = execStartTime;
		this.execEndTime = execEndTime;
		this.batchCount = batchCount;
		this.successCount = successCount;
		this.failureCount = failureCount;
		this.errorCodeCount = errorCodeCount;
		this.jobExecutionStatus = jobExecutionStatus;
		
	}
	public JobMonitoringData() {
		// TODO Auto-generated constructor stub
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public String getExecStartTime() {
		return execStartTime;
	}
	public void setExecStartTime(String execStartTime) {
		this.execStartTime = execStartTime;
	}
	public String getExecEndTime() {
		return execEndTime;
	}
	public void setExecEndTime(String execEndTime) {
		this.execEndTime = execEndTime;
	}
	public BigInteger getBatchCount() {
		return batchCount;
	}
	public void setBatchCount(BigInteger batchCount) {
		this.batchCount = batchCount;
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
	public List<ErrorCodeCount> getErrorCodeCount() {
		return errorCodeCount;
	}
	public void setErrorCodeCount(List<ErrorCodeCount> errorCodeCount) {
		this.errorCodeCount = errorCodeCount;
	}
	public String getJobExecutionStatus() {
		return jobExecutionStatus;
	}
	public void setJobExecutionStatus(String jobExecutionStatus) {
		this.jobExecutionStatus = jobExecutionStatus;
	}
	
	
}
