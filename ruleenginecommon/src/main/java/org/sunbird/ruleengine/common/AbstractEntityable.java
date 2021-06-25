package org.sunbird.ruleengine.common;

import java.math.BigInteger;
import java.util.Date;

public interface AbstractEntityable {

	public abstract BigInteger getId();

	public abstract void setId(BigInteger id);

	public BigInteger getCreatedBy();

	public void setCreatedBy(BigInteger createdBy);

	public Date getCreationDate();

	public void setCreationDate(Date creationDate);

	public Date getLastUpdateDate();

	public void setLastUpdateDate(Date lastUpdateDate);

	public BigInteger getLastUpdatedBy();

	public void setLastUpdatedBy(BigInteger lastUpdatedBy);

}
