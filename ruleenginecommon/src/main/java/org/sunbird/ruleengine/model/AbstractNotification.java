package org.sunbird.ruleengine.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractNotification extends AbstractMultiTenantEntity{
/*

	*/
	@Column(name="NOTIFICATION_TYPE")
	private String notificationType;


	 @Column(name = "NOTIFIED")
	 private Boolean notified;
	

	@Column(name="NOTIFY_TO")
	private BigInteger notifyTo;

	
	@Column(name="NOTIFICATION_FROM")
	private BigInteger notificationFrom;
	
	@Column(name="NOTIFICATION_REFERENCE_ID")
	private BigInteger notificationReferenceId;
	
	@Column(name="NOTIFICATION_TEXT")
	private String notificationText;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NOTIFICATION_TIME")
	private Date notificationTime;
	
	 @Column(name = "READ")
	 private Boolean read=Boolean.FALSE;
	
	@Transient	
	private String firstName;
	
	@Transient	
	private String lastName;

	

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}



	public Boolean getNotified() {
		return notified;
	}

	public void setNotified(Boolean notified) {
		this.notified = notified;
	}

	public BigInteger getNotifyTo() {
		return notifyTo;
	}

	public void setNotifyTo(BigInteger notifyTo) {
		this.notifyTo = notifyTo;
	}

	public BigInteger getNotificationFrom() {
		return notificationFrom;
	}

	public void setNotificationFrom(BigInteger notificationFrom) {
		this.notificationFrom = notificationFrom;
	}

	public BigInteger getNotificationReferenceId() {
		return notificationReferenceId;
	}

	public void setNotificationReferenceId(BigInteger notificationReferenceId) {
		this.notificationReferenceId = notificationReferenceId;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public Date getNotificationTime() {
		return notificationTime;
	}

	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	
}
