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
import org.sunbird.ruleengine.model.OipGlobalVariable;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.OipGlobalVariableService;

import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/oipGlobalVariableRest")
public class OipGlobalVariableRest
		extends GenericMultiTenantRoleBasedSecuredRest<OipGlobalVariable, OipGlobalVariable> {

	public OipGlobalVariableRest() {

		super(OipGlobalVariable.class, OipGlobalVariable.class);
	}

	@Autowired
	OipGlobalVariableService oipGlobalVariableService;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<OipGlobalVariable, OipGlobalVariable> getService() {

		return oipGlobalVariableService;
	}

	@Override
	public GenericService<OipGlobalVariable, OipGlobalVariable> getUserService() {

		return oipGlobalVariableService;
	}

	@Override
	public String rolePrefix() {

		return "oipGlobalVariable";
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<OipGlobalVariable> getListByCriteria(@PathVariable("clientCode") String clientCode,
			@ModelAttribute OipGlobalVariable t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {

		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		String query = "SELECT oipGlobalVariables.ID, oipGlobalVariables.CLIENT_ID, oipGlobalVariables.JOB_DETAIL_ID, oipGlobalVariables.GLOBAL_VARIABLE_NAME, oipGlobalVariables.GLOBAL_VARIABLE_VALUE, client.NAME, jobDetail.JOB_NAME FROM OIP_GLOBAL_VARIABLES oipGlobalVariables, CLIENT client, JOB_DETAIL jobDetail where oipGlobalVariables.JOB_DETAIL_ID = jobDetail.ID AND client.ID = oipGlobalVariables.CLIENT_ID  ";
		return (List<OipGlobalVariable>) oipGlobalVariableService.getListByCriteria(t, query, null, firstResult,
				maxResult);

	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute OipGlobalVariable t,
			Principal principal) {

		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		return super.count(clientCode, t, principal);

	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public Response<OipGlobalVariable> save(@PathVariable("clientCode") String clientCode,
			@RequestBody OipGlobalVariable t, Principal principal) {

		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		return super.save(clientCode, t, principal);

	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public Response<OipGlobalVariable> update(@PathVariable("clientCode") String clientCode,
			@RequestBody OipGlobalVariable t, Principal principal) {

		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		return super.update(clientCode, t, principal);

	}
}
