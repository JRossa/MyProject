package org.myproject.support.clock;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Named;





import org.myproject.model.utils.BaseBean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "clockMBean")
public class ClockMBean extends BaseBean {

	private static final long serialVersionUID = -4122506723972872414L;

	
	private final static long ONCE_PER_DAY = 1000*60*60*24;
	
	private final static int HOURS = 18;
	private final static int MINUTES = 00;

	
	public void setupAlarm() {
		AlarmClock alarmClock = new AlarmClock(3, 1, 20);
		alarmClock.activateAlarmThenStop();
		
	}
	
	public void setupTimeToSendNotification () {
    	TimerTask fetchMail = new FetchMail();
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(fetchMail, getFetchTime(), (7 * ONCE_PER_DAY));
	}
	
	
	private static LocalDate calcNextFriday(LocalDate d) {
		 return d.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
	}

	
	private static Date getFetchTime (){
		LocalDate ld = new java.sql.Date(new java.util.Date().getTime()).toLocalDate();
		
		Calendar scheduleTime = new GregorianCalendar();

		scheduleTime.add(Calendar.DATE, calcNextFriday(ld).getDayOfMonth());
		
		Calendar result = new GregorianCalendar(scheduleTime.get(Calendar.YEAR),
												scheduleTime.get(Calendar.MONTH),
												scheduleTime.get(Calendar.DATE), HOURS, MINUTES);
		return result.getTime();
	}

}
