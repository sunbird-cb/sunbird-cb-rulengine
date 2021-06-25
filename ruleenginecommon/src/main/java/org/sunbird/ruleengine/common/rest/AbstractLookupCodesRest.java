package org.sunbird.ruleengine.common.rest;

import org.sunbird.ruleengine.model.AbstractLookupCodes;

public abstract class AbstractLookupCodesRest<T extends AbstractLookupCodes, U extends AbstractLookupCodes> extends GenericMultiTenantRoleBasedSecuredRest<T, U> {

	public AbstractLookupCodesRest(Class<T> class1,
			Class<U> class2) {
		super(class1,class2);
	}
	

	@Override
	public String getSearchQuery() {
		
		return "select lookupCodes.ID,lookupCodes.LOOKUP_CODE,lookupCodes.MEANING,lookupCodes.CLIENT_ID,lookupCodes.LOOKUP_TYPE,lookupCodes.DESCRIPTION,lookupCodes.LANGUAGE,lookupCodes.ENABLED_FLAG from LOOKUP_CODES lookupCodes  where 1=1 ";
	}

	@Override
	public String getSearchOrderBy() {
		
		return " order by lookupCodes.id desc";
	}
	
}
