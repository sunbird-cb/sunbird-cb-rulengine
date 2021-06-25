package org.sunbird.ruleengine.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.model.JobSettings;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.JobSettingsService;

import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/jobSettings")
public class JobSettingsRest extends GenericMultiTenantRoleBasedSecuredRest<JobSettings, JobSettings> {
	public JobSettingsRest() {
	super(JobSettings.class,JobSettings.class);
	}
	@Autowired
	JobSettingsService jobSettingsService;

	@Autowired
	ClientService clientService;

	@Override	
	public GenericService<JobSettings, JobSettings> getService() {
		return jobSettingsService;
	}

	@Override
	public GenericService<JobSettings, JobSettings> getUserService() {
		return jobSettingsService;
	}

	@Override
	public String rolePrefix() {
		return "jobSettings";
	}

	@RequestMapping(value = "/getJobSetting", method = RequestMethod.GET)
	public @ResponseBody Object getJobSetting(@PathVariable("clientCode") String clientCode, @ModelAttribute JobSettings t,
			@RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		return getService().getListByCriteria(t,"select js.ID, js.JOB_DETAIL_ID, js.VALUE, js.KEY_,js.CLIENT_ID from JOB_SETTINGS js where 1=1 " ,"order by js.ID desc", firstResult, maxResult);
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public  @ResponseBody Long count(@PathVariable("clientCode") String clientCode, 
			@ModelAttribute JobSettings t,Principal principal){
		return getService().getCount(t, "select count(client.ID) from CLIENT_SETTINGS ").longValue();
	}


	@Override
	@RequestMapping(method = RequestMethod.POST)
	public  @ResponseBody Response<JobSettings> save(@PathVariable("clientCode")String clientCode, 
			@RequestBody JobSettings t,Principal principal){
		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		return super.save(clientCode,t,principal);
	}


	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public  @ResponseBody Response<JobSettings> update(@PathVariable("clientCode")String clientCode, 
			@RequestBody JobSettings t,Principal principal){
		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		return super.update(clientCode,t,principal);
	}

	@RequestMapping(value="/getAllKeys",method= RequestMethod.GET)
	public @ResponseBody Object getAllKeys (@PathVariable("clientCode") String clientCode,Principal principal){
		return getService().getListByCriteria(new JobSettings(), "SELECT DISTINCT(KEY_) FROM JOB_SETTINGS WHERE 1=1", "ORDER BY KEY_", -1, 0);
	}

}

