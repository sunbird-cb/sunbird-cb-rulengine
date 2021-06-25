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
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.model.Step;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.GenericService;
import org.sunbird.ruleengine.service.StepService;

@RestController
@RequestMapping("{clientCode}/stepRest")
public class StepRest extends GenericMultiTenantRoleBasedSecuredRest<Step, Step> {
	
	public StepRest() {
	super(Step.class,Step.class);
	}

	@Autowired
	StepService stepService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<Step, Step> getService() {
		return stepService;
	}

	@Override
	public GenericService<Step, Step> getUserService() {
		return stepService;
	}

	@Override
	public String rolePrefix() {
		return "step";
	}

	@RequestMapping(value = "/getStepEnums", method = RequestMethod.GET)
	public @ResponseBody Object getStepType(@PathVariable("clientCode") String clientCode, Principal principal) {
		Object[] objects = new Object[3];
		objects[0] = Step.ObjectType.values();
		objects[1] = Step.Result.values();
		objects[2] = Step.Type.values();
		return objects;
	}

	@RequestMapping(value = "/getStep", method = RequestMethod.GET)
	public @ResponseBody Object getStep(@PathVariable("clientCode") String clientCode, @ModelAttribute Step t,
			@RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		
		super.validateAuthorization(clientCode, principal);
		
		return getService().getListByCriteria(t,
				"select step.ID, step.JOB_DETAIL_ID, step.PARENT_ID, step.SUCCESS_KEY, step.SUCCESS_VALUE , step.FAILURE_JOB_DETAIL_ID , step.STEP_TYPE , step.OBJECT_TYPE ,  step.PATH , step.REQUEST_TEMPLATE , step.RESPONSE_TYPE, step.RESULT, step.SEQUENCE, step.SUB_TYPE, step.TYPE, step.HEADER_TEMPLATE, step.RESPONSE_FORMATTER_JS, step.ERROR_FORMATTER_JS, step.LOGGING_ENABLED, step.FAILURE_STEP_ID, step.STEP_CODE, step.UNIQUE_KEY_TEMPLATE, step.DUPLICATE_CHECK_ENABLED,step.CLIENT_ID,step.REQUEST_FORMATTER_JS,step.BEFORE_AGENT_CALL,step.REQUEST_TYPE from STEP step where 1=1 ",
				"order by step.SEQUENCE asc", firstResult, maxResult);
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody Response<Step> update(@PathVariable("clientCode") String clientCode,
			 @RequestBody Step t, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.update(clientCode, t, principal);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<Step> save(@PathVariable("clientCode") String clientCode, @RequestBody Step t,
			Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.save(clientCode, t, principal);
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute Step t,
			Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return getService().getCount(t, "select count(step.ID) from STEP step where 1=1 ").longValue();
	}

}
