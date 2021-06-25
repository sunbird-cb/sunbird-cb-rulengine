package org.sunbird.ruleengine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.sunbird.ruleengine.common.PaginationHelper.PaginantorReturn;
import org.sunbird.ruleengine.dao.ApprovalHierarcyDAO;
import org.sunbird.ruleengine.model.ApprovalHierarcy;



public  abstract class ApprovalHierarcyServiceImp<T extends ApprovalHierarcy,U extends ApprovalHierarcy> implements ApprovalHierarcyService<T>{


	@Autowired
	ApprovalHierarcyDAO<T,U> approvalHierarcyDAO;


	public void save(T t) {
		
		approvalHierarcyDAO.save(t);
	}

	public void update(T t) {
		approvalHierarcyDAO.update(t);
	}

	public T get(Long id) {
		return approvalHierarcyDAO.getById(id);
	}

	/*@Override
	public PaginantorReturn search(Bank bank, int firstResult,
			int maxResult) {
	
		List<AdvancePaymentDetails> list = bankDAO.getListByCriteria(bank,
				firstResult, maxResult);
		Long count = bankDAO.getCountByCriteria(bank);
		
		return  PaginationHelper.convertForPagination(count, list);
	}*/

	public List<T> getListByCriteria( 
			T bank, int firstResult,
			int maxResult) {
		
		return approvalHierarcyDAO.getListByCriteria(bank, firstResult, maxResult);
	}
	
	public void delete(Long user) {
		approvalHierarcyDAO.delete(user);
		
	}

	public PaginantorReturn search(T role, int firstResult, int maxResult) {
		
		return null;
	}
	
}