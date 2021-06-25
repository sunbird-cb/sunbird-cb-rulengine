package org.sunbird.ruleengine.common;

import java.math.BigInteger;

public interface Auditable 
{
	public BigInteger getLastUpdatedBy();

	public void setLastUpdatedBy(BigInteger lastUpdatedBy) ;
	
	public BigInteger getClientId();

	
	public void setClientId(BigInteger clientId);

}
