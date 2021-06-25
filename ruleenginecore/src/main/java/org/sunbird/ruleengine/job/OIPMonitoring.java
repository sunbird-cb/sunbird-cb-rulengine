package org.sunbird.ruleengine.job;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.transaction.jta.platform.internal.JOnASJtaPlatform;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.sunbird.ruleengine.model.Client;
import org.sunbird.ruleengine.model.ErrorCodeCount;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.model.JobMonitoringData;
import org.sunbird.ruleengine.model.JobRunBatchDetail;
import org.sunbird.ruleengine.model.JobRunDetail;
import org.sunbird.ruleengine.model.MonitoringReport;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.IntegrationInstanceFailureService;
import org.sunbird.ruleengine.service.Job;
import org.sunbird.ruleengine.service.JobDetailService;
import org.sunbird.ruleengine.service.JobRunDetailService;




@Service
public class OIPMonitoring{
	private static final Logger logger = LogManager.getLogger(OIPMonitoring.class);

	@Autowired
	ClientService clientService;
	
	@Autowired
	JobDetailService jobDetailService;
	
	@Autowired
	JobRunDetailService jobRunDetailService;
	
	@Autowired
	IntegrationInstanceFailureService integrationInstanceFailureService;
	
	
	
	String integrationInstanceFailureQuery = "SELECT ERROR_CODE as errorCode,count(ID) as count FROM INTEGRATION_INSTANCE_FAILURE where JOB_RUN_ID IN";
	
	String jobRunDetailQuery = "SELECT min(start_time),case when MAX(end_time IS NULL)=0 THEN max(end_time) END AS end_time,cast(count(ID) as SIGNED) as batches,cast(sum(success_count) as SIGNED) as success_count,cast(sum(failure_count) as SIGNED) as failure_count FROM JOB_RUN_DETAIL where id IN";
	
	String orderby = " group by ERROR_CODE order by ERROR_CODE asc";
	
	public List<Client> getClient(String code){
		Client cli = new Client();
		cli.setCode(code);
		List<Client> clientList = clientService.getListByColumnNameAndValue("code", code);
		return clientList;
	}
	
	public List<JobDetail> getAllJobsByClient(JobDetail jobDetail){
		List<JobDetail> jobDetails = jobDetailService.getListByCriteria(jobDetail, -1, 0);
		return jobDetails;
	}
	
	public List<JobRunDetail> getJobRunDetail(JobRunDetail jobRunDetail){
		List<JobRunDetail> jrDetail = jobRunDetailService.getListByCriteria(jobRunDetail, -1, 0);
		return jrDetail;
	}
	
	public List<JobRunBatchDetail> getJobRunBatchDetail(String jobRunIDS){
		List<JobRunBatchDetail> jobRunBatchDetail = new ArrayList<JobRunBatchDetail>();
		StringBuffer sb = new StringBuffer(jobRunDetailQuery);
		sb.append(" "+jobRunIDS);
		String query = sb.toString();
		Object object = jobRunDetailService.getListByCriteria(new JobRunDetail(),query, null, -1, 0);
		if(object !=null) {
		jobRunBatchDetail = convertObjectToJobRunDetail(object);
		}
		return jobRunBatchDetail;
	}
	
	public List<ErrorCodeCount> getErrorCodeDetail(String jobRunIDS){
		List<ErrorCodeCount> er = new ArrayList<ErrorCodeCount>();
		StringBuffer sb = new StringBuffer(integrationInstanceFailureQuery);
		sb.append(" "+jobRunIDS);
		String query = sb.toString();
		Object object = integrationInstanceFailureService.getListByCriteria(new IntegrationInstanceFailure(),query,orderby,-1, 0);
		if(object != null) {
		er = convertObjectToList(object);
		}
		return er;
	}
	
	StringBuffer mailContent = new StringBuffer();
	
