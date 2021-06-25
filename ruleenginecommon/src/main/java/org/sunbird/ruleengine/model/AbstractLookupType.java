package org.sunbird.ruleengine.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractLookupType extends AbstractEntity {


	
	@Column(name="CLIENT_ID")
	private Long clientId;

	
	@Column(name="LOOKUP_TYPE")
	private String lookupType;

	@Column(name="MEANING")
	private String meaning;

	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="LANGUAGE")
	private String language;

	@Column(name="CUSTOMIZATION_LEVEL")
	private String customizationLevel;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getLookupType() {
		return lookupType;
	}

	public void setLookupType(String lookupType) {
		this.lookupType = lookupType;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCustomizationLevel() {
		return customizationLevel;
	}

	public void setCustomizationLevel(String customizationLevel) {
		this.customizationLevel = customizationLevel;
	}
	
	
}
