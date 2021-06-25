package org.sunbird.ruleengine.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@MappedSuperclass
public abstract class AbstractSettings extends AbstractMultiTenantEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "KEY", updatable=false)
	private String key;

	@Column(name = "VALUE")
	private String value;

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

}
