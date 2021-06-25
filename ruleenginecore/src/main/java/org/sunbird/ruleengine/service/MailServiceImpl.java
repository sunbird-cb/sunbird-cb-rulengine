package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.MailDao;
import org.sunbird.ruleengine.model.Mail;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class MailServiceImpl extends GenericServiceImpl<Mail, Mail> implements MailService {

	@Autowired
	MailDao mailDao;

	@Override
	public AbstractDAO<Mail, Mail> getDAO() {
		return mailDao;
	}

	@Override
	public List<Mail> getListByDate(Mail mail, int firstResult, int maxResult, Date startDate, Date endDate) {
		EntityManager entityManager = mailDao.getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Mail> q = cb.createQuery(Mail.class);
		Root<Mail> c = q.from(Mail.class);
		q.select(c);
		q.where(cb.equal(c.get("clientId").as(BigInteger.class), mail.getClientId()),
				cb.between(c.get("creationDate").as(Date.class), startDate, endDate),
				cb.equal(c.get("jobDetailId"), mail.getJobDetailId())
		);
		TypedQuery<Mail> tq = entityManager.createQuery(q);
		tq.setFirstResult(firstResult);
		tq.setMaxResults(firstResult);
		List<Mail> mailList = tq.getResultList();
		return mailList;
	}

}
