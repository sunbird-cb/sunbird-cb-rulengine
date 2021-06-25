package org.sunbird.ruleengine.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Approval {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="PRIORITY")
	private String priority;
	
	@Column(name="SKIP_TO_NEXT_LEVEL")
	private String skipToNextlevel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSkipToNextlevel() {
		return skipToNextlevel;
	}

	public void setSkipToNextlevel(String skipToNextlevel) {
		this.skipToNextlevel = skipToNextlevel;
	}
	
	

}
