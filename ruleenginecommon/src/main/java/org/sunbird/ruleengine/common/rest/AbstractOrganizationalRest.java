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
import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.common.NotAuthenticatedException;
import org.sunbird.ruleengine.common.NotAuthorizedException;
import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.model.AbstractOrgnizationableEntity;
import org.sunbird.ruleengine.service.AbstractClientService;
import org.sunbird.ruleengine.service.GenericService;

public abstract class AbstractOrganizationalRest<T extends AbstractOrgnizationableEntity, U extends AbstractOrgnizationableEntity>
		extends GenericMultiTenantRoleBasedSecuredRest<T, U> {
	private static final Logger logger = LogManager.getLogger(AbstractOrganizationalRest.class);
	private Class<T> t;
	private Class<U> u;

	public abstract GenericService<T, U> getService();

	public abstract GenericService<T, U> getUserService();

	public abstract AbstractClientService<?, ?> getClientService();

	public abstract String rolePrefix();

	public AbstractOrganizationalRest() {
	}

	public AbstractOrganizationalRest(Class<T> t, Class<U> u) {
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
		if (isGetAuthenticated() && principal == null) {
			throw new NotAuthenticatedException();
		}
		if (isGetAuthorized() && getService()
				.getCount(instantiateCriteria(), getAuthorizationQueryForGet(id, principal)).longValue() == 0) {
			throw new NotAuthorizedException();
		}
		return getService().get(id);

	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<T> save(@PathVariable("clientCode") String clientCode, @RequestBody T t,
			Principal principal) {
		saveAuthenticationCheck(principal);
		if (permissionForSave()!=null && getService()
				.getCount(instantiateCriteria(), getAuthorizationQuery(permissionForSave(), t.getOrgId(), principal)).longValue() == 0) {
			throw new NotAuthorizedException();
		}
		if (principal != null) {
			t.setCreatedBy(new BigInteger(principal.getName()));
			t.setLastUpdatedBy(new BigInteger(principal.getName()));
		}
		t.setCreationDate(new java.util.Date());
		t.setLastUpdateDate(new java.util.Date());
		t.setClientId(getClientIdByClientCode(clientCode));
		validateSave(t, principal);
		getService().save(t);
		return new Response<T>(true, t);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody Response<T> update(@PathVariable("clientCode") String clientCode, @RequestBody T t,
			Principal principal) {

		updateAuthenticationCheck(principal);
		if (permissoinForUpdate() !=null && getService()
				.getCount(instantiateCriteria(), getAuthorizationQuery(permissoinForUpdate(), t.getOrgId(), principal)).longValue() == 0) {
			throw new NotAuthorizedException();
		}
		if (principal != null) {
			t.setLastUpdatedBy(new BigInteger(principal.getName()));
			t.setLastUpdateDate(new java.util.Date());
		}
		t.setClientId(getClientIdByClientCode(clientCode));
		validateUpdate(t, principal);
		getService().update(t);
		return new Response<T>(true, t);
	}

	protected void updateAuthenticationCheck(Principal principal) {
		if (isUpdateAuthenticated() && principal == null) {
			throw new NotAuthenticatedException();
		}
	}
	
	protected void saveAuthenticationCheck(Principal principal) {
		if (isSaveAuthenticated() && principal == null) {
			throw new NotAuthenticatedException();
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<T> getListByCriteria(@PathVariable("clientCode") String clientCode, @ModelAttribute U t,
			@RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		if (isSearchAuthenticated() && principal == null) {
			throw new NotAuthenticatedException();
		}
		if (permissionForSearch()!=null && getService()
				.getCount(instantiateCriteria(), getAuthorizationQuery(permissionForSearch(), t.getOrgId(), principal)).longValue() == 0) {
			throw new NotAuthorizedException();
		}
		t.setClientId(getClientIdByClientCode(clientCode));
		return getService().getListByCriteria(t, firstResult, maxResult);
	}

	@Deprecated
	@RequestMapping(value = "/searchWithQuery", method = RequestMethod.GET)
	public final @ResponseBody List<T> getListByCriteriaWithQuery(@PathVariable("clientCode") String clientCode,
			@ModelAttribute U t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		if (isSearchAuthenticated() && principal == null) {
			throw new NotAuthenticatedException();
		}
		if (permissionForSearch() !=null &&  getService()
				.getCount(instantiateCriteria(), getAuthorizationQuery(permissionForSearch(), t.getOrgId(), principal)).longValue() == 0) {
			throw new NotAuthorizedException();
		}
		t.setClientId(getClientIdByClientCode(clientCode));
		return getService().getListByCriteria(t, getSearchQuery(), getSearchOrderBy(), firstResult, maxResult,
				getMapper());
	}
	
	@RequestMapping(value = "/searchWithQueryObjectArray", method = RequestMethod.GET)
	public @ResponseBody Object searchWithQueryObjectArrayObjectArray(@PathVariable("clientCode") String clientCode,
			@ModelAttribute U t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		if (isSearchAuthenticated() && principal == null) {
			throw new NotAuthenticatedException();
		}
		if (permissionForSearch() !=null &&  getService()
				.getCount(instantiateCriteria(), getAuthorizationQuery(permissionForSearch(), t.getOrgId(), principal)).longValue() == 0) {
			throw new NotAuthorizedException();
		}
		t.setClientId(getClientIdByClientCode(clientCode));
		return getService().getListByCriteria(t, getSearchQuery(), getSearchOrderBy(), firstResult, maxResult);
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Long count(@PathVariable("clientCode") String clientCode, @ModelAttribute U t,
			Principal principal) {
		t.setClientId(getClientIdByClientCode(clientCode));
		return getService().getCount(t);
	}

	@RequestMapping(value = "/deleteWithBiginteger/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Response<T> deleteWithBiginteger(@PathVariable("clientCode") String clientCode,
			@PathVariable("id") BigInteger id, Principal principal) {

		T object = getService().get(id);

		if (isDeleteAuthenticated() && principal == null) {
			throw new NotAuthenticatedException();
		}
		if (permissionForDelete() != null
				&& getService()
						.getCount(instantiateCriteria(),
								getAuthorizationQuery(permissionForDelete(), object.getOrgId(), principal))
						.longValue() == 0) {
			throw new NotAuthorizedException();
		}

		getService().delete(id);

		return new Response<T>(true, null);
	}

	public String getTableForUpdateByColumn() {
		return null;
	}

	public String getAuthorizationQuery(String permission, BigInteger orgId, Principal principal) {
		return "select count(id) from " + getUserTable()
				+ " usr inner join user_role_join ur on usr.id=ur.user_id inner join role_permission_join rp on rp.role_id=ur.role_id inner join permission pr on pr.id=rp.permission_id where pr.permission_name="
				+ getPermissionName() + " and usr.id=" + principal.getName() + " and ur.ORG_ID='" + orgId + "' ";
	};

	public String permissoinForUpdate() {
		return null;
	}

	public String permissionForSave() {
		return null;
	}

	public String permissionForDelete() {
		return null;
	}

	public String permissionForGet() {
		return null;
	}

	public String permissionForSearch() {
		return null;
	}

	public boolean isUpdateAuthenticated() {
		return true;
	}

	public boolean isSaveAuthenticated() {
		return true;
	}

	public boolean isDeleteAuthenticated() {
		return true;
	}

	public boolean isGetAuthenticated() {
		return false;
	}

	public boolean isSearchAuthenticated() {
		return false;
	}

	public String getUserTable() {
		return "user_";
	};

	public String getSearchQuery() {
		return null;
	};

	public String getSearchOrderBy() {
		return "";
	};

	public AbstractObjectToEntityMapper<List<T>> getMapper() {
		return null;
	};

	public void validateSave(T t, Principal principal) {
	};

	public void validateUpdate(T t, Principal principal) {
	};

	public BigInteger getClientIdByClientCode(String clientCode) {
		return getClientService().getClientCodeById(clientCode,null);
	}

}