	public List<MonitoringReport> getMonitoringReport(List<Client> clientList,String startDate) {
		List<MonitoringReport> monitoringReportCollection = new ArrayList<MonitoringReport>();
		List<JobMonitoringData> jobMonitoringData = new ArrayList<JobMonitoringData>();
		try {
			for (Client client : clientList) {
				List<Client> clientDetail = getClient(client.getCode());
				System.out.println("Client Name is " + clientDetail.get(0).getName());
				MonitoringReport mr = new MonitoringReport();
				mr.setClient(clientDetail.get(0));
				JobDetail jb = new JobDetail();
				jb.setClientId(clientDetail.get(0).getId());
				List<JobDetail> jobDetails = getAllJobsByClient(jb);
				for (JobDetail jobDetail : jobDetails) {
					
					JobMonitoringData jmd = new JobMonitoringData();
					jmd.setJobName(jobDetail.getJobName());
					if(jobDetail.getInterval() != null) {
					jmd.setInterval(jobDetail.getInterval());
					}
					if(jobDetail.getLastStartTime() != null) {
					jmd.setStartTime(jobDetail.getLastStartTime().toString());
					}
					if(jobDetail.getLastEndTime() != null) {
					jmd.setEndTime(jobDetail.getLastEndTime().toString());
					}
					JobRunDetail jr = new JobRunDetail();
					jr.setJobDetailId(jobDetail.getId());
					if(startDate != null) {
						System.out.println("The Start Time is: "+startDate);
						jr.setStartTime(new Date(startDate));
					}else {
					jr.setStartTime(jobDetail.getLastStartTime());
					}
					
					List<JobRunBatchDetail> jobRunDetail = new ArrayList<JobRunBatchDetail>();
					List<JobRunDetail> jrDetail = getJobRunDetail(jr);
					StringBuffer jobrunids = new StringBuffer();
					String runids = "";
					if (jrDetail.size() > 0) {
						jobrunids.append("(");
						for (int i = 0; i < jrDetail.size(); i++) {
							if (i < jrDetail.size() - 1)
								jobrunids.append(jrDetail.get(i).getId() + ",");
							else {
								jobrunids.append(jrDetail.get(i).getId());
							}
						}
						jobrunids.append(")");
						runids = jobrunids.toString();
						jobRunDetail = getJobRunBatchDetail(runids);
					}

					if (jobRunDetail.size() > 0 && jobRunDetail.get(0).getEndTime() != null) {
						jmd.setBatchCount(jobRunDetail.get(0).getBatches());
						jmd.setExecStartTime(jobRunDetail.get(0).getStartTime());
						jmd.setExecEndTime(jobRunDetail.get(0).getEndTime());
						jmd.setSuccessCount(jobRunDetail.get(0).getSuccessCount());
						jmd.setFailureCount(jobRunDetail.get(0).getFailureCount());

						List<ErrorCodeCount> ercount = getErrorCodeDetail(runids);

						jmd.setErrorCodeCount(ercount);
						jmd.setJobExecutionStatus("SUCCESS");
					} else if (jobRunDetail.size() > 0 && jobRunDetail.get(0).getEndTime() == null) {
						jmd.setExecStartTime(jobRunDetail.get(0).getStartTime());
						jmd.setExecEndTime(jobRunDetail.get(0).getEndTime());
						jmd.setJobExecutionStatus("FAILED");

					} else {
						jmd.setExecStartTime(null);
						if(jobDetail.getLastStartTime() != null && jobDetail.getNextRunTime() != null && jobDetail.getInterval() != null) {
					    Date lastStartTime =  jobDetail.getLastStartTime();
					    Integer jobIntervalseconds = jobDetail.getInterval();
					   
					    Calendar nrt = Calendar.getInstance();
					    nrt.setTime(jobDetail.getNextRunTime());
					    Date nextRunTime = nrt.getTime();
					    
					    Calendar cal = Calendar.getInstance();
				        cal.setTime(lastStartTime);
				        cal.add(Calendar.SECOND, jobIntervalseconds);
				        Date calcNextRunTime = cal.getTime();
				        System.out.println("Last Start Time "+lastStartTime);
				        System.out.println("Calculated Next Start Time "+calcNextRunTime);
				        System.out.println("Next Run Time "+nextRunTime);
				        if(calcNextRunTime.compareTo(nextRunTime) < 0) {
				        	jmd.setJobExecutionStatus("JOB HAS YET TO START");
					        }
				            else {
				            	jmd.setJobExecutionStatus("JOB NOT STARTED ON TIME");
				            }
					    
						} else {
							jmd.setJobExecutionStatus("JOB NOT STARTED ON TIME");
						}
						
					}
					jobMonitoringData.add(jmd);
					mr.setJobMonitoringData(jobMonitoringData);
					 
					
				}
				
				monitoringReportCollection.add(mr);
				jobMonitoringData = new ArrayList<JobMonitoringData>();
			}

			return monitoringReportCollection;
			
		} catch (Exception e) {
			logger.info("Exception in OIPMonitoring.runJob()");
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
		return null;
	}
	
	
	public List<ErrorCodeCount> convertObjectToList(Object obj) {
		List<ErrorCodeCount> ecc = new ArrayList<ErrorCodeCount>();
		  for(Object[] object:((List<Object[]>)obj)) {
			  ErrorCodeCount er = new ErrorCodeCount((String) object[0], (BigInteger) object[1]);
			  ecc.add(er);
		    }
		  return ecc;
	}
	
	
	public List<JobRunBatchDetail> convertObjectToJobRunDetail(Object obj) {
		List<JobRunBatchDetail> jrbd = new ArrayList<JobRunBatchDetail>();
		  String start_time = null;
		  String end_time = null;
		  
		  
		  for(Object[] object:((List<Object[]>)obj)) {
			  if(object[0] != null) {
				start_time =  object[0].toString();
			  }
			  if(object[1] != null) {
				  end_time = object[1].toString();
			  }
			  JobRunBatchDetail jr = new JobRunBatchDetail(start_time, end_time, (BigInteger) object[2],(BigInteger) object[3],(BigInteger) object[4]);
			  jrbd.add(jr);
		    }
		  return jrbd;
	}
}
