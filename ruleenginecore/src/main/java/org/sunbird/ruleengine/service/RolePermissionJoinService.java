package org.sunbird.ruleengine.service;

import org.sunbird.ruleengine.model.RolePermissionJoin;
import org.sunbird.ruleengine.vo.RolePermissionJoinVo;

import org.sunbird.ruleengine.service.AbstractRolePermissionJoinService;

public interface RolePermissionJoinService extends AbstractRolePermissionJoinService<RolePermissionJoin,RolePermissionJoinVo>{


	void saveRolePermission(RolePermissionJoin t);
}
