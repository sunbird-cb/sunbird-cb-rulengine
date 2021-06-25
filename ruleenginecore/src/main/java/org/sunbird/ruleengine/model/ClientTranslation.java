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
@Table(name = "CLIENT_TRANSLATION")
public class ClientTranslation extends AbstractMultiTenantEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "STEP_SEQ")
	//
	// @SequenceGenerator(name = "STEP_SEQ", sequenceName = "STEP_SEQ",
	// allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "CODE")
	private String code;

	@Column(name = "_KEY")
	private String key;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "KEY_DESCRIPTION")
	private String keyDescription;

	@Column(name = "VALUE_DESCRIPTION")
	private String valueDescription;

	@Column(name = "RECIPROCAL")
	private boolean reciprocal=false;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public BigInteger getId() {
		return id;
	}

	@Override
	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getKeyDescription() {
		return keyDescription;
	}

	public void setKeyDescription(String keyDescription) {
		this.keyDescription = keyDescription;
	}

	public String getValueDescription() {
		return valueDescription;
	}

	public void setValueDescription(String valueDescription) {
		this.valueDescription = valueDescription;
	}

	public boolean isReciprocal() {
		return reciprocal;
	}

	public void setReciprocal(boolean reciprocal) {
		this.reciprocal = reciprocal;
	}

}