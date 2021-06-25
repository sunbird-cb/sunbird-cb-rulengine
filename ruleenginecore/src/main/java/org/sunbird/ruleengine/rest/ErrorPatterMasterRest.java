package org.sunbird.ruleengine.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.model.ErrorPatternMaster;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.ErrorPatternMasterService;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/errorPatternMaster")
public class ErrorPatterMasterRest extends GenericMultiTenantRoleBasedSecuredRest<ErrorPatternMaster, ErrorPatternMaster>{



	public  ErrorPatterMasterRest() {
		super(ErrorPatternMaster.class,ErrorPatternMaster.class);
	}
	
	@Autowired
	ErrorPatternMasterService errorPatternMasterService;
	
	@Autowired
	ClientService clientService;
	
	@Override
	public GenericService<ErrorPatternMaster, ErrorPatternMaster> getService() {
		return errorPatternMasterService;
	}

	@Override
	public GenericService<ErrorPatternMaster, ErrorPatternMaster> getUserService() {
		return errorPatternMasterService;
	}

	@Override
	public String rolePrefix() {
		return "errorPatternMasterService";
	}

	@Override
	public Response<ErrorPatternMaster> save(@PathVariable("clientCode") String clientCode, @RequestBody ErrorPatternMaster t, Principal principal){
		//t.setClientId(clientService.getClientCodeById(clientCode));
		return super.save(clientCode, t, principal);
	}

	@Override
	public Response<ErrorPatternMaster> update(@PathVariable("clientCode")String clientCode,@RequestBody ErrorPatternMaster t, Principal principal) {
	//	t.setClientId(clientService.getClientCodeById(clientCode));
		return super.update(clientCode, t, principal);
	}

	@Override
	public List<ErrorPatternMaster> getListByCriteria(@PathVariable("clientCode")String clientCode, @ModelAttribute ErrorPatternMaster t,@RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		// TODO Auto-generated method stub
		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
	}


	
	
}
