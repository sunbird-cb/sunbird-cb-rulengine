package org.sunbird.ruleengine.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.sunbird.ruleengine.model.SystemJobDetail;

public interface SystemJobDetailService {

	public void save(SystemJobDetail jobDetail);

	public SystemJobDetail update(SystemJobDetail jobDetail);

	public SystemJobDetail get(String id);

	public void startCron(ApplicationContext applicationContext);
	
	public void runCron(List<SystemJobDetail> systemJobDetails);

	public List<SystemJobDetail> searchByCriteria(SystemJobDetail applicationProperties, int firstResult, int maxResult);
}