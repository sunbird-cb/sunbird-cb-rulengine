package org.sunbird.ruleengine.model;

import java.math.BigInteger;
import java.util.List;

public class MonitoringReport {
	public Client client;
	public List<JobMonitoringData> jobMonitoringData;
	
	public MonitoringReport(Client client, List<JobMonitoringData> jobMonitoringData) {
		this.client = client;
		this.jobMonitoringData = jobMonitoringData;
	}
	public MonitoringReport() {
		// TODO Auto-generated constructor stub
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public List<JobMonitoringData> getJobMonitoringData() {
		return jobMonitoringData;
	}
	public void setJobMonitoringData(List<JobMonitoringData> jobMonitoringData) {
		this.jobMonitoringData = jobMonitoringData;
	}
	
	
}
