package org.sunbird.ruleengine.dao;

public interface UserAbstractDAO 
{
	public boolean userInRole(Integer userId, Integer clientId, String FUNCTION_NAME);
}
