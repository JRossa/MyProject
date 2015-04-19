package org.myproject.model.utils;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
 

// http://www.journaldev.com/1050/java-timer-and-timertask-example-tutorial
public class NotificationTimer extends TimerTask {
 
    @Override
    public void run() {
        System.out.println("Timer task started at:"+new Date());
        completeTask();
        System.out.println("Timer task finished at:"+new Date());
    }
 
    
    private void completeTask() {
        try {
            //assuming it takes 20 secs to complete the task
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
     
    
    public static void main(String args[]){
        TimerTask timerTask = new NotificationTimer();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        
        timer.scheduleAtFixedRate(timerTask, 0, 10*1000);
        
        System.out.println("TimerTask started");
        
        // To wait until cancel the task after sometime
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        timer.cancel();
        
        System.out.println("TimerTask cancelled");
        
        // To finish the current task after cancel
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
 
}