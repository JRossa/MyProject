package org.myproject.test.email;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.junit.Test;
import org.myproject.model.utils.MailSender;

public class EmailSenderTest {

    @Test
    public void Test1 () {
    
        
        MailSender mail = new MailSender();
       
        mail.sendEmail("email@gmail.pt", 
                       "email@gmail.pt", "Test", "Teste",
                       "email@gmail.pt", "password");
        
       
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
