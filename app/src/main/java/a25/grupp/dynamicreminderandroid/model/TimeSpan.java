package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;

/**
 * TimeSpan sets the interval beetween how often a task needs to be done.
 *
 * @author Hanna Ringkvist
 * @version 1.0
 */

public class TimeSpan implements Serializable {
    private int time;
    private TimeUnit timeUnit;

    public TimeSpan(int time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }


    /**
     * Changes the timeunit to minutes
     * @return a number of how many minutes the interval should be
     */
    public int getInMinutes() {
        int minutes = 0;
        switch (timeUnit) {
            case hour:
                minutes = time * 60;
                break;
            case day:
                minutes = time * 24 * 60;
                break;
            case week:
                minutes = time * 7 * 24 * 60;
                break;
            case month:
                minutes = time * 30 * 24 * 60;
                break;
            case year:
                minutes = time * 365 * 24 * 60;
                break;

        }
        return minutes;

        //TODO
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }


    /**
     * Creates a String that explains the interval
     * @return a String that explains the interval
     */
    @Override
    public String toString() {
        return "Every " + time + " " + timeUnit;

        //TODO vad skriver man på engelska istället för e i "var 3e dag"?
    }
}
