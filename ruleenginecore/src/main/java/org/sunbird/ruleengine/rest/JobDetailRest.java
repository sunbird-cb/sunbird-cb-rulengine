package org.sunbird.ruleengine.rest;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.model.JobDetail;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.JobDetailService;

import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/jobDetailRest")
public class JobDetailRest extends GenericMultiTenantRoleBasedSecuredRest<JobDetail, JobDetail> {

	public JobDetailRest() {
		super(JobDetail.class, JobDetail.class);
	}

	@Autowired
	JobDetailService jobDetailService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<JobDetail, JobDetail> getService() {
		return jobDetailService;
	}

	@Override
	public GenericService<JobDetail, JobDetail> getUserService() {
		return jobDetailService;
	}

	@Override
	public String rolePrefix() {
		return "jobDetail";
	}

	@RequestMapping(value = "/getJobDetailEnums", method = RequestMethod.GET)
	public @ResponseBody Object getStepType(@PathVariable("clientCode") String clientCode, Principal principal) {
		Object[] objects = new Object[3];
		objects[0] = JobDetail.Status.values();
		objects[1] = JobDetail.IntervalType.values();
		objects[2] = JobDetail.TimeIntervalType.values();
		return objects;
	}

	@RequestMapping(value = "/getJobDetail", method = RequestMethod.GET)
	public @ResponseBody Object getJobDetail(@PathVariable("clientCode") String clientCode, @ModelAttribute JobDetail t,
			@RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));

	super.validateAuthorization(clientCode, principal);
			return getService().getListByCriteria(t,
					"select jd.ID,jd.JOB_INTERVAL,jd.JOB_NAME,jd.LAST_END_TIME,jd.LAST_START_TIME,jd.NEXT_RUN_TIME,jd.JOB_STATUS,jd.CLIENT_ID,jd.VERSION,jd.INTERVAL_TYPE,jd.RETRY_ENABLED from JOB_DETAIL jd	where 1=1 ",
					"order by jd.ID desc", firstResult, maxResult);
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody Response<JobDetail> update(@PathVariable("clientCode") String clientCode,
			@Valid @RequestBody JobDetail t, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.update(clientCode, t, principal);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<JobDetail> save(@PathVariable("clientCode") String clientCode,
			@Valid @RequestBody JobDetail t, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.save(clientCode, t, principal);
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute JobDetail t,
			Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
			return getService().getCount(t, "select count(jd.ID) from JOB_DETAIL jd where 1=1 ").longValue();
	}



}
