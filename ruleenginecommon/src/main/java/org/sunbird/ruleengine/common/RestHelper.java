package org.sunbird.ruleengine.common;

import java.math.BigInteger;
import java.security.Principal;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

public class RestHelper {

	public static void setPrincipalDetails(AbstractMultiTenantEntity t,
			Principal principal) {
		t.setCreatedBy(new BigInteger(principal.getName()));
		t.setLastUpdatedBy(new BigInteger(principal.getName()));
		t.setCreationDate(new java.util.Date());
		t.setLastUpdateDate(new java.util.Date());

	}
	
	public static void setPrincipalDetails(AbstractMultiTenantEntity t,
			BigInteger loggedInUserId ) {
		t.setCreatedBy(loggedInUserId);
		t.setLastUpdatedBy(loggedInUserId);
		t.setCreationDate(new java.util.Date());
		t.setLastUpdateDate(new java.util.Date());

	}

}
