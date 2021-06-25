package org.sunbird.ruleengine.service;

import java.math.BigInteger;

import org.sunbird.ruleengine.model.User;
import org.sunbird.ruleengine.vo.UserVo;

import org.sunbird.ruleengine.service.AbstractUserService;

public interface UserService extends AbstractUserService<User, UserVo>{

	public UserVo getUserByEmailId(String email);

	public UserVo getUserByUsername(String userName);

	public boolean hasPermission(BigInteger userId, String functionName);
}
