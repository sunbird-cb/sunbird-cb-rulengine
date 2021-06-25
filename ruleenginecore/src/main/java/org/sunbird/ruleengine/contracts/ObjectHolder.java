package org.sunbird.ruleengine.contracts;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectHolder {
	
	
	
	BigInteger jobRunningId;
	boolean isDryRun;
	boolean runAgain=false;
	String startSequence;
	String endSequence;
	String batchSize;
	Date currentDate= new Date();
	Date lastScheduledStartTime;
	Date nextRunTime;
	Integer jobRunSuccessCount=0;
	Integer jobRunFailureCount=0;
	int noOfFiles=-1;
	Map<String, Config> configs = new HashMap<>();
	public Map<String, Config> getConfigs() {
		return configs;
	}
	
	public String getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(String batchSize) {
		this.batchSize = batchSize;
	}

	public void setConfigs(Map<String, Config> configs) {
		this.configs = configs;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public BigInteger getJobRunningId() {
		return jobRunningId;
	}

	public void setJobRunningId(BigInteger jobRunningId) {
		this.jobRunningId = jobRunningId;
	}

	public boolean isDryRun() {
		return isDryRun;
	}

	public void setDryRun(boolean isDryRun) {
		this.isDryRun = isDryRun;
	}

	public Date getLastScheduledStartTime() {
		return lastScheduledStartTime;
	}

	public void setLastScheduledStartTime(Date lastScheduledStartTime) {
		this.lastScheduledStartTime = lastScheduledStartTime;
	}

	public Date getNextRunTime() {
		return nextRunTime;
	}

	public void setNextRunTime(Date nextRunTime) {
		this.nextRunTime = nextRunTime;
	}
	
	public boolean isRunAgain() {
		return runAgain;
	}

	public void setRunAgain(boolean runAgain) {
		this.runAgain = runAgain;
	}

	public String getStartSequence() {
		return startSequence;
	}

	public void setStartSequence(String startSequence) {
		this.startSequence = startSequence;
	}

	public String getEndSequence() {
		return endSequence;
	}

	public void setEndSequence(String endSequence) {
		this.endSequence = endSequence;
	}

	public Integer getJobRunSuccessCount() {
		return jobRunSuccessCount;
	}

	public void setJobRunSuccessCount(Integer jobRunSuccessCount) {
		this.jobRunSuccessCount = jobRunSuccessCount;
	}

	public Integer getJobRunFailureCount() {
		return jobRunFailureCount;
	}

	public void setJobRunFailureCount(Integer jobRunFailureCount) {
		this.jobRunFailureCount = jobRunFailureCount;
	}

	public int getNoOfFiles() {
		return noOfFiles;
	}

	public void setNoOfFiles(int noOfFiles) {
		this.noOfFiles = noOfFiles;
	}

	
	
}
