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
@Table(name = "MAIL_SETTINGS")
public class MailSettings extends AbstractMultiTenantEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "JOB_DETAIL_SEQ")
	// @SequenceGenerator(name = "JOB_DETAIL_SEQ", sequenceName =
	// "JOB_DETAIL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "JOB_DETAIL_ID")
	private BigInteger jobDetailId;

	@Column(name = "MAIL_INTERVAL")
	private Integer mailInterval;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getMailInterval() {
		return mailInterval;
	}

	public void setMailInterval(Integer mailInterval) {
		this.mailInterval = mailInterval;
	}

}
