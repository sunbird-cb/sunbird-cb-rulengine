package org.sunbird.ruleengine.common.rest;

import org.sunbird.ruleengine.model.AbstractUser;

public abstract class AbstractUserRest<T extends AbstractUser, U extends AbstractUser> extends GenericMultiTenantRoleBasedSecuredRest<T, U>{

	public AbstractUserRest(Class<T> class1,
			Class<U> class2) {
		super(class1,class2);
	}
	
	
	
}
