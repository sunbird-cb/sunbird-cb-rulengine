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
@Table(name="TRANSLATION_MAPPER")
public class TranslationMapper extends AbstractMultiTenantEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name="CODE")
	private String code;
	
	@Column(name="SOURCE_FIELD_NAME")
	private String sourceFieldName;
	
	@Column(name="DESTINATION_FIELD_NAME")
	private String destinationFieldName;
	
	@Column(name="SOURCE_FIELD_DESCRIPTION")
	private String sourceFieldDescription;
	
	@Column(name="DESTINATION_FIELD_DESCRIPTION")
	private String destinationFieldDescription;
	
	
	@Column(name="RECIPROCAL_CODE")
	private String reciprocalCode;

	
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSourceFieldName() {
		return sourceFieldName;
	}

	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}

	public String getDestinationFieldName() {
		return destinationFieldName;
	}

	public void setDestinationFieldName(String destinationFieldName) {
		this.destinationFieldName = destinationFieldName;
	}

	public String getSourceFieldDescription() {
		return sourceFieldDescription;
	}

	public void setSourceFieldDescription(String sourceFieldDescription) {
		this.sourceFieldDescription = sourceFieldDescription;
	}

	public String getDestinationFieldDescription() {
		return destinationFieldDescription;
	}

	public void setDestinationFieldDescription(String destinationFieldDescription) {
		this.destinationFieldDescription = destinationFieldDescription;
	}

	public String getReciprocalCode() {
		return reciprocalCode;
	}

	public void setReciprocalCode(String reciprocalCode) {
		this.reciprocalCode = reciprocalCode;
	}
	
	
	
	
}
