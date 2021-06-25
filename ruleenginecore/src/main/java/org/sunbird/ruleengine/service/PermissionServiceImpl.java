package org.sunbird.ruleengine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.PermissionDao;
import org.sunbird.ruleengine.model.Permission;
import org.sunbird.ruleengine.vo.PermissionVo;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.AbstractPermissionServiceImpl;

@Service
@Transactional
public class PermissionServiceImpl extends AbstractPermissionServiceImpl<Permission, PermissionVo> implements PermissionService{

	@Autowired
	PermissionDao permissionDAO;

	@Override
	public AbstractDAO<Permission, PermissionVo> getDAO() {
		return permissionDAO;
	}

	public List<PermissionVo> getPermissions(String permission) {


		List<PermissionVo> permissionVo=permissionDAO.getPermissions(permission);
		return permissionVo;
	}

}
