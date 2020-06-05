package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Stores the hours possible, for a task to be done, in an array.
 *
 * @author Hanna My Jansson
 * @version 1.0
 */
public class PossibleHours implements Serializable {
    private boolean whenever;
    private TimeInterval[] intervals;
    private int freeIntervalIndex;
    private static final int MAX_NBR_OF_INTERVALS = 10;

    public PossibleHours() {
        intervals = new TimeInterval[MAX_NBR_OF_INTERVALS];
        whenever = true;
        freeIntervalIndex = 0;
    }

    /**
     * Adds a interval to the array of intervals
     *
     * @param from, the beginning of the interval
     * @param to,   object then end of the interval
     */
    public void addInterval(Date from, Date to) {
        if (freeIntervalIndex >= MAX_NBR_OF_INTERVALS) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        int timeFrom = cal.get(Calendar.HOUR);

        cal.setTime(to);
        int timeTo = cal.get(Calendar.HOUR);

        intervals[freeIntervalIndex].setInterval(timeFrom, timeTo);
        whenever = false;
    }

    public TimeInterval getPossibleTimeInterval() {
        return intervals[0];
    }

    /**
     * Adds a interval to the array of intervals
     *
     * @param timeFrom the beginning of the interval
     * @param timeTo   object then end of the interval
     * @return true if it was able to add the interval
     */
    public boolean addInterval(int timeFrom, int timeTo) {
        if (freeIntervalIndex >= MAX_NBR_OF_INTERVALS) {
            return false;
        }
        intervals[freeIntervalIndex].setInterval(timeFrom, timeTo);
        whenever = false;
        return true;
    }

    /**
     * Deletes all intervals in the array
     */
    public void clearAllInterval() {
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = new TimeInterval();
        }
        whenever = true;
    }

    /**
     * Checks if the date is possible
     *
     * @param date the date to be checked
     * @return true if its possible
     */
    public boolean possible(Date date) {
        if (whenever)
            return true;

        boolean possible = false;
        for (TimeInterval interval : intervals) {
            if (interval.isInInterval(date))
                possible = true;
        }
        return possible;
    }

    /**
     * Calculate how many hours until the chosen interval
     *
     * @param date the current date where the interval is added to
     * @return the time until in hours
     */
    public int hoursUntilPossible(Date date) {
        TimeInterval interval = intervals[0];
        int hoursUntil = 0;
        if (interval != null)
            hoursUntil = interval.hoursUntilInterval(date);
        return hoursUntil;
    }

    /**
     * Calculate how many hours since last possible time for execution
     *
     * @param date the date for the last possible time for execution
     * @return the time since last possible time for execution in hours
     */
    public int hoursBeforePossible(Date date) {
        TimeInterval interval = intervals[0];
        int hoursBefore = 0;
        if (interval != null)
            hoursBefore = interval.hoursBeforeInterval(date);
        return hoursBefore;
    }
}
