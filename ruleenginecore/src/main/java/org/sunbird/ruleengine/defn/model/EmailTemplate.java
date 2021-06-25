package org.sunbird.ruleengine.defn.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@Entity
@Table(name="APPROVAL_EMAIL_TEMPLATE")
public class EmailTemplate extends AbstractMultiTenantEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="APPROVAL_EMAIL_TEMPLATE_SEQ")
	@SequenceGenerator(name="APPROVAL_EMAIL_TEMPLATE_SEQ", sequenceName="APPROVAL_EMAIL_TEMPLATE_SEQ", allocationSize=1)
	BigInteger id;

	@Override
	public BigInteger getId() {
		return id;
	}

	@Override
	public void setId(BigInteger id) {
		this.id = id;
	}
	
	@Column(name = "BODY")
	String body;
	
	@Column(name = "SUBJECT")
	String subject;
	
	@Column(name = "FROM_EMAIL")
	String fromEmail;
	
	@Column(name = "FROM_NAME")
	String fromName;
	
	@Column(name = "CODE")
	String code;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	
	
}
