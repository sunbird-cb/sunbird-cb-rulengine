package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.common.PaginationHelper.PaginantorReturn;
import org.sunbird.ruleengine.dao.AbstractDAO;

@Transactional
public abstract class GenericServiceImpl<T, U> implements GenericService<T, U> {

	@Override
	public Number getCount(U approval, String query) {
		return getDAO().getCount(approval, query);
	}

	@Override
	public List<T> getListByCriteria(U criteria, String query, String orderby, int firstResult, int maxResult,
			AbstractObjectToEntityMapper<List<T>> mapper) {
		return getDAO().getListByCriteria(criteria, query, orderby, firstResult, maxResult, mapper);
	}

	@Override
	public Object getListByCriteria(U criteria, String query, String orderby, int firstResult, int maxResult) {
		return getDAO().getListByCriteria(criteria, query, orderby, firstResult, maxResult);
	}

	@Override
	public boolean updateByColumn(String table, Long id, String column, Object value, String type) {
		return getDAO().updateByColumn(table, id, column, value, type);

	}

	public abstract AbstractDAO<T, U> getDAO();

	public void save(T t) {
		getDAO().save(t);

	}

	public T update(T t) {
		return getDAO().update(t);
	}

	public void delete(Object id) {
		getDAO().delete(id);
	}

	public T get(BigInteger id) {
		return getDAO().getById(id);
	}

	public T get(Long id) {
		return getDAO().getById(id);
	}

	public PaginantorReturn search(U approval, int firstResult, int maxResult) {
		List<T> list = getDAO().getListByCriteria(approval, firstResult, maxResult, null, false, null, (String[]) null);
		Long i = getDAO().getCount(approval);
		return new PaginantorReturn(i, list);
	}

	public List<T> getListByCriteria(U approval, int firstResult, int maxResult) {
		return getDAO().getListByCriteria(approval, firstResult, maxResult, null, false, null, (String[]) null);
	}

	public List<T> getListByCriteria(U approval, String query, int firstResult, int maxResult) {
		return getDAO().getListByCriteria(approval, firstResult, maxResult, null, false, null, (String[]) null);
	}

	public List<T> getListByCriteria(U approval, int firstResult, int maxResult, Map<String, Boolean> orderBy,
			boolean distinct, Map<String, Set<String>> joinTableWithFieldsToLoad, String... fieldsToLoad) {
		return getDAO().getListByCriteria(approval, firstResult, maxResult, orderBy, distinct,
				joinTableWithFieldsToLoad, fieldsToLoad);
	}

	public Long getCount(U approval) {
		return getDAO().getCount(approval);
	}

	@Override
	public T getEntityByColumnNameAndValue(String filterFieldName, Object filterFieldValue) {
		return getDAO().getEntityByColumnNameAndValue(filterFieldName, filterFieldValue);
	}
	@Override
	public List<Object[]> getArrayListByCriteria(U criteria, String query, String orderBy, int firstResult,
			int maxResult) {
		return getDAO().getArrayListByCriteria(criteria, query, orderBy, firstResult, maxResult);
	}
	@Override
	public  List<Object[]> getArrayListByCriteria(U criteria, int firstResult,
			int maxResult, Map<String, Boolean> orderBy, boolean distinct,
			Map<String, Set<String>> joinTableWithFieldsToLoad,
			String... fieldsToLoad) 
	{
		return  getDAO().getArrayListByCriteria(criteria, firstResult,maxResult, orderBy,distinct,joinTableWithFieldsToLoad, fieldsToLoad);
	}

	@Override
	public List<T> getListByColumnNameAndValue(String filterFieldName,
			Object filterFieldValue) {
		return getDAO().getListByColumnNameAndValue(filterFieldName, filterFieldValue);
	}
	

}
