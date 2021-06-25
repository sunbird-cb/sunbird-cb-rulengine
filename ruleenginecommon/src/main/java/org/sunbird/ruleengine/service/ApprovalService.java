package org.sunbird.ruleengine.service;

import java.util.List;

import org.sunbird.ruleengine.common.PaginationHelper.PaginantorReturn;
import org.sunbird.ruleengine.model.Approval;

public interface ApprovalService <T extends Approval, U extends Approval> {
	public void save(T user) ;
	public void update(T user) ;
	public void delete(Long user) ;
	public T get(Long id) ;
	public PaginantorReturn search(U approval, int firstResult,int maxResult);
	public List<T> getListByCriteria(T approval, int firstResult, int maxResult);
	List<T> searchByCriteria(T approval,
			int firstResult, int maxResult);
}
