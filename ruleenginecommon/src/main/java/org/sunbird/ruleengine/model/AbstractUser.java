package org.sunbird.ruleengine.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * The persistent class for the USER_ database table.
 * 
 */
@MappedSuperclass
public abstract class AbstractUser extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private String address;

	private String city;

	@Column(name="CLOUD_PARTY_ID")
	private BigDecimal cloudPartyId;

	@Column(name="CLOUD_PARTY_NUM")
	private BigDecimal cloudPartyNum;

	@Column(name="CLOUD_PERSON_NUMBER")
	private BigDecimal cloudPersonNumber;

	private String country;

	private String email;

	@Column(name="FIRST_NAME")
	private String firstName;

	@Column(name="LAST_NAME")
	private String lastName;

	@Column(name="ONE_TIME_KEY")
	private String oneTimeKey;

	@Column(name="ORG_ID")
	private BigDecimal orgId;

	private String password;
	
	@Column(name = "PHONE")
	private String phone;
	
	@Column(name = "EMAIL_CONFIRMED")
	private boolean emailConfirmed;

	@Column(name="SOURCE_USER_ID")
	private String sourceUserId;

	@Column(name="SOURCE_USER_NUMBER")
	private String sourceUserNumber;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="CLIENT_ID")
	BigDecimal clientId;
	

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BigDecimal getCloudPartyId() {
		return this.cloudPartyId;
	}

	public void setCloudPartyId(BigDecimal cloudPartyId) {
		this.cloudPartyId = cloudPartyId;
	}

	public BigDecimal getCloudPartyNum() {
		return this.cloudPartyNum;
	}

	public void setCloudPartyNum(BigDecimal cloudPartyNum) {
		this.cloudPartyNum = cloudPartyNum;
	}

	public BigDecimal getCloudPersonNumber() {
		return this.cloudPersonNumber;
	}

	public void setCloudPersonNumber(BigDecimal cloudPersonNumber) {
		this.cloudPersonNumber = cloudPersonNumber;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return this.email; 
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOneTimeKey() {
		return this.oneTimeKey;
	}

	public void setOneTimeKey(String oneTimeKey) {
		this.oneTimeKey = oneTimeKey;
	}

	public BigDecimal getOrgId() {
		return this.orgId;
	}

	public void setOrgId(BigDecimal orgId) {
		this.orgId = orgId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSourceUserId() {
		return this.sourceUserId;
	}

	public void setSourceUserId(String sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public String getSourceUserNumber() {
		return this.sourceUserNumber;
	}

	public void setSourceUserNumber(String sourceUserNumber) {
		this.sourceUserNumber = sourceUserNumber;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getClientId() {
		return clientId;
	}

	public void setClientId(BigDecimal clientId) {
		this.clientId = clientId;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	

}