package org.sunbird.ruleengine.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class AbstractLookupCodes extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	
	@Column(name="CLIENT_ID")
	private Long clientId;
	
	@Column(name="ATTRIBUTE_CATEGORY")
	private String attributeCategory;
	
	@Column(name="LOOKUP_CODE")
	private String lookupCode;
	
	@Column(name="LOOKUP_TYPE")
	private String lookupType;

	@Column(name="MEANING")
	private String meaning;

	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="ENABLED_FLAG")
	private Boolean enabledFlag;

	@Column(name="LANGUAGE")
	private String language;
	
	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE_ACTIVE")
	private Date startDateActive;
	
	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE_ACTIVE")
	private Date endDateActive;
	
	
	@Column(name="LAST_UPDATE_LOGIN")
	private Long lastUpdateLogin;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getAttributeCategory() {
		return attributeCategory;
	}

	public void setAttributeCategory(String attributeCategory) {
		this.attributeCategory = attributeCategory;
	}

	public String getLookupCode() {
		return lookupCode;
	}

	public void setLookupCode(String lookupCode) {
		this.lookupCode = lookupCode;
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

	public Boolean getEnabledFlag() {
		return enabledFlag;
	}

	public void setEnabledFlag(Boolean enabledFlag) {
		this.enabledFlag = enabledFlag;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getStartDateActive() {
		return startDateActive;
	}

	public void setStartDateActive(Date startDateActive) {
		this.startDateActive = startDateActive;
	}

	public Date getEndDateActive() {
		return endDateActive;
	}

	public void setEndDateActive(Date endDateActive) {
		this.endDateActive = endDateActive;
	}

	public Long getLastUpdateLogin() {
		return lastUpdateLogin;
	}

	public void setLastUpdateLogin(Long lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}
	
	
	
	
}
