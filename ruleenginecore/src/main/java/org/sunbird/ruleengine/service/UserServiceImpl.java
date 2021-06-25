package org.sunbird.ruleengine.service;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.UserDao;
import org.sunbird.ruleengine.model.User;
import org.sunbird.ruleengine.vo.UserVo;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.AbstractUserServiceImpl;

@Service(value = "userService")
@Transactional
public class UserServiceImpl extends AbstractUserServiceImpl<User, UserVo> implements UserService{

	@Autowired
	UserDao userDAO;

	
	@Override
	public AbstractDAO<User, UserVo> getDAO() {
		return userDAO;
	}

	@Override
	public UserVo getUserByEmailId(String email) {

		UserVo user= userDAO.getUserByEmailId(email);

		return user;
	}

	@Override
	public UserVo getUserByUsername(String userName) {

		UserVo user= userDAO.getUserByUsername(userName);

		return user;
	}
	@Override
	public boolean hasPermission(BigInteger userId, String permissionName){
		return userDAO.hasPermission(userId, permissionName);


	}

	
}
