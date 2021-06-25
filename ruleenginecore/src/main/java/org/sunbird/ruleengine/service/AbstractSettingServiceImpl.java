package org.sunbird.ruleengine.service;

import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.sunbird.ruleengine.model.AbstractSettings;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
public abstract class AbstractSettingServiceImpl<T extends AbstractSettings, U extends AbstractSettings>
extends GenericServiceImpl<T, U> implements AbstractSettingService<T, U> {

	@Override
	public String getValue(BigInteger clientId, String key) {
		U u = intantiatateVo();
		u.setClientId(clientId);
		u.setKey(key);
		if(getDAO().getListByCriteria(u, 0, 1).size()>0)
		{
			return getDAO().getListByCriteria(u, 0, 1).get(0).getValue();	
		}
		return "";
	}

	@Override
	public boolean equal(BigInteger clientId, String key, String value) {
		U u = intantiatateVo();
		u.setClientId(clientId);
		u.setKey(key);
		u.setValue(value);
		return getDAO().getCount(u) > 0;
	}

	protected abstract U intantiatateVo();

	@Override
	public abstract AbstractDAO<T, U> getDAO();

}
