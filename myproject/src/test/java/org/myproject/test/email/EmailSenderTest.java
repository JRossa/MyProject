package org.myproject.test.email;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.Test;
import org.myproject.model.utils.MailSender;
import org.myproject.support.email.MailAccount;

public class EmailSenderTest {

    @Test
    public void Test1 () {
    
        
        MailSender mail = new MailSender();
       
        MailAccount mailAccount = new MailAccount();
 
        mailAccount.setUserAccount(mailAccount.JR);
        mailAccount.setServerAccount(mailAccount.JR);
        
        System.out.println("User Log     : " + mailAccount.getUserAccount().getUserLogin());
        System.out.println("Server Log   : " + mailAccount.getServerAccount().getUserLogin());
        System.out.println("Server Pass  : " + mailAccount.getServerAccount().getUserPassword());

         mail.sendOAuthEmail(mailAccount.getUserAccount().getUserLogin(), 
                        mailAccount.getUserAccount().getUserLogin(), "Test", "Teste",
                        mailAccount.getServerAccount().getUserLogin(), 
                        mailAccount.getServerAccount().getUserPassword());
       
    }
    
	private LocalDate calcNextFriday(LocalDate d) {
		 return d.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
	}
    
	@Test
    public void Test2 () {
		LocalDate ld = new java.sql.Date(new java.util.Date().getTime()).toLocalDate();

		System.out.println("Friday :" + calcNextFriday(ld).toString());
		System.out.println("Friday :" + calcNextFriday(ld).getDayOfMonth());
    }
}
