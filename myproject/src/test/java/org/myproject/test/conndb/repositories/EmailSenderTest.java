package org.myproject.test.conndb.repositories;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

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
