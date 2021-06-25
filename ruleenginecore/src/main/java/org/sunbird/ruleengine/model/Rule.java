package org.sunbird.ruleengine.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.sunbird.ruleengine.model.AbstractMultiTenantEntity;

@Entity
@Table(name = "RULE")
public class Rule extends AbstractMultiTenantEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	/*
	 * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STEP_SEQ")
	 * 
	 * @SequenceGenerator(name = "STEP_SEQ", sequenceName = "STEP_SEQ",
	 * allocationSize = 1)
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	@Column(name = "JOB_DETAIL_ID")
	BigInteger jobDetailId;

	@Column(name = "PARENT_ID")
	BigInteger parentId;

	@Column(name = "SUCCESS_KEY")
	String successKey;

	@Column(name = "SUCCESS_VALUE")
	String successValue;

	@Column(name = "FAILURE_JOB_DETAIL_ID")
	BigInteger failureJobDetailId;

	@Column(name = "FAILURE_STEP_ID")
	BigInteger failureStepId;

	@Column(name = "RESPONSE_FORMATTER_JS")
	String responseFormatterJs;

	@Column(name = "REQUEST_FORMATTER_JS")
	String requestFormatterJs;

	@Column(name = "ERROR_FORMATTER_JS")
	String errorFormatterJs;

	@Column(name = "LOGGING_ENABLED")
	boolean loggingEnabled;

	@Column(name = "UNIQUE_KEY_TEMPLATE")
	String uniqueKeyTemplate;

	@Column(name = "DRY_RUN_RESPONSE")
	String dryRunResponse;

	@Column(name = "SKIP_UNIQUE")
	boolean skipUnique;

	@Transient
	Boolean parentNull;

	@Column(name = "STEP_CODE")
	@Pattern(regexp = "[a-zA-Z_]+", message = "Please enter valid step code")
	String stepCode;

	@Column(name = "DUPLICATE_CHECK_ENABLED")
	boolean duplicateCheckEnabled;

	@Column(name = "REQUEST_VALIDATOR")
	String requestValidator;

	@Column(name = "BEFORE_AGENT_CALL")
	Boolean beforeAgentCall = Boolean.FALSE;

	@Override
	public BigInteger getId() {
		return id;
	}

	@Override
	public void setId(BigInteger id) {
		this.id = id;
	}

	@Column(name = "STEP_TYPE")
	@Enumerated(EnumType.STRING)
	Type stepType;

	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	Type type;

	@Column(name = "SUB_TYPE")
	String subType;

	@Column(name = "PATH")
	String path;

	@Column(name = "REQUEST_TEMPLATE")
	String requestTemplate;

	@Column(name = "HEADER_TEMPLATE")
	String headerTemplate;

	@Column(name = "RESPONSE_TYPE")
	String responseType;

	@Column(name = "REQUEST_TYPE")
	String requestType;

	@Column(name = "SEQUENCE")
	Integer sequence;

	@Enumerated(EnumType.STRING)
	@Column(name = "RESULT")
	Result result;

	@Enumerated(EnumType.STRING)
	@Column(name = "OBJECT_TYPE")
	ObjectType objectType;

	public enum Result {
		OBJECT, LIST, MAP
	}

	public enum ObjectType {
		OBJECT_ARRAY, LINKED_HASH_MAP
	}

	public enum Type {
		REST, NASHORN, FREEMARKER
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public BigInteger getJobDetailId() {
		return jobDetailId;
	}

	public void setJobDetailId(BigInteger jobDetailId) {
		this.jobDetailId = jobDetailId;
	}

	public Type getStepType() {
		return stepType;
	}

	public void setStepType(Type stepType) {
		this.stepType = stepType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRequestTemplate() {
		return requestTemplate;
	}

	public void setRequestTemplate(String requestTemplate) {
		this.requestTemplate = requestTemplate;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public BigInteger getParentId() {
		return parentId;
	}

	public void setParentId(BigInteger parentId) {
		this.parentId = parentId;
	}

	public Boolean getParentNull() {
		return parentNull;
	}

	public void setParentNull(Boolean parentNull) {
		this.parentNull = parentNull;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public BigInteger getFailureJobDetailId() {
		return failureJobDetailId;
	}

	public void setFailureJobDetailId(BigInteger failureJobDetailId) {
		this.failureJobDetailId = failureJobDetailId;
	}

	public String getSuccessKey() {
		return successKey;
	}

	public void setSuccessKey(String successKey) {
		this.successKey = successKey;
	}

	public String getSuccessValue() {
		return successValue;
	}

	public void setSuccessValue(String successValue) {
		this.successValue = successValue;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getHeaderTemplate() {
		return headerTemplate;
	}

	public void setHeaderTemplate(String headerTemplate) {
		this.headerTemplate = headerTemplate;
	}

	public boolean isLoggingEnabled() {
		return loggingEnabled;
	}

	public void setLoggingEnabled(boolean loggingEnabled) {
		this.loggingEnabled = loggingEnabled;
	}

	public String getResponseFormatterJs() {
		return responseFormatterJs;
	}

	public void setResponseFormatterJs(String responseFormatterJs) {
		this.responseFormatterJs = responseFormatterJs;
	}

	public String getErrorFormatterJs() {
		return errorFormatterJs;
	}

	public void setErrorFormatterJs(String errorFormatterJs) {
		this.errorFormatterJs = errorFormatterJs;
	}

	public BigInteger getFailureStepId() {
		return failureStepId;
	}

	public void setFailureStepId(BigInteger failureStepId) {
		this.failureStepId = failureStepId;
	}

	public String getUniqueKeyTemplate() {
		return uniqueKeyTemplate;
	}

	public void setUniqueKeyTemplate(String uniqueKeyTemplate) {
		this.uniqueKeyTemplate = uniqueKeyTemplate;
	}

	public String getDryRunResponse() {
		return dryRunResponse;
	}

	public void setDryRunResponse(String dryRunResponse) {
		this.dryRunResponse = dryRunResponse;
	}

	public boolean isSkipUnique() {
		return skipUnique;
	}

	public void setSkipUnique(boolean skipUnique) {
		this.skipUnique = skipUnique;
	}

	public String getStepCode() {
		return stepCode;
	}

	public void setStepCode(String stepCode) {
		this.stepCode = stepCode;
	}

	public boolean isDuplicateCheckEnabled() {
		return duplicateCheckEnabled;
	}

	public void setDuplicateCheckEnabled(boolean duplicateCheckEnabled) {
		this.duplicateCheckEnabled = duplicateCheckEnabled;
	}

	public String getRequestValidator() {
		return requestValidator;
	}

	public void setRequestValidator(String requestValidator) {
		this.requestValidator = requestValidator;
	}

	public String getRequestFormatterJs() {
		return requestFormatterJs;
	}

	public void setRequestFormatterJs(String requestFormatterJs) {
		this.requestFormatterJs = requestFormatterJs;
	}

	public Boolean getBeforeAgentCall() {
		return beforeAgentCall;
	}

	public void setBeforeAgentCall(Boolean beforeAgentCall) {
		this.beforeAgentCall = beforeAgentCall;
	}

}