package org.sunbird.ruleengine.service;

import org.sunbird.ruleengine.model.UserRoleJoin;
import org.sunbird.ruleengine.vo.UserRoleJoinVo;

import org.sunbird.ruleengine.service.AbstractUserRoleJoinService;

public interface UserRoleJoinService extends AbstractUserRoleJoinService<UserRoleJoin, UserRoleJoinVo> {


	void saveUserRole(UserRoleJoin t);
}
