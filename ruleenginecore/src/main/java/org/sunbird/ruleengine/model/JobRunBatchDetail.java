package org.sunbird.ruleengine.model;

import java.math.BigInteger;

public class JobRunBatchDetail {
	public String startTime;
	public String endTime;
	public BigInteger batches;
	public BigInteger successCount;
	public BigInteger failureCount;
	
	
	public JobRunBatchDetail(String startTime, String endTime, BigInteger batches, BigInteger successCount,
			BigInteger failureCount) {
		
		this.startTime = startTime;
		this.endTime = endTime;
		this.batches = batches;
		this.successCount = successCount;
		this.failureCount = failureCount;
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
	public BigInteger getBatches() {
		return batches;
	}
	public void setBatches(BigInteger batches) {
		this.batches = batches;
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
	
	
	
	
	
    
	

}
