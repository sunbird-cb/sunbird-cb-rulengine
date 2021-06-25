package org.sunbird.ruleengine.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.sunbird.ruleengine.model.User;
import org.sunbird.ruleengine.vo.UserVo;

import org.sunbird.ruleengine.common.CommonUtil;
import org.sunbird.ruleengine.dao.AbstractUserDAO;

@Repository
public class UserDao extends AbstractUserDAO<User, UserVo>{
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		
		return entityManager;
	}

	@Override
	public Class<User> getClassType() {
		
		return User.class;
	}
	
	@Override
	protected Predicate[] getSearchPredicates(Root<User> root, UserVo example) {
		List<Predicate> predicates= new ArrayList<>();
		
		if(example.getId()!=null)
		{
			Path<Long> p=root.get("id");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getId()));
		}
		if(CommonUtil.isNotBlank(example.getEmail()))
		{
			Path<String> p=root.get("email");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			/*predicates.add(cb.equal(p, example.getEmail()));*/
			predicates.add(cb.like(cb.lower(p), "%"+example.getEmail().toLowerCase()+"%"));
		}
		
		if(CommonUtil.isNotBlank(example.getPassword()))
		{
			Path<Long> p=root.get("password");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getPassword()));
		}
		if(CommonUtil.isNotBlank(example.getFirstName()))
		{
			Path<String> p=root.get("firstName");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.like(cb.lower(p), "%"+example.getFirstName().toLowerCase()+"%"));
			/*predicates.add(cb.equal(p, example.getFirstName()));*/
		}
		if(CommonUtil.isNotBlank(example.getLastName()))
		{
			Path<String> p=root.get("lastName");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			//predicates.add(cb.equal(p, example.getLastName()));
			predicates.add(cb.like(cb.lower(p), "%"+example.getLastName().toLowerCase()+"%"));
		}
		if(CommonUtil.isNotBlank(example.getUserName()))
		{
			Path<String> p=root.get("userName");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getUserName()));
		}
		if(example.getClientId()!=null )
		{
			Path<String> p=root.get("clientId");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getClientId()));
					
		}
		/*if(CommonUtil.isNotBlank(example.getSecureToken()))
		{
			Path<String> p=root.get("secureToken");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getSecureToken()));
		}*/
		
		if(!example.isSearchInactiveAlso())
		{
			Path<Boolean> p=root.get("active");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, true));
			
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	@Override
	public String getCriteriaForVo(UserVo userVo) {
		String query="";
		
		if(userVo.getId()!= null)
		{
			query=query+"and (u.ID)= :id ";
		}
		if(userVo.getFirstName()!= null)
		{
			query=query+"and lower(u.FIRST_NAME) like :firstName ";
			
		}
		if(userVo.getLastName()!= null)
		{
			query=query+"and lower(u.LAST_NAME) like :lastName ";
			
		}
		if(userVo.getEmail()!= null)
		{
			query=query+"and lower(u.EMAIL) like :email ";
			
		}
		/*if(userVo.getSecureToken()!= null)
		{
			query=query+"and (u.SECURE_TOKEN) like :secureToken ";
			
		}*/
		if(userVo.getUserName()!= null)
		{
			query=query+"and (u.USER_NAME) like :userName ";
			
		}
		if(!userVo.isSearchInactiveAlso())
		{
			query=query+"and (usr.ACTIVE) = :ACTIVE ";
			
		}
		
		return query;
	}
	
	@Override
	public void setBindParameterForVo(Query queryJpa, UserVo userVo) {
		
		if(userVo.getId()!=null)
		{
		 queryJpa.setParameter("id",userVo.getId());
		}
		if(userVo.getFirstName()!=null)
		{
		 queryJpa.setParameter("firstName","%"+userVo.getFirstName().toLowerCase()+"%");
		}
		if(userVo.getLastName()!=null)
		{
		 queryJpa.setParameter("lastName","%"+userVo.getLastName().toLowerCase()+"%");
		}
		if(userVo.getEmail()!=null)
		{
		 queryJpa.setParameter("email","%"+userVo.getEmail().toLowerCase()+"%");
		}
		/*if(userVo.getSecureToken()!=null)
		{
		 queryJpa.setParameter("secureToken",userVo.getSecureToken());
		}*/
		if(userVo.getUserName()!=null)
		{
		 queryJpa.setParameter("userName",userVo.getUserName());
		}
		if(!userVo.isSearchInactiveAlso())
		{
			 queryJpa.setParameter("ACTIVE","1");
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public UserVo getUserByEmailId(String email)
	{
		
	
		List<Object[]> list = new ArrayList<>();
		
		String query="select u.ID,u.FIRST_NAME,u.LAST_NAME,u.USER_NAME,u.EMAIL FROM USER_ u where u.EMAIL=?1 and u.ACTIVE='1'";
	
		list=entityManager.createNativeQuery
        (query).setParameter(1, email).getResultList();
		
	
			UserVo userVo= new UserVo();
			for (Object[] object : list) {
			userVo.setId(((BigDecimal)object[0]).toBigInteger());
			userVo.setFirstName((String)object[1]);
			userVo.setLastName((String)object[2]);
			userVo.setUserName((String)object[3]);
			userVo.setEmail((String)object[4]);
	   }
			return userVo;
    }

	// after login get user details using username
	@SuppressWarnings("unchecked")
	public UserVo getUserByUsername(String userName)
	{
		List<Object[]> list = new ArrayList<>();
		
		String query="select u.ID,u.FIRST_NAME,u.LAST_NAME,u.USER_NAME,u.EMAIL,u.CLIENT_ID FROM USER_ u where u.USER_NAME=?1 and u.ACTIVE='1'";
	
		list=entityManager.createNativeQuery
        (query).setParameter(1, userName).getResultList();

			UserVo userVo= new UserVo();
			for (Object[] object : list) {
			userVo.setId((BigInteger)object[0]);
			userVo.setFirstName((String)object[1]);
			userVo.setLastName((String)object[2]);
			userVo.setUserName((String)object[3]);
			userVo.setEmail((String)object[4]);
			userVo.setClientId(new BigDecimal((Integer)object[5]));
	   }
			return userVo;
    }

	public boolean hasPermission(BigInteger userId, String permissionName){
		
		
		BigDecimal list = (BigDecimal) entityManager.createNativeQuery("select count(permission.id) from PERMISSION permission left join ROLE_PERMISSION_JOIN rpj on rpj.PERMISSION_ID=permission.ID left join USER_ROLE_JOIN r on r.ROLE_ID=rpj.ROLE_ID where  permission.permission_name= '"+  permissionName + "' and r.USER_ID= " + userId + " ").getSingleResult();		  
		if(list.compareTo(BigDecimal.ZERO) == 0)
			return false;
		else 
			return true;
		

	}

}
