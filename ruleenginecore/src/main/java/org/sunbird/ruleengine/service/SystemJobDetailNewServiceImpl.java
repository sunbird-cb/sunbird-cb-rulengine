package org.sunbird.ruleengine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.dao.SystemJobDetailNewDao;
import org.sunbird.ruleengine.model.SystemJobDetail;


@Service
@Transactional
public class SystemJobDetailNewServiceImpl  extends GenericServiceImpl<SystemJobDetail, SystemJobDetail>implements SystemJobDetailNewService{

	@Autowired
	SystemJobDetailNewDao systemJobDetialDAO;


	@Override
	public void save(SystemJobDetail SystemJobDetail) {
		systemJobDetialDAO.save(SystemJobDetail);	
	}


	@Override
	public SystemJobDetail update(SystemJobDetail SystemJobDetail) {
		return systemJobDetialDAO.update(SystemJobDetail);	
		
	}



	@Override
	public List<SystemJobDetail> getListByCriteria(SystemJobDetail systemJobDetail, int firstResult, int maxResult) {
		List<SystemJobDetail> systemJobDetails = systemJobDetialDAO.getListByCriteria(systemJobDetail, -1, 0);
		return systemJobDetails;
	}


	@Override
	public AbstractDAO<SystemJobDetail, SystemJobDetail> getDAO() {
		return systemJobDetialDAO;
		
	}
	
}
