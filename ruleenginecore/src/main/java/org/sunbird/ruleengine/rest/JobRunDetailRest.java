package org.sunbird.ruleengine.rest;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.model.JobRunDetail;
import org.sunbird.ruleengine.service.GenericService;
import org.sunbird.ruleengine.service.JobRunDetailService;

@RestController
@RequestMapping("{clientCode}/jobRunDetailRest")
public class JobRunDetailRest extends GenericMultiTenantRoleBasedSecuredRest<JobRunDetail, JobRunDetail> {

	public JobRunDetailRest() {
	super(JobRunDetail.class,JobRunDetail.class);
	}
	

	@Autowired
	JobRunDetailService jobRunDetailService;
 
	@Autowired
	org.sunbird.ruleengine.service.ClientService clientService;

	@Override
	public GenericService<JobRunDetail, JobRunDetail> getService() {
		return jobRunDetailService;
	}

	@Override
	public GenericService<JobRunDetail, JobRunDetail> getUserService() {
		return jobRunDetailService;
	}

	@Override
	public String rolePrefix() {
		return "jobRunDetail";
	}

	
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<JobRunDetail> getListByCriteria(@PathVariable("clientCode") String clientCode,
			@ModelAttribute JobRunDetail t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		if (t.getEndDate() == null) {
			Calendar cal = Calendar.getInstance();
			t.setEndDate( new java.sql.Date(cal.getTimeInMillis()));
		} 
		else{
			t.setEndDate(new java.sql.Date(t.getEndDate().getTime()));
		}
		if(t.getStartDate()!=null)
			t.setStartDate(new java.sql.Date(t.getStartDate().getTime()));
		return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public Long count(@PathVariable("clientCode") String clientCode,@ModelAttribute JobRunDetail t, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		if (t.getEndDate() == null) {
			Calendar cal = Calendar.getInstance();
			t.setEndDate( new java.sql.Date(cal.getTimeInMillis()));
		} 
		else{
			t.setEndDate(new java.sql.Date(t.getEndDate().getTime()));
		}
		if(t.getStartDate()!=null)
			t.setStartDate(new java.sql.Date(t.getStartDate().getTime()));
		return super.count(clientCode, t, principal);
	}
	

}
