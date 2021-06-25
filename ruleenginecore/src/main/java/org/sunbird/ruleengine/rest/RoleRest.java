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
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.model.Role;
import org.sunbird.ruleengine.service.GenericService;
import org.sunbird.ruleengine.service.RoleService;

@RestController
@RequestMapping("{clientCode}/role")
public class RoleRest extends GenericMultiTenantRoleBasedSecuredRest<Role, Role>{
	
	public RoleRest() {
		super(Role.class,Role.class);
	}
	
	@Autowired
	RoleService roleService;

	@Override
	public GenericService<Role, Role> getService() {
		return roleService;
	}

	@Override
	public GenericService<Role, Role> getUserService() {
		return roleService;
	}
	

	@Override
	public String rolePrefix() {
		
		return "role";
	}
	
	@RequestMapping(value = "/getDataNotInUserRole/{userId}" ,method = RequestMethod.GET)
	 public  @ResponseBody List<Role> getusersByFunction(@PathVariable("clientCode") String clientCode,@PathVariable("userId") String userId,@ModelAttribute Role t,@RequestParam(value= "firstResult", required=false) int firstResult, @RequestParam(value="maxResult", required=false) int maxResult){
		return (List<Role>) getService().getListByCriteria(t, "select role.id,role.role_name from ROLE role where role.id not in(select role_id from USER_ROLE_JOIN where user_id= '"+userId+"')" ,"order by role.ID desc", firstResult, maxResult);
	}		
	
	
	
	

	@Override
	 @RequestMapping(method = RequestMethod.PUT)
	 public @ResponseBody
	 Response<Role> update(
	   @PathVariable("clientCode") String clientCode,
	   @RequestBody Role t,Principal principal) {
	  
	  return super.update(clientCode, t, principal);
	 }

	 @Override
	 @RequestMapping(method = RequestMethod.POST)
	 public @ResponseBody
	 Response<Role> save(
	   @PathVariable("clientCode") String clientCode,
	   @RequestBody Role t,Principal principal)  {
	  
	  return super.save(clientCode, t, principal);
	 }
	
	
}