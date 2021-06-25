package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.RoleDao;
import org.sunbird.ruleengine.model.Role;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;


@Service
@Transactional
public class RoleServiceImpl extends GenericServiceImpl<Role, Role> implements RoleService{

	@Autowired
	RoleDao roleDao;
	
	@Override
	public AbstractDAO<Role, Role> getDAO() {
		return roleDao;
	}

}
