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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.model.UserRoleJoin;
import org.sunbird.ruleengine.service.UserRoleJoinService;
import org.sunbird.ruleengine.vo.UserRoleJoinVo;

import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping("{clientCode}/userRole")
public class UserRoleJoinRest extends GenericMultiTenantRoleBasedSecuredRest<UserRoleJoin, UserRoleJoinVo>{

	public UserRoleJoinRest() {
		super(UserRoleJoin.class,UserRoleJoinVo.class);
	}

	@Autowired
	UserRoleJoinService userRoleService;

	@Override
	public GenericService<UserRoleJoin, UserRoleJoinVo> getService() {

		return userRoleService;
	}

	@Override
	public GenericService<UserRoleJoin, UserRoleJoinVo> getUserService() {

		return userRoleService;
	}

	@Override
	public String rolePrefix() {

		return "userRole";
	}

	@SuppressWarnings("deprecation")
	@Override
	@RequestMapping( method = RequestMethod.GET)
	public @ResponseBody List<UserRoleJoin> getListByCriteria(@PathVariable("clientCode") String clientCode,@ModelAttribute UserRoleJoinVo t,
			@RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		return (List<UserRoleJoin>) getService().getListByCriteria(t, "SELECT userRole.id, userRole.USER_ID, userRole.ROLE_ID ,role.ROLE_NAME FROM USER_ROLE_JOIN userRole left join ROLE role on(role.ID=userRole.ROLE_ID)  where 1=1  ", "order by userRole.ID DESC ", firstResult, maxResult);

	}

	@RequestMapping(value="/saveUserRole" ,method = RequestMethod.POST)
	public @ResponseBody Response<Boolean> save(@PathVariable("clientCode") String clientCode,@RequestBody UserRoleJoin userRoleJoin)
	{
		userRoleService.saveUserRole(userRoleJoin);
		return new Response<Boolean>(true, null);

	}



	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody
	Response<UserRoleJoin> update(
			@PathVariable("clientCode") String clientCode,
			@RequestBody UserRoleJoin t,Principal principal) {

		return super.update(clientCode, t, principal);
	}



}
