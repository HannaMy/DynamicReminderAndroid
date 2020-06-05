package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a notification
 * @author Minna Röriksson
 * @version 1.0
 */
public class Notification implements Serializable {
    private Task task;
    private Date timeForNotification;
    private String message;
    private Calendar calendarTimeForNotification;


    public Notification(Task task, Date timeForNotification, String message){ //TODO: tog bort buttonTitles från konstruktorn
        this.task = task;
        this.timeForNotification = timeForNotification;
        this.message = message;
        calendarTimeForNotification = Calendar.getInstance();
        calendarTimeForNotification.setTime(timeForNotification);
    }

    public Date getTimeForNotification() {
        return timeForNotification;
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


}
