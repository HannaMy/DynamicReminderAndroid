package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Keeps track o a intervall with a startTime, from and a endtime, to
 *
 * @author Hanna My Jansson
 * @version 1.3
 */
public class TimeInterval implements Serializable {
    private int from;
    private int to;

    public TimeInterval() {
    }


    /**
     * Changes the interval to the given parameters from och to.
     *
     * @param from the beginning of the interval
     * @param to   the end of the interval
     */
    public void setInterval(int from, int to) {
        if (from < to) {
            this.from = from;
            this.to = to;
        } else {
            System.out.println("TimeInterval rad51: false");
        }
    }

    /**
     * Get method for from variable
     *
     * @return hours from
     */
    public int getFrom() {
        return from;
    }

    /**
     * Get method for to variable
     *
     * @return hours to
     */
    public int getTo() {
        return to;
    }

    /**
     * Checks if the date is in the interval
     *
     * @param date the date to be checked
     * @return true if the given date is in the given time Interval
     */
    public boolean isInInterval(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return from <= hour && to > hour;
    }

    /**
     * Calculate how many hours until the chosen interval
     *
     * @param date the current date where the interval is added to
     * @return the time until in hours
     */
    public int hoursUntilInterval(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR);
        int hoursUntil = from - hour;
        if (hoursUntil < 0) {
            hoursUntil += 24;
        }
        return hoursUntil;
    }

    /**
     * Calculate how many hours since last possible time for execution
     *
     * @param date the date for the last possible time for execution
     * @return the time since last possible time for execution in hours
     */
    public int hoursBeforeInterval(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR);
        int hoursBefore = hour - (to - 1);
        if (hoursBefore < 0) {
            hoursBefore += 24;
        }
        return hoursBefore;
    }
}
