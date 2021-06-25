package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.sunbird.ruleengine.model.ErrorPatternMaster;
import org.sunbird.ruleengine.model.IntegrationInstanceFailure;

import org.sunbird.ruleengine.service.GenericService;

public interface IntegrationInstanceFailureService extends GenericService<org.sunbird.ruleengine.model.IntegrationInstanceFailure, org.sunbird.ruleengine.model.IntegrationInstanceFailure> {
	
	
	public List<IntegrationInstanceFailure> getIntegrationInstanceFailureWithJobDetail(IntegrationInstanceFailure criteria,Date startDate, Date endDate,String jobDetailIds,int firstResult, int MaxResult,List<Object[]> notInErrorpatternIds);
	
	public ErrorPatternMaster getFailureErrorPattern(BigInteger clientId,String errorResponse);
}
 