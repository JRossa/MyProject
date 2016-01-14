package org.myproject.test.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.myproject.support.email.MailAccount;

import java.util.Properties;

/**
 * Created by anirudh on 28/10/14.
 */
public class JavaGmailSend_SSL {

    public static void main(String[] args) {

        MailAccount mailAccount = new MailAccount();
        
        mailAccount.setUserAccount(mailAccount.JR);
        mailAccount.setServerAccount(mailAccount.JR);
    	
        final String username = mailAccount.getServerAccount().getUserLogin();
        final String password = mailAccount.getServerAccount().getUserPassword();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailAccount.getUserAccount().getUserLogin()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailAccount.getUserAccount().getUserLogin()));
            message.setSubject("Test JCG Example");
            message.setText("Hi," +
                    "This is a Test mail for JCG Example!");

            Transport.send(message);

            System.out.println("Mail sent succesfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
