package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Keeps track o a intervall with a startTime, from and a endtime, to
 * @author Hanna My Jansson
 * @version 1.0
 */
public class TimeInterval implements Serializable {
    private int from;
    private int to;
    private boolean isEmpty;


    public TimeInterval(int from, int to) {
        this.from = from;
        this.to = to;
        isEmpty = false;
    }

    public TimeInterval() {
        isEmpty = true;
    }


    /**
     * Checks if the interval is empty
     * @return true if ther is no specified time in the interval
     */
    public boolean isEmpty() {
        return isEmpty;
    }


    /**
     * Changes the interval to the given parameters from och to.
     * @param from the beginning of the interval
     * @param to the end of the interval
     * @return true if it was able to change the interval
     */
    public boolean setInterval(int from, int to) {

        if (from<to) {
            this.from = from;
            this.to = to;
            isEmpty = false;
            return true;
        } else {
            System.out.println("TimeInterval rad51: false" );
            return false;
        }

    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    /**
     * Checks if the date is in the interval
     * @param date the date to be checked
     * @return true if the given date is in the given time Interval
     */
    public boolean isInInterval(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR);
        //int minute = cal.get(Calendar.MINUTE);



        if (from<=hour && to>hour) {
            return true;
        }
        return false;
    }

    public int hoursUntilInterval(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR);
        //int minute = cal.get(Calendar.MINUTE);

        int hoursUntil = from-hour;
        if(hoursUntil<0){
            hoursUntil += 24;
        }
        return hoursUntil;
    }

    public int hoursBeforeInterval(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR);
        //int minute = cal.get(Calendar.MINUTE);

        int hoursBefore = hour-(to-1);
        if(hoursBefore<0){
            hoursBefore += 24;
        }
        return hoursBefore;
    }


}
