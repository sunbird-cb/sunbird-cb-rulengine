package org.sunbird.ruleengine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.sunbird.ruleengine.common.PaginationHelper.PaginantorReturn;
import org.sunbird.ruleengine.dao.ApprovalDAO;
import org.sunbird.ruleengine.model.Approval;

public abstract class ApproavalServiceImp<T extends Approval,U extends Approval> implements ApprovalService<T,U>{

	@Autowired
	ApprovalDAO<T,U> approvalDAO;



	public void save(T t) {
		
		approvalDAO.save(t);
	}

	
	public void update(T t) {
		
		approvalDAO.update(t);
	}


	public T get(Long id) {
		
		return approvalDAO.getById(id);
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
			T approval, int firstResult,
			int maxResult) {
		
		return approvalDAO.getListByCriteria(approval, firstResult, maxResult);
	}
	

	public void delete(Long user) {
		approvalDAO.delete(user);
		
	}


	public PaginantorReturn search(U approval, int firstResult, int maxResult) {
		
		return null;
	}
	
	
	
	
}
