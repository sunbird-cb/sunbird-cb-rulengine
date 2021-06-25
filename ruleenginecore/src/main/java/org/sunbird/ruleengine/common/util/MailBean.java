package org.sunbird.ruleengine.common.util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.model.Client;
import org.sunbird.ruleengine.model.ClientSettings;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.ClientSettingsService;
import org.sunbird.ruleengine.service.GenericCron;

@Component
public class MailBean {
	private static final Logger logger = LogManager.getLogger(MailBean.class);
	@Autowired
	ClientSettingsService clientSettingsService;

	@Autowired
	ClientService clientService;

	private final Map<String, Transport> transportTLSMap = new HashMap<>();

	
	public void init() {
		List<Client> clients = clientService.getListByCriteria(new Client(), -1, 0);
		for (Client client : clients) {
			String emailsToLoad = clientSettingsService.getValue(client.getId(), "MAIL_ACCOUNT_TO_LOAD");
			if (!emailsToLoad.isEmpty() && transportTLSMap.isEmpty()) {
				for (String email : emailsToLoad.split(",")) {
					init(client.getId(), email);
				}
			}
		}
	}

	private void init(BigInteger clientId, String email) {
		Properties properties = new Properties();
		properties.put("mail.smtp.starttls.enable", "true");
		ClientSettings clientSettingsSMTPAddres = new ClientSettings();
		clientSettingsSMTPAddres.setKey(email + "_SMTP_ADDRESS");
		ClientSettings clientSettingsSMTPPort = new ClientSettings();
		clientSettingsSMTPPort.setKey(email + "_PORT");
		ClientSettings applicationPropertiesPassword = new ClientSettings();
		applicationPropertiesPassword.setKey(email + "_PASSWORD");
		createTransport(clientSettingsService.getValue(clientId, email + "_SMTP_ADDRESS"), "smtp",
				Integer.parseInt(clientSettingsService.getValue(clientId, email + "_PORT")), email,
				clientSettingsService.getValue(clientId, email + "_PASSWORD"), properties);
	}

	public Session sessionTLS;

	public void createTransport(String smtpAdddress, String protocol, int port, final String fromEmail,
			final String password, Properties properties) {
		try {
			Properties propsTLS = new Properties();
			propsTLS.put("mail.transport.protocol", protocol);
			propsTLS.put("mail.smtp.host", smtpAdddress);
			propsTLS.put("mail.smtp.starttls.enable", "true");
			propsTLS.put("mail.smtp.auth", "true");
			propsTLS.put("mail.debug", "true");
			propsTLS.put("mail.smtp.ssl.enable", "true"); // Added
			if (properties != null)
				propsTLS.putAll(properties);
			sessionTLS = Session.getInstance(propsTLS, new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail, password);
				}
			});
			Transport transportTLS = sessionTLS.getTransport();
			transportTLS.connect(smtpAdddress, port, fromEmail, password);
			transportTLSMap.put(fromEmail, transportTLS);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
	}

	public boolean sendMail(BigInteger clientId, String fromMail, String fromMailDisplay, String fromName,
			String toEmail, String message, String subject, boolean isHtml) {
		try {
			Properties properties = new Properties();
			properties.put("mail.smtp.starttls.enable", "true");
			MimeMessage messageTLS = new MimeMessage(sessionTLS);
			messageTLS.setFrom(new InternetAddress(fromMailDisplay, fromName));
			Address[] address = new Address[1];
			address[0] = new InternetAddress(fromMailDisplay, fromName);
			messageTLS.setReplyTo(address);
			messageTLS.setSender(address[0]);
			// for (String singleEmail : toEmail.split(",")) {
			// messageTLS.setRecipients(Message.RecipientType.TO,
			// InternetAddress.parse(singleEmail));
			// }
			messageTLS.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			messageTLS.setSubject(subject);

			if (isHtml) {
				messageTLS.setText(message, "utf-8", "html");
			} else {
				messageTLS.setText(message);
			}
			init(clientId, fromMail);
			transportTLSMap.get(fromMail).sendMessage(messageTLS, messageTLS.getAllRecipients());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
			return false;
		}
	}

}
