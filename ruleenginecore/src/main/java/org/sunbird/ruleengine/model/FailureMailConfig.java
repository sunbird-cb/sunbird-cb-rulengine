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
@Table(name= "FAILURE_MAIL_CONFIG")
public class FailureMailConfig extends AbstractMultiTenantEntity {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

@Id
@Column(name="ID")
@GeneratedValue(strategy = GenerationType.IDENTITY)
BigInteger id;

@Column(name= "JOB_DETAIL_ID")
BigInteger jobDetailId;

@Column(name= "NOT_IN_ERROR_PATTERN_ID")
BigInteger notInErrorPatternId;

public BigInteger getId() {
	return id;
}

public void setId(BigInteger id) {
	this.id = id;
}

public BigInteger getJobDetailId() {
	return jobDetailId;
}

public void setJobDetailId(BigInteger jobDetailId) {
	this.jobDetailId = jobDetailId;
}

public BigInteger getNotInErrorPatternId() {
	return notInErrorPatternId;
}

public void setNotInErrorPatternId(BigInteger notInErrorPatternId) {
	this.notInErrorPatternId = notInErrorPatternId;
}

}


