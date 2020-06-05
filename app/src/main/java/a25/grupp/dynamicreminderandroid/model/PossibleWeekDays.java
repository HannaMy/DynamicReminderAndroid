package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Stores the weekdays possible, for a task to be done, in an array.
 *
 * @author Hanna My Jansson
 * @version 1.3
 */
public class PossibleWeekDays implements Serializable {
    private boolean[] possibleWeekdays;

    public PossibleWeekDays() {
        possibleWeekdays = new boolean[7];
        setAllTrue();
    }

    /**
     * sets all the booleans in the array to true
     */

    public void setAllTrue() {
        Arrays.fill(possibleWeekdays, true);
    }

    /**
     * @param possibleWeekdays sets the possible weeksDays
     */
    public void setPossibleWeekdays(boolean[] possibleWeekdays) {
        this.possibleWeekdays = possibleWeekdays;
    }

    /**
     * Returns true if the task is possible to do whenever
     *
     * @return boolean, true if it is possible
     */

    public boolean possibleWhenever() {
        return possibleWeekdays[0] && possibleWeekdays[1] && possibleWeekdays[2] && possibleWeekdays[3] && possibleWeekdays[4] && possibleWeekdays[5] && possibleWeekdays[6];
    }

    /**
     * Get method for possible weekdays
     *
     * @return a boolean array of possible weekdays
     */
    public boolean[] getPossibleWeekdays() {
        return possibleWeekdays;
    }

    /**
     * Checks if the task is possible to perform at the given date
     *
     * @param date, the date that we want to preform the task at
     * @return true if the task is possible to perform at the given date
     */
    public boolean possible(Date date) {
        if (possibleWhenever()) {
            return true;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        return possibleWeekdays[weekday - 1];
    }
}
