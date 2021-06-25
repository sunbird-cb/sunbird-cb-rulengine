package org.sunbird.ruleengine.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@Entity
@Table(name = "USER_EVENT")
public class UserEvent extends AbstractMultiTenantEntity implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_LOGIN_EVENTS_SEQ")
	@SequenceGenerator(name = "USER_LOGIN_EVENTS_SEQ", sequenceName = "USER_LOGIN_EVENTS_SEQ", allocationSize = 1)*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "USER_ID")
	BigInteger userId;

	@Column(name = "USER_NAME")
	String userName;

	@Column(name = "FULL_NAME")
	String fullName;

	@Enumerated(EnumType.STRING)
	@Column(name = "ACTION")
	Action action;
	
	@Transient
	
	 Date fromDate;

	@Transient
	
	 Date toDate;

	
	
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public enum Action {
		LOGIN, LOGOUT
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getUserId() {
		return userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
