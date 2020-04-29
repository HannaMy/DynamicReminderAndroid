package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;

/**
 * Timespan sets the intervalls beetween how often a task needs to be done.
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

    @Override
    public String toString() {
        return "Every " + time + " " + timeUnit;

        //TODO vad skriver man på engelska istället för e i "var 3e dag"?
    }
}
