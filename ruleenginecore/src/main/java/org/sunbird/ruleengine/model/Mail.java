package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@Entity
@Table(name = "MAIL")
public class Mail extends AbstractMultiTenantEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
/*	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_DETAIL_SEQ")
	@SequenceGenerator(name = "JOB_DETAIL_SEQ", sequenceName = "JOB_DETAIL_SEQ", allocationSize = 1)*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "FROM_MAIL")
	private String fromMail;

	@Column(name = "TO_MAIL")
	private String toMail;

	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "HEADER")
	private String header;
	
	@Column(name = "JOB_DETAIL_ID")
	private BigInteger jobDetailId;
	
	

	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public String getToMail() {
		return toMail;
	}

	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public BigInteger getId() {
		return id;
	}

	@Override
	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getJobDetailId() {
		return jobDetailId;
	}

	public void setJobDetailId(BigInteger jobDetailId) {
		this.jobDetailId = jobDetailId;
	}
	
	

}
