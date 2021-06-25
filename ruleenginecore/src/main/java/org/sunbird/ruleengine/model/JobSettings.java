package org.sunbird.ruleengine.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JOB_SETTINGS")
@AttributeOverride(name="key", column=@Column(name="KEY_"))
public class JobSettings extends AbstractSettings implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENT_SETTINGS_SEQ")
	@SequenceGenerator(name = "CLIENT_SETTINGS_SEQ", sequenceName = "CLIENT_SETTINGS_SEQ", allocationSize = 1)*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "JOB_DETAIL_ID")
	private BigInteger jobDetailId;

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
