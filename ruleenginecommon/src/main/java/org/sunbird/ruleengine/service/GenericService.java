package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sunbird.ruleengine.common.AbstractObjectToEntityMapper;
import org.sunbird.ruleengine.common.PaginationHelper.PaginantorReturn;

public interface GenericService<T,U> {
	public boolean updateByColumn(String table, Long id, String column,
			Object value,String type);
	public void delete(Object user) ;
	public void save(T object) ;
	public T update(T object) ;
	public T get(BigInteger id) ;
	public T get(Long id) ;
	
	@Deprecated
	public PaginantorReturn search(U criteria, int firstResult,int maxResult);
	public List<T> getListByCriteria(U criteria, int firstResult, int maxResult);
	public List<T> getListByCriteria(U approval, int firstResult, int maxResult, Map<String, Boolean> orderBy, boolean distinct, Map<String, Set<String>> joinTableWithFieldsToLoad, String... fieldsToLoad );
	public Number getCount(U criteria, String query);
	/**
	 * This Method is @deprecated to make way for get {@link org.sunbird.ruleengine.service.GenericService#getArrayListByCriteria(Object, String, String, int, int)}. 
	 * 
	 * But even the  {@link org.sunbird.ruleengine.service.GenericService#getArrayListByCriteria(Object, String, String, int, int)} method should not be used and try to use 
	 * 
	 * {@link org.sunbird.ruleengine.service.GenericService#getArrayListByCriteria(Object, int, int, Map, boolean, Map, String[])}
	 *  
	 * */
	@Deprecated
	public List<T> getListByCriteria(U criteria, String query, String orderby, int firstResult, int maxResult, AbstractObjectToEntityMapper<List<T>>  mapper);
	public Long getCount(U criteria);
	 T getEntityByColumnNameAndValue(String filterFieldName,
				Object filterFieldValue) ;
	 
	 List<T> getListByColumnNameAndValue(String filterFieldName,
				Object filterFieldValue) ;
	Object getListByCriteria(U criteria, String query, String orderby,
			int firstResult, int maxResult);
	List<Object[]> getArrayListByCriteria(U criteria, String query, String orderBy, int firstResult, int maxResult);
	List<Object[]> getArrayListByCriteria(U criteria, int firstResult, int maxResult, Map<String, Boolean> orderBy,
			boolean distinct, Map<String, Set<String>> joinTableWithFieldsToLoad, String[] fieldsToLoad);
	
	/* public void error();*/
	
}
