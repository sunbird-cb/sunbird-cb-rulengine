package org.sunbird.ruleengine.service;

import java.util.Date;
import java.util.List;

import org.sunbird.ruleengine.model.Mail;

import org.sunbird.ruleengine.service.GenericService;

public interface MailService extends GenericService<Mail, Mail> {

	public List<Mail> getListByDate(Mail mail, int firstResult, int maxResult, Date startDate, Date endDate);

}
