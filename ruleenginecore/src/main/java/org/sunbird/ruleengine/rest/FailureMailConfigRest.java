package org.sunbird.ruleengine.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.model.FailureMailConfig;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.FailureMailConfigService;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/failureMailConfigRest")
public class FailureMailConfigRest
		extends GenericMultiTenantRoleBasedSecuredRest<FailureMailConfig, FailureMailConfig> {

	public FailureMailConfigRest() {
		super(FailureMailConfig.class, FailureMailConfig.class);
	}

	@Autowired
	FailureMailConfigService failureMailConfigService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<FailureMailConfig, FailureMailConfig> getService() {
		return failureMailConfigService;
	}

	@Override
	public GenericService<FailureMailConfig, FailureMailConfig> getUserService() {
		return failureMailConfigService;
	}

	@Override
	public String rolePrefix() {
		return "failureMailConfig";
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<FailureMailConfig> getListByCriteria(@PathVariable("clientCode") String clientCode,
			@ModelAttribute FailureMailConfig t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		//return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
		String query= "SELECT failureMailConfig.ID,failureMailConfig.CLIENT_ID,failureMailConfig.NOT_IN_ERROR_PATTERN_ID,failureMailConfig.JOB_DETAIL_ID,epm.ERROR_CODE,jd.JOB_NAME,client.NAME FROM FAILURE_MAIL_CONFIG failureMailConfig INNER JOIN ERROR_PATTERN_MASTER epm ON (epm.id=failureMailConfig.NOT_IN_ERROR_PATTERN_ID) LEFT JOIN JOB_DETAIL jd on (jd.ID = failureMailConfig.JOB_DETAIL_ID) INNER JOIN CLIENT client on (client.ID =failureMailConfig.CLIENT_ID) ";
		return (List<FailureMailConfig>) failureMailConfigService.getListByCriteria(t, query, null, firstResult, maxResult);
				
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute FailureMailConfig t,
			Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		return super.count(clientCode, t, principal);
	}
	
	@Override
	@RequestMapping(method = RequestMethod.POST)
	public Response<FailureMailConfig> save(@PathVariable("clientCode") String clientCode, @RequestBody FailureMailConfig t, Principal principal) {
		return super.save(clientCode, t, principal);
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public Response<FailureMailConfig> update(@PathVariable("clientCode") String clientCode,@RequestBody FailureMailConfig t, Principal principal) {
		return super.update(clientCode, t, principal);
	}

	
	
}
