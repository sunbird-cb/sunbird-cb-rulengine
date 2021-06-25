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
@Table(name = "STEP_SETTINGS")
@AttributeOverride(name="key", column=@Column(name="KEY_"))
public class StepSettings extends AbstractSettings implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENT_SETTINGS_SEQ")
	@SequenceGenerator(name = "CLIENT_SETTINGS_SEQ", sequenceName = "CLIENT_SETTINGS_SEQ", allocationSize = 1)*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "STEP_ID")
	private BigInteger stepId;
	
	@Column(name = "ENCRYPTED")
	private String 	encrypted;
	
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getStepId() {
		return stepId;
	}

	public void setStepId(BigInteger stepId) {
		this.stepId = stepId;
	}

	public String getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}

}
