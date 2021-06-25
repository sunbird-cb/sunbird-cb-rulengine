package org.sunbird.ruleengine.model;



import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="AUDIT_USER_ACTION")
public class AuditUserAction extends AbstractMultiTenantEntity implements Serializable{
	
	@Id	
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="AUDIT_USR_ACTION_SEQ")
	@SequenceGenerator(name="AUDIT_USR_ACTION_SEQ", sequenceName="AUDIT_USR_ACTION_SEQ", allocationSize=1)
	private BigInteger id;
	
	
		
		private static final long serialVersionUID = 1723925210620674919L;

		public AuditUserAction() {
			super();
		}



		public BigInteger getId() {
			return id;
		}



		public void setId(BigInteger id) {
			this.id = id;
		}



		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public BigInteger getRowId() {
			return rowId;
		}

		public void setRowId(BigInteger rowId) {
			this.rowId = rowId;
		}

		public BigInteger getActionBy() {
			return actionBy;
		}

		public void setActionBy(BigInteger actionBy) {
			this.actionBy = actionBy;
		}

	

		public Date getActionDate() {
			return actionDate;
		}

		public void setActionDate(Date actionDate) {
			this.actionDate = actionDate;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

	
		

		@Column(name = "TABLE_NAME")
		private String tableName;
		

		@Column(name = "ROW_ID")
		private BigInteger rowId;
		
		@Column(name = "ACTION_BY")
		private BigInteger actionBy;
		
		@Column(name = "ORG_ID")
		private BigInteger orgId;
		
		
		
		
		
		public BigInteger getOrgId() {
			return orgId;
		}



		public void setOrgId(BigInteger orgId) {
			this.orgId = orgId;
		}
		
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "ACTION_DATE_TIME")
		private Date actionDate;

		@Column(name = "ACTION")
		private String action;
		
}

