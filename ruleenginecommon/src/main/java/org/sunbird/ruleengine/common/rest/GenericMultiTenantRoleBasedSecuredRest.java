package org.sunbird.ruleengine.common.rest;


import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.common.NotAuthenticatedException;
import org.sunbird.ruleengine.common.NotAuthorizedException;
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.model.AbstractEntity;
import org.sunbird.ruleengine.service.GenericService;

public abstract class GenericMultiTenantRoleBasedSecuredRest<T extends AbstractEntity, U> {
	private static final Logger logger = LogManager.getLogger(GenericMultiTenantRoleBasedSecuredRest.class);
	private Class<T> t;
	private Class<U> u;
	
	public abstract GenericService<T, U> getService();
	
	public abstract GenericService<T, U> getUserService();
	
	public abstract String rolePrefix();
	
	
	public GenericMultiTenantRoleBasedSecuredRest() {
		
	}
	
	public GenericMultiTenantRoleBasedSecuredRest(Class<T> t, Class<U> u) {
		this.t=t;
		this.u=u;
	}
	
	public T instantiateEntity() {
		try {
			return t.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
		return null;
	}

	public U instantiateCriteria() {
		try {
			return u.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
		return null;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public  @ResponseBody T get(@PathVariable("clientCode") String clientCode, @PathVariable("id") BigInteger id, Principal principal){
		if(isGetAuthenticated() && principal==null)
		{
			throw new NotAuthenticatedException();
		}
		validateAuthorization( clientCode,  principal);
		if(isGetAuthorized() && getService().getCount(instantiateCriteria(), getAuthorizationQueryForGet(id,principal)).longValue()==0)
		{
			throw new NotAuthorizedException();
		}
		return getService().get(id);
		
		

	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public  @ResponseBody Response<T> save(@PathVariable("clientCode") String clientCode,@RequestBody T t, Principal principal){
		if(isSaveAuthenticated() && principal==null)
		{
			throw new NotAuthenticatedException();
		}
		validateAuthorization( clientCode,  principal);
		if(isSaveAuthorized() && getService().getCount(instantiateCriteria(), getAuthorizationQueryForSave(t,principal)).longValue()==0)
		{
			throw new NotAuthorizedException();
		}
		if(principal!=null)
		{
		t.setCreatedBy(new BigInteger(principal.getName()));
		t.setLastUpdatedBy(new BigInteger(principal.getName()));
		t.setCreationDate(new java.util.Date());
		t.setLastUpdateDate(new java.util.Date());
		}
		validateSave(t,principal);
		getService().save(t);
		return new Response<T>(true, t);
	}
	
	@RequestMapping( method = RequestMethod.PUT)
	public @ResponseBody Response<T> update(@PathVariable("clientCode") String clientCode,@RequestBody T t, Principal principal){
		if(isUpdateAuthenticated() && principal==null)
		{
			throw new NotAuthenticatedException();
		}
		
		validateAuthorization( clientCode,  principal);
		if(principal!=null)
		{
			t.setLastUpdatedBy(new BigInteger(principal.getName()));
			t.setLastUpdateDate(new java.util.Date());
		}
		validateUpdate(t,principal);
		getService().update(t);
		return new Response<T>(true, t);
	}
	@RequestMapping(method = RequestMethod.GET)
	public  @ResponseBody List<T> getListByCriteria(@PathVariable("clientCode") String clientCode, @ModelAttribute U t,@RequestParam(value= "firstResult", required=false) int firstResult, @RequestParam(value="maxResult", required=false) int maxResult, Principal principal){
		if(isGetAuthenticated() && principal==null)
		{
			throw new NotAuthenticatedException();
		}
		validateAuthorization( clientCode,  principal);
		if(isSearchAuthorized() && getService().getCount(instantiateCriteria(), getAuthorizationQueryForSearch(t,principal)).longValue()==0)
		{
			throw new NotAuthorizedException();
		}
		return getService().getListByCriteria(t, firstResult, maxResult);
	}
	
	@RequestMapping(value="/searchWithQuery", method = RequestMethod.GET)
	public  @ResponseBody List<T> getListByCriteriaWithQuery(@PathVariable("clientCode") String clientCode, @ModelAttribute U t,@RequestParam(value= "firstResult", required=false) int firstResult, @RequestParam(value="maxResult", required=false) int maxResult, Principal principal){
		if(isSearchAuthenticated() && principal==null)
		{
			throw new NotAuthenticatedException();
		}
		validateAuthorization( clientCode,  principal);
		if(isSearchAuthorized() && getService().getCount(instantiateCriteria(), getAuthorizationQueryForSearch(t,principal)).longValue()==0)
		{
			throw new NotAuthorizedException();
		}
		return getService().getListByCriteria(t, getSearchQuery(), getSearchOrderBy(), firstResult, maxResult, getMapper());
	}
	
	
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public  @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute U t, Principal principal){
		return getService().getCount(t);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Response<T> delete(@PathVariable("clientCode") String clientCode,@PathVariable("id") Long id, Principal principal){
		if(isDeleteAuthenticated() && principal==null)
		{
			throw new NotAuthenticatedException();
		}
		validateAuthorization( clientCode,  principal);
		if(isDeleteAuthorized() && getService().getCount(instantiateCriteria(), getAuthorizationQueryForDelete(id,principal)).longValue()==0)
		{
			throw new NotAuthorizedException();
		}
		getService().delete(id);
		return new Response<T>(true, null);
	}
	
public String getTableForUpdateByColumn(){return null;}
	
	public  String getAuthorizationQueryForGet(BigInteger Id, Principal principal){return "select count(id) from "+getUserTable()+" usr inner join user_role_join ur on usr.id=ur.user_id inner join role_permission_join rp on rp.role_id=ur.role_id inner join permission pr on pr.id=rp.permission_id where pr.permission_name="+getPermissionName()+" and usr.id="+principal.getName()+" ";};
	public  String getPermissionName(){return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName().toLowerCase()+"_get";};
	public  String getAuthorizationQueryForDelete(Long id, Principal principal){return "select count(id) from "+getUserTable()+" usr inner join user_role_join ur on usr.id=ur.user_id inner join role_permission_join rp on rp.role_id=ur.role_id inner join permission pr on pr.id=rp.permission_id where pr.permission_name="+deletePermissionName()+" and usr.id="+principal.getName()+" ";};
	public  String deletePermissionName(){return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName().toLowerCase()+"_delete";};
	public  String getAuthorizationQueryForSearch(U searchCriteria, Principal principal){return "select count(id) from "+getUserTable()+" usr inner join user_role_join ur on usr.id=ur.user_id inner join role_permission_join rp on rp.role_id=ur.role_id inner join permission pr on pr.id=rp.permission_id where pr.permission_name="+searchPermissionName()+" and usr.id="+principal.getName()+" ";};
	public  String searchPermissionName(){return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName().toLowerCase()+"_search";};
	public  String getAuthorizationQueryForSave(T entity, Principal principal){return "select count(id) from "+getUserTable()+" usr inner join user_role_join ur on usr.id=ur.user_id inner join role_permission_join rp on rp.role_id=ur.role_id inner join permission pr on pr.id=rp.permission_id where pr.permission_name="+savePermissionName()+" and usr.id="+principal.getName()+" ";};
	public  String savePermissionName(){return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName().toLowerCase()+"_save";};
	public  String getAuthorizationQueryForUpdate(T entity, Principal principal){return "select count(usr.id) from "+getUserTable()+" usr inner join user_role_join ur on usr.id=ur.user_id inner join role_permission_join rp on rp.role_id=ur.role_id inner join permission pr on pr.id=rp.permission_id where pr.permission_name='"+updatePermissionName()+"' and usr.id="+principal.getName()+" ";};
	public  String updatePermissionName(){return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName().toLowerCase()+"_update";};
	public  String getAuthorizationQueryForPartialUpdate(T entity, Principal principal){return "select count(id) from "+getUserTable()+" usr inner join user_role_join ur on usr.id=ur.user_id inner join role_permission_join rp on rp.role_id=ur.role_id inner join permission pr on pr.id=rp.permission_id where pr.permission_name='"+updatePermissionName()+"' and usr.id="+principal.getName()+" ";};
	
	public  String getAuthorizationQuery(String clientCode, Principal principal){return "select count(usr.id) from USER_ usr, CLIENT cl where usr.client_id = cl.id and cl.code = '"+clientCode+"' and usr.id = "+principal.getName()+" ";};
	public  String isAdmin(String clientCode, Principal principal){return "select count(cl.id) from CLIENT cl where cl.code = '"+clientCode+"' and cl.IS_ADMIN=1";};
	
	
	
	public  boolean isUpdateAuthorized(){return false;}
	public  boolean isSaveAuthorized(){return false;}
	public  boolean isDeleteAuthorized(){return false;}
	public  boolean isGetAuthorized(){return false;}
	public  boolean isSearchAuthorized(){return false;}
	public  boolean isAutherized() {
		return true;
	}
	public  boolean isUpdateAuthenticated(){return true;}
	public  boolean isSaveAuthenticated(){return true;}
	public  boolean isDeleteAuthenticated(){return true;}
	public  boolean isGetAuthenticated(){return false;}
	public  boolean isSearchAuthenticated(){return false;}
	
	public String getUserTable(){return "user_";};
	
	public String getSearchQuery(){return null;};
	
	public String getSearchOrderBy(){return "";};
	
	public AbstractObjectToEntityMapper<List<T>> getMapper(){return null;};
	public void validateSave(T t,Principal principal){};
	public void validateUpdate(T t,Principal principal){};

	public void validateAuthorization(String clientCode, Principal principal)
	{
		if(isAutherized() && getService().getCount(instantiateCriteria(), getAuthorizationQuery(clientCode,principal)).longValue()==0 
				&& getService().getCount(instantiateCriteria(), isAdmin(clientCode,principal)).longValue()==0 )
		{
			throw new NotAuthorizedException();
		}
	}
	
	
	
	
	
	
}
