package org.myproject.test.conndb.repositories;

import org.junit.Test;
import org.myproject.model.utils.MailSender;

public class EmailSenderTest {

    @Test
    public void Test1 () {
    
        
        MailSender mail = new MailSender();
        
        mail.sendEmail("gepaq@academiamilitar.pt", 
                       "gepaq@academiamilitar.pt", "Test", "Teste",
                       "gepaq@academiamilitar.pt", "chefegepaq");
        
        
    }
}
