package org.sunbird.ruleengine.contracts;

import java.math.BigInteger;
import java.util.LinkedHashMap;

public class RequestObject {

	private String jobCode;
	private LinkedHashMap<?, ?> data;
	private BigInteger jobRunId;
	
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public LinkedHashMap<?, ?> getData() {
		return data;
	}
	public void setData(LinkedHashMap<?, ?> data) {
		this.data = data;
	}
	public BigInteger getJobRunId() {
		return jobRunId;
	}
	public void setJobRunId(BigInteger jobRunId) {
		this.jobRunId = jobRunId;
	}
}
