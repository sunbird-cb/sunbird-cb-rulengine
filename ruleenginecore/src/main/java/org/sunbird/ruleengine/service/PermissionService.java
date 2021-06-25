package org.sunbird.ruleengine.service;

import java.util.List;

import org.sunbird.ruleengine.model.Permission;
import org.sunbird.ruleengine.vo.PermissionVo;

import org.sunbird.ruleengine.service.AbstractPermissionService;

public interface PermissionService extends AbstractPermissionService<Permission, PermissionVo> {
	public List<PermissionVo> getPermissions(String permissions);
}
