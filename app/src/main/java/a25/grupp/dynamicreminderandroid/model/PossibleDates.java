package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Stores the dates possible, for a task to be done, in an array.
 *
 * @author Hanna My Jansson
 * @version 1.3
 */
public class PossibleDates implements Serializable {
    private boolean[] possibleDates;

    public PossibleDates() {
        possibleDates = new boolean[31];
        setAllToTrue();
    }

    /**
     * Sets all values in the boolean array to true
     */

    public void setAllToTrue() {
        Arrays.fill(possibleDates, true);
    }

    /**
     * Replaces the array of possible dates with the parameter possibleDates
     *
     * @param possibleDates the array with the possible dates to be changed to
     */
    public void setPossibleDates(boolean[] possibleDates) {
        this.possibleDates = possibleDates;
    }

    /**
     * Checks if the Task can be performed all the dates
     *
     * @return true if there are no restrictions in dates
     */
    public boolean possibleWhenever() {
        boolean whenever = true;
        for (boolean possibleDate : possibleDates) {
            if (!possibleDate) {
                whenever = false;
                break;
            }
        }
        return whenever;
    }

    public boolean[] getPossibleDates() {
        return possibleDates;
    }

    /**
     * Checks if the date is possible to perform the task
     *
     * @param date the date checked if its a possible date
     * @return true is it is a possible date
     */
    public boolean possible(Date date) {
        if (possibleWhenever()) {
            return true;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekday = cal.get(Calendar.DAY_OF_MONTH);
        return possibleDates[weekday - 1];
    }
}
