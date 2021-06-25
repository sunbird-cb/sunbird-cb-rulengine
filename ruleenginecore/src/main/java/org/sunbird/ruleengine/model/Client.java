package org.sunbird.ruleengine.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sunbird.ruleengine.model.AbstractEntity;


@Entity
@Table(name="CLIENT")
public class Client extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@SequenceGenerator(name="CLIENT_ID_SEQ", sequenceName="CLIENT_ID_SEQ", allocationSize=1)
	private BigInteger id;
	
	public BigInteger getId() {
		return id;
	}


	public void setId(BigInteger id) {
		this.id = id;
	}
	
	
	@Column(name="NAME")
	private String name;

	@Column(name="CODE")
	private String code;
	
	@Column(name="CURRENCY_SYMBOL")
	private String currencySymbol;

	@Column(name = "ENCRYPTION_KEY")
	private String encryptionKey;
	
	@Column(name = "IS_ADMIN")
	private boolean isAdmin;

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getCurrencySymbol() {
		return currencySymbol;
	}


	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}


	public String getEncryptionKey() {
		return encryptionKey;
	}


	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}


	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	
	
}
