package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*@Entity
@Table(name = "JOB_HISTORY")*/
public class JobHistory extends AbstractMultiTenantEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
/*	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_HISTORY_SEQ")
	@SequenceGenerator(name = "JOB_HISTORY_SEQ", sequenceName = "JOB_HISTORY_SEQ", allocationSize = 1)*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	@Column(name = "COMPLETE_SUCCESS")
	boolean completeSuccess;

	public boolean isCompleteSuccess() {
		return completeSuccess;
	}

	public void setCompleteSuccess(boolean completeSuccess) {
		this.completeSuccess = completeSuccess;
	}

}
