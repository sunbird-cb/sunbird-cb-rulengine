package org.sunbird.ruleengine.service;

import java.util.List;

import org.sunbird.ruleengine.common.PaginationHelper.PaginantorReturn;
import org.sunbird.ruleengine.model.ApprovalHierarcy;



public interface ApprovalHierarcyService<T extends ApprovalHierarcy> {
	public void save(T user) ;
	public void update(T user) ;
	public void delete(Long user) ;
	public T get(Long id) ;
	public PaginantorReturn search(T role, int firstResult,int maxResult);
	public List<T> getListByCriteria(T bank, int firstResult, int maxResult);
	List<T> searchByCriteria(T approvalHierarcy,
			int firstResult, int maxResult);
}

