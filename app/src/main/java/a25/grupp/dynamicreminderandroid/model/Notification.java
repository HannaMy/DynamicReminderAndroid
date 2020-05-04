package a25.grupp.dynamicreminderandroid.model;

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a notification
 * @author Minna R�riksson
 * @version 1.0
 */
public class Notification {
    private Task task;
    private Date timeForNotification;
    private String message;
    private String buttonTitles;
    private Calendar calendarTimeForNotification;


    public Notification(Task task, Date timeForNotification, String message){ //TODO: tog bort buttonTitles från konstruktorn
        this.task = task;
        this.timeForNotification = timeForNotification;
        this.message = message;
        calendarTimeForNotification = Calendar.getInstance();
        calendarTimeForNotification.setTime(timeForNotification);
        //this.buttonTitles = buttonTitles;
    }

    public void setTimeForNotification(Date timeForNotification){
        this.timeForNotification = timeForNotification;
    }

    public Date getTimeForNotification() {
        return timeForNotification;
    }

    public void setMessage(String message){

    }

    public String getMessage(){
        return message;
    }

    public Calendar getCalendarTimeForNotification() {
        return calendarTimeForNotification;
    }

    public void setCalendarTimeForNotification(Calendar calendarTimeForNotification) {
        this.calendarTimeForNotification = calendarTimeForNotification;
    }

    public int getNbrOfButtons(){
        return 0;
    }

    public String getButtonText(int nbrOfButtons){
        return null;
    }

}
