package org.myproject.test.oauth;

import com.sun.mail.imap.IMAPStore;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.code.samples.oauth2.OAuth2SaslClientFactory;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.smtp.SMTPTransport;

import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.myproject.support.email.MailAccount;


/**
 * Performs OAuth2 authentication.
 *
 * Before using this class, you must call {@code initialize} to install the
 * OAuth2 provider.
 * 
 */
public class OAuth2Authenticator {
	
	
  private static final Logger logger = Logger.getLogger(OAuth2Authenticator.class.getName());

  public static final class OAuth2Provider extends Provider {
    private static final long serialVersionUID = 1L;

    public OAuth2Provider() {
      super("Google OAuth2 Provider", 1.0,
            "Provides the XOAUTH2 SASL Mechanism");
      put("SaslClientFactory.XOAUTH2",
          "com.google.code.samples.oauth2.OAuth2SaslClientFactory");
    }
  }

  /**
   * Installs the OAuth2 SASL provider. This must be called exactly once before
   * calling other methods on this class.
   */
  public static void initialize() {
    Security.addProvider(new OAuth2Provider());
  }

	
  public static void main(String args[]) throws Exception {
 
    Gmail service = GmailQuickstart.getGmailService();
    
    // Print the labels in the user's account.
    String user = "me";
    ListLabelsResponse listResponse =
        service.users().labels().list(user).execute();
    List<Label> labels = listResponse.getLabels();
    if (labels.size() == 0) {
        System.out.println("No labels found.");
    } else {
        System.out.println("Labels:");
        for (Label label : labels) {
            System.out.printf("- %s\n", label.getName());
        }
    }
    
    MailAccount mailAccount = new MailAccount();
    
    mailAccount.setUserAccount(mailAccount.JR);
    
    MyClass.sendMessage(service, 
    		            user, 
    		            MyClass.createEmail(mailAccount.getUserAccount().getUserLogin(), 
    		            		            mailAccount.getUserAccount().getUserLogin(), 
    		            		            "Teste", 
    		            		            "Nada"));
    		            		            
  }
}