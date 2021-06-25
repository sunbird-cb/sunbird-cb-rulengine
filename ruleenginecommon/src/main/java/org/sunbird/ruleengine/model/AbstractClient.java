package org.sunbird.ruleengine.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractClient extends AbstractEntity{
	
	@Column(name="CREATED_BY")
	private String clientCode;

	
}
