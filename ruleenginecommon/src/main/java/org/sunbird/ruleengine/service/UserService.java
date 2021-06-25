package org.sunbird.ruleengine.service;

public interface UserService<T,U> extends GenericService<T, U>
{
	public boolean userInRole(Integer userId, Integer clientId, String FUNCTION_NAME);
}
