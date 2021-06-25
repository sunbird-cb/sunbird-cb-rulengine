package org.sunbird.ruleengine.rest;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.dao.SystemJobDetailNewDao;
import org.sunbird.ruleengine.model.SystemJobDetail;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.GenericService;
import org.sunbird.ruleengine.service.SystemJobDetailNewService;
import org.sunbird.ruleengine.service.SystemJobDetailService;

@RestController
@RequestMapping("{clientCode}/systemJobDetailRest")
public class SystemJobDetailRest extends GenericMultiTenantRoleBasedSecuredRest<SystemJobDetail, SystemJobDetail> {
	 private static final Logger logger = LogManager.getLogger(SystemJobDetailRest.class);

	public SystemJobDetailRest() {
		super(SystemJobDetail.class, SystemJobDetail.class);
	}

	@Autowired
	SystemJobDetailNewService systemJobDetialService;
	
	@Autowired
	SystemJobDetailService systemJobDetialoldService;
	
	@Autowired
	SystemJobDetailNewDao systemJobDetialnewDao;

	@Autowired
	ClientService clientService;

	@Override
	public GenericService<SystemJobDetail, SystemJobDetail> getService() {
		return systemJobDetialService;
	}

	@Override
	public GenericService<SystemJobDetail, SystemJobDetail> getUserService() {
		return systemJobDetialService;
	}

	@Override
	public String rolePrefix() {
		return "systemJobDetail";
	}
	

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<SystemJobDetail> getListByCriteria(@PathVariable("clientCode") String clientCode,
			@ModelAttribute SystemJobDetail t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));
		return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
	}
	
	@RequestMapping(value = "/getsystemJobDetailEnums", method = RequestMethod.GET)
	public @ResponseBody Object getStepType(@PathVariable("clientCode") String clientCode, Principal principal) {
		Object[] object = new Object[1];
		object[0] = SystemJobDetail.Status.values();
		return object;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String invoke(@PathVariable("clientCode") String clientCode, @PathVariable("id") BigInteger id,Principal principal) throws Exception {
		System.out.println("Cron Started");
		logger.info("Cron Started");
		super.validateAuthorization(clientCode, principal);
		SystemJobDetail systemJobDetail = new SystemJobDetail();
		systemJobDetail.setId(id);
		systemJobDetail.setStatus(SystemJobDetail.Status.STOPPED);
		List<SystemJobDetail> systemJobDetails = systemJobDetialnewDao.getListByCriteria(systemJobDetail, -1, 0);
		systemJobDetialoldService.runCron(systemJobDetails);
		return "ok";
	}

	@Override
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute SystemJobDetail t,
			Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode,t.getClientId()));

		return super.count(clientCode, t, principal);

	}
}