package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Stores the dates, hours and weekdays possible, for a task to be done, in an array.
 *
 * @author Hanna My Jansson
 * @version 1.0
 */
public class PossibleTime implements Serializable {
    private PossibleHours possibleHours;
    private PossibleDates possibleDates;
    private PossibleWeekDays possibleWeekDays;

    public PossibleTime() {
        possibleDates = new PossibleDates();
        possibleHours = new PossibleHours();
        possibleWeekDays = new PossibleWeekDays();
    }

    /**
     * Sets the possible dates to the values int the array sent in the parameters.
     *
     * @param possibleDates
     */
    public void setPossibleDates(boolean[] possibleDates) {
        this.possibleDates.setPossibleDates(possibleDates);
    }

    /**
     * sets the possible hours to the interval derived from the two date objects
     * clears all intervals from before
     *
     * @param from a Date with the right time for the beginning of the interval
     * @param to   a Date with the right time for the end of the interval
     */
    public void setPossibleHours(Date from, Date to) {
        possibleHours.clearAllInterval();
        possibleHours.addInterval(from, to);
    }

    /**
     * sets the possible hours to the interval with the help of two LocalTime Objects
     * clears all intervals from before
     *
     * @param from a LocalTime with the right time for the beginning of the interval
     * @param to   a LocalTime with the right time for the end of the interval
     */
    public void setPossibleHours(int from, int to) {
        possibleHours.clearAllInterval();
        boolean bol = possibleHours.addInterval(from, to);
        System.out.println("Possible time: added " + bol);
    }

    /**
     * Adds a new interval to the list of possibleTimes
     *
     * @param from a LocalTime with the right time for the beginning of the interval
     * @param to   a LocalTime with the right time for the end of the interval
     */
    public boolean addPossibleHours(int from, int to) {
        boolean worked = possibleHours.addInterval(from, to);
        return worked;
    }

    /**
     * sets the possible weekdays
     *
     * @param possibleWeekDays a array with booleans representing the possibility to do the task that day. weekday-1 = index. example: tuesday= index 1.
     */
    public void setPossibleWeekDays(boolean[] possibleWeekDays) {
        this.possibleWeekDays.setPossibleWeekdays(possibleWeekDays);
    }

    /**
     * Get method for possible dates
     *
     * @return a boolean array of possible dates
     */
    public boolean[] getPossibleDates() {
        return possibleDates.getPossibleDates();
    }

    /**
     * Get method for possible weekdays
     *
     * @return a boolean array of possible weekdays
     */
    public boolean[] getPossibleWeekdays() {
        return possibleWeekDays.getPossibleWeekdays();
    }

    /**
     * Get method for possible hours
     *
     * @return a TimeInterval with possible hours
     */
    public TimeInterval getPossibleHours() {
        return possibleHours.getPossibleTimeInterval();
    }

    /**
     * checks if the chosen interval is correct and possible to choose
     *
     * @param date a date from the chosen interval
     * @return true if it is possible and false if it is not possible
     */
    public boolean possible(Date date) {
        return (possibleWeekDays.possible(date) && possibleDates.possible(date) && possibleHours.possible(date));
    }

    /**
     * Changes the dates to the first time that is possible before the given date, if it is not possible now
     * hours until can maximum be cut by half
     * if its not possible to find a time before it will return the next possible time.
     *
     * @param date                   the date that is decided now and is going to be adjusted in the method
     * @param hoursUntilNotification the number of hours from this time until the notification is going to be sent
     * @return the new date for the notification
     */
    public Date changeToReasonableTime(Date date, long hoursUntilNotification) {
        int hoursUntil = possibleHours.hoursUntilPossible(date);
        int hoursBefore = possibleHours.hoursBeforePossible(date);
        if (hoursBefore < hoursUntil && (hoursUntilNotification / 2) >= hoursBefore) {
            long millisecondsOriginDate = date.getTime();
            long milliSecondsBefore = hoursBefore * 1000 * 60 * 60;
            date.setTime(millisecondsOriginDate - milliSecondsBefore);
        } else {
            long millisecondsOriginDate = date.getTime();
            long milliSecondsUntil = hoursUntil * 1000 * 60 * 60;
            date.setTime(millisecondsOriginDate + milliSecondsUntil);
        }
        return date;
    }
}
