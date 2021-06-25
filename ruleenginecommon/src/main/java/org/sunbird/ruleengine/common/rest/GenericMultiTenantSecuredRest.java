package org.sunbird.ruleengine.common.rest;

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
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.service.GenericService;

public abstract class GenericMultiTenantSecuredRest<T, U> {
	private static final Logger logger = LogManager.getLogger(GenericMultiTenantSecuredRest.class);
	private Class<T> t;
	private Class<U> u;

	public abstract GenericService<T, U> getService();

	public abstract GenericService<T, U> getUserService();

	public abstract String rolePrefix();

	public GenericMultiTenantSecuredRest() {
	}

	public GenericMultiTenantSecuredRest(Class<T> t, Class<U> u) {
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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody T get(@PathVariable("clientCode") String clientCode, @PathVariable("id") BigInteger id,
			Principal principal) {
		return getService().get(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<T> save(@PathVariable("clientCode") String clientCode, @RequestBody T t,
			Principal principal) {

		getService().save(t);
		return new Response<T>(true, t);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody Response<T> update(@PathVariable("clientCode") String clientCode, @RequestBody T t,
			Principal principal) {
		getService().update(t);
		return new Response<T>(true, t);
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<T> getListByCriteria(@PathVariable("clientCode") String clientCode, @ModelAttribute U t,
			@RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		return getService().getListByCriteria(t, firstResult, maxResult);
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute U t,
			Principal principal) {
		return getService().getCount(t);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Response<T> delete(@PathVariable("clientCode") String clientCode,
			@PathVariable("id") BigInteger id, Principal principal) {
		getService().delete(id);
		return new Response<T>(true, null);
	}

}
