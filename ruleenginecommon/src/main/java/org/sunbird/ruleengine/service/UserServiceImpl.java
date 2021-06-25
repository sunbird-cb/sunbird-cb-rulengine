package org.sunbird.ruleengine.service;

import org.sunbird.ruleengine.dao.AbstractDAO;

public abstract class UserServiceImpl<T,U>
{
	public abstract AbstractDAO<T, U> getDAO();
	/**
	 * Checks whether the user for this client is having the permission to performa any 
	 * given task/function.
	 * 
	 * @author chirodip.p
	 * @since 1.1
	 */
	
	public boolean userInRole(Integer userId, Integer clientId, String FUNCTION_NAME)
	{
		return getDAO().userInRole(userId,clientId,FUNCTION_NAME);
	}
}
