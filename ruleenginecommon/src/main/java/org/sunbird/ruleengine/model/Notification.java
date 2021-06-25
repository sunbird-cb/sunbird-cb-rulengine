package org.sunbird.ruleengine.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTIFICATION")
public class Notification extends AbstractNotification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="NOTIFICATION_ID_SEQ")
	@SequenceGenerator(name="NOTIFICATION_ID_SEQ", sequenceName="NOTIFICATION_ID_SEQ", allocationSize=1)
	*/
	private BigInteger id;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}
}
