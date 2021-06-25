package org.sunbird.ruleengine.helper;

public class TranslatorNotFoundException extends Exception {

	private String translatorCode;

	private String message;

	public TranslatorNotFoundException(String err) {
		super(err);
		this.message = err;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTranslatorCode() {
		return translatorCode;
	}

	public void setTranslatorCode(String translatorCode) {
		this.translatorCode = translatorCode;
	}

}
