package org.sunbird.ruleengine.common.rest;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.service.GenericService;

public abstract class GenericRest<T, U> {
	private static final Logger logger = LogManager.getLogger(GenericRest.class);
	private Class<T> t;
	private Class<U> u;

	public abstract GenericService<T, U> getService();

	public GenericRest() {
	}

	public GenericRest(Class<T> t, Class<U> u) {
		this.t = t;
		this.u = u;
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

	//@PreAuthorize("#oauth2.hasScope('read')")	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public @ResponseBody
	T get(@PathVariable("id") Long id) {
		return getService().get(id);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody
	Response<T> save(@RequestBody T t) {
		
		getService().save(t);
		return new Response<T>(true, t);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public @ResponseBody
	Response<T> update(@RequestBody T t) {
		getService().update(t);
		return new Response<T>(true, t);
	}

	@RequestMapping(value = "/search/{firstResult}/{maxResult}", method = RequestMethod.POST)
	public @ResponseBody
	List<T> getListByCriteria(@RequestBody U t,
			@PathVariable("firstResult") int firstResult,
			@PathVariable("maxResult") int maxResult) {
		return getService().getListByCriteria(t, firstResult, maxResult);
	}

	@RequestMapping(value = "/count", method = RequestMethod.POST)
	public  @ResponseBody Long count(@RequestBody U t){
		return getService().getCount(t);
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Response<T> delete(@PathVariable("id") Long id){
		getService().delete(id);
		return new Response<T>(true, null);
	}
	
	@RequestMapping(value = "/updateByColumn/{table}/{column}/{id}/{value}/{type}", method = RequestMethod.POST)
	public @ResponseBody boolean updateByColumn(@PathVariable("id") Long id,@PathVariable("column") String column,@PathVariable("table") String table,@PathVariable("value") String value,@PathVariable("type") String type){
		Object newValue=null;
		if(type.equals("String"))
		{
			newValue=value;
		}
		else if(type.equals("date")){
			newValue= new Date(new Long(value));
		}
		else if(type.equals("number")){
			newValue= new Long(value);
		}
		getService().updateByColumn(table, id, column, newValue,type);
		return true;
	}
	
	
	/*  private TokenStore tokenStore;
	 
	 @Autowired
	 public GenericRest(TokenStore tokenStore) {
	     this.tokenStore = tokenStore;
	 }

	 // @PreAuthorize("#oauth2.hasScope('read')")
	  @RequestMapping(method = RequestMethod.GET, value = "/users/extra")
	  @ResponseBody
	  public Map<String, Object> getExtraInfo(OAuth2Authentication auth) {
	        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
	        final OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
	        System.out.println(accessToken);
	        return accessToken.getAdditionalInformation();
	    }*/
	
}
