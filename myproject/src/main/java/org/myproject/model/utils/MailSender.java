package org.myproject.model.utils;

import java.util.Properties;




import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.samples.oauth2.OAuth2SaslClientFactory;
import com.sun.mail.smtp.SMTPTransport;

// https://www.google.com/settings/security/lesssecureapps


@Stateless
public class MailSender {

    static Logger logger = LoggerFactory.getLogger(MailSender.class);
   
    
//   TODO - http://stackoverflow.com/questions/22777223/connecting-to-user-email-with-oauth2
    
    public void sendEmail (String fromEmail, String toEmail, String subject, String message,
                           String username, String password) {
        
        try {
            Properties props = System.getProperties();
            
            props.setProperty("mail.transport.protocol", "smtp"); 
            props.setProperty("mail.smpt.host", "smtp.gmail.com");
            props.put("mail.smpt.auth", "true");
            props.put("mail.smpt.port", "465");
            props.put("mail.smpt.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smpt.socketFactory.port", "465");
            props.put("mail.smpt.socketFactory.fallback", "false");
            
            // ou Transport transport = mailSession.getTransport("smtps");
            // tenho os dois ativos para não haver problemas
            props.put("mail.smtp.starttls.enable","true"); 
            props.put("mail.smtp.EnableSSL.enable","true");

            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(true);
        
            Message mailMessage = new MimeMessage(mailSession);
            
            mailMessage.setFrom(new InternetAddress(fromEmail));
            mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            mailMessage.setContent(message, "text/html; charset=UTF-8");
            mailMessage.setSubject(subject);
            
            Transport transport = mailSession.getTransport("smtps");
            transport.connect("smtp.gmail.com", username, password);
            
            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
            transport.close();
            
        } catch (AddressException e) {
            logger.debug(MailSender.class.getName() + " Address problem !!");
            e.printStackTrace();
        } catch (MessagingException e) {
            logger.debug(MailSender.class.getName() + " Message problem !!");
            e.printStackTrace();
        }
        
        
        
    }
    
    
    
	public void sendOAuthEmail(String fromEmail, String toEmail, String subject, String message, String username,
			String oauthToken) {

		boolean debug = true;
		
		String host = "smtp.gmail.com";
		int port = 587;
		
		try {
			Properties props = System.getProperties();
//            props.setProperty("mail.smpt.host", "smtp.gmail.com");
//            props.put("mail.smpt.port", "587");

			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.starttls.required", "true");
			
		    props.put("mail.imap.ssl.enable", "true"); // required for Gmail
			props.put("mail.imap.auth.mechanisms", "XOAUTH2");
			
			props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, oauthToken);
			Session mailSession = Session.getInstance(props);
			mailSession.setDebug(debug);

			final URLName unusedUrlName = null;
			SMTPTransport transport = new SMTPTransport(mailSession, unusedUrlName);
			// If the password is non-null, SMTP tries to do AUTH LOGIN.
			final String emptyPassword = "";
			transport.connect(host, port, username, emptyPassword);

			Message mailMessage = new MimeMessage(mailSession);

			mailMessage.setFrom(new InternetAddress(fromEmail));
			mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			mailMessage.setContent(message, "text/html; charset=UTF-8");
			mailMessage.setSubject(subject);

			transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
			transport.close();

		} catch (AddressException e) {
			logger.debug(MailSender.class.getName() + " Address problem !!");
			e.printStackTrace();
		} catch (MessagingException e) {
			logger.debug(MailSender.class.getName() + " Message problem !!");
			e.printStackTrace();
		}

	}

}
