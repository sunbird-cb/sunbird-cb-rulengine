package org.sunbird.ruleengine.dao;

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
import org.sunbird.ruleengine.model.Notification;
import org.sunbird.ruleengine.vo.NotificationVo;

@Repository
public class NotificationDAO extends AbstractNotificationDAO<Notification, NotificationVo> {
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		
		return entityManager;
	}

	@Override
	public Class<Notification> getClassType() {
		
		return Notification.class;
	}

	public Integer getNoOfNotificationCount(BigInteger userId) {	
		return ((Number) entityManager.createNativeQuery("select count(ID)  FROM NOTIFICATION where NOTIFY_TO=?  and (notified is null or notified=0)").setParameter(1, userId).getSingleResult()).intValue();
	}

	@Override
	protected Predicate[] getSearchPredicates(Root<Notification> root,
			NotificationVo example) {
		List<Predicate> predicates= new ArrayList<>();
		
		if(example.getId()!=null)
		{
			Path<String> p=root.get("id");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getId()));
		}
		
		if(example.getNotifyTo()!=null)
		{
			Path<String> p=root.get("notifyTo");
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			predicates.add(cb.equal(p, example.getNotifyTo()));
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public String getCriteriaForVo(NotificationVo criteria) {
		String query="";
		if(criteria.getNotifyTo()!= null)
		{
			query=query+" and (notification.NOTIFY_TO)= :notify ";
			//query=query+"and lower(registration.REGISTRATION_NO) like :registrationNo ";
		}
		if(criteria.getId()!= null)
		{
			query=query+" and (notification.ID)= :id ";
			//query=query+"and lower(registration.REGISTRATION_NO) like :registrationNo ";
		}
		return query;
	}

	@Override
	public void setBindParameterForVo(Query queryJpa, NotificationVo criteria) {
		if(criteria.getNotifyTo()!=null)
		{
		 queryJpa.setParameter("notify",criteria.getNotifyTo());
		}
		if(criteria.getId()!=null)
		{
		 queryJpa.setParameter("id",criteria.getId());
		}
	}

	
	
	
}
