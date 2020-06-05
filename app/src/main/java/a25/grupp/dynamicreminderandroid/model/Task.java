package a25.grupp.dynamicreminderandroid.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * This class represents a task, it handles all the information regarding the task
 * such as id, title, notes, preferred interval, possible time, last preformed and notifications
 * This class is serializable
 *
 * @author Anni Johansson, Minna Röriksson, Hanna My Jansson
 * @version 1.3
 */
public class Task implements Comparable<Object>, Serializable {
    private int id;
    private String title;
    private String notes;
    private TimeSpan preferredInterval;
    private TimeSpan maximum;
    private PossibleTime possibleTimeForExecution;
    private Date lastPerformed;
    private Notification nextNotification;

    public Task(int taskId) {
        id = taskId;
        setPossibleHours(8, 22);
    }

    public Task(String title, String notes, TimeSpan preferredInterval, TimeSpan maximum, PossibleTime possibleTimeForExecution) {
        this.title = title;
        this.notes = notes;
        this.preferredInterval = preferredInterval;
        this.maximum = maximum;
        this.possibleTimeForExecution = possibleTimeForExecution;
        setPossibleHours(8, 22);
        markAsDoneNow();
    }

    /**
     * constructs a task with a title, notes and preferredInterval
     * also sets a default value for possible hours
     *
     * @param title             the desired title
     * @param notes             teh notes for the task
     * @param preferredInterval the interval the user wants to have
     */
    public Task(String title, String notes, TimeSpan preferredInterval) {
        this.title = title;
        this.notes = notes;
        this.preferredInterval = preferredInterval;
        setPossibleHours(8, 22);
    }

    /**
     * Changes time for the variabel lastPerformed to the current time.
     */
    public void markAsDoneNow() {
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        lastPerformed = dateNow;
        setNextNotification();
    }

    /**
     * generates a notification, se method generateNotification()
     */
    public void setNextNotification() {
        nextNotification = generateNotification();
    }


    /**
     * Creates a new notification with a randomly generated title
     * uses happy quastions as default and serious questions if the task is a hour or mor late
     * uses the method getDateForNotification() to calculate the time fo the notification
     *
     * @return {@link Notification}
     */
    public Notification generateNotification() {
        Date timeForNotification = getDateForNotification();
        int timeUntilMINUTES = getMinutesUntil();
        String[] questions = getQuestionHappy();
        if (timeUntilMINUTES < -59) {
            questions = getQuestionsSerious();
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(5);
        String randomPhase = questions[randomIndex];
        String message = randomPhase + " " + title + "?";
        nextNotification = new Notification(this, timeForNotification, message);
        return nextNotification;
    }

    /**
     * Returns a list of happy questions
     *
     * @return String[]
     */
    private String[] getQuestionHappy() {
        String[] happyQuestion = {"Isn't this the perfect time to",
                "Sunshine, maybe it's time to",
                "Can you please make time to",
                "It's wonderful if you can make time to",
                "Wow, isn't it time for you to",
                "Friendly reminder, have you remembered to"};

        return happyQuestion;
    }

    /**
     * Returns a list of Serious questions
     *
     * @return String[]
     */
    private String[] getQuestionsSerious() {
        String[] seriousQuestion = {"Have you remembered to",
                "Isn't it time for you to",
                "Another reminder, have you remembered to",
                "Did you remember to",
                "Please, can you make time to",
                "Pleeeease, maybe it's time for you to"};

        return seriousQuestion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TimeSpan getPreferredInterval() {
        return preferredInterval;
    }

    public void setPreferredInterval(TimeSpan preferredInterval) {
        this.preferredInterval = preferredInterval;
    }

    //method can be used for further development
    public TimeSpan getMaximum() {
        return maximum;
    }

    //method can be used for further development
    public void setMaximum(TimeSpan maximum) {
        this.maximum = maximum;
    }

    //method can be used for further development
    public PossibleTime getPossibleTimeForExecution() {
        return possibleTimeForExecution;
    }

    public void setPossibleTimeForExecution(PossibleTime possibleTimeForExecution) {
        this.possibleTimeForExecution = possibleTimeForExecution;
    }

    public Date getLastPerformed() {
        return lastPerformed;
    }

    public void setLastPerformed(Date lastPerformed) {
        this.lastPerformed = lastPerformed;
    }

    public Notification getNextNotification() {
        return nextNotification;
    }

    //method can be used for further development
    public boolean[] getPossibleDates() {
        return possibleTimeForExecution.getPossibleDates();
    }

    //method can be used for further development
    public boolean[] getPossibleWeekdays() {
        return possibleTimeForExecution.getPossibleWeekdays();
    }

    public TimeInterval getPossibleHour() {
        return possibleTimeForExecution.getPossibleHours();
    }

    public void setPossibleHours(int from, int to) {
        possibleTimeForExecution.setPossibleHours(from, to);
    }

    /**
     * Calculates the minutes until the task is to be preformed from the current date and the date of last preformed
     * uses the {@link TimeInterval} method getInMinutes()
     *
     * @return an int with minutes until
     */
    public int getMinutesUntil() {
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();

        long millisecondsNOW = dateNow.getTime();
        long millisecondsDONE = lastPerformed.getTime();
        System.out.println("Task " + id + " - getMinutesUntil() - lastpreformed date: " + lastPerformed);
        long millisecondsDIFFERENCE = millisecondsNOW - millisecondsDONE;
        long minutesDIFFERENCE = millisecondsDIFFERENCE / 60000;

        int preferredIntervalInMinutes = preferredInterval.getInMinutes();
        int timeUntilMINUTES = (int) (preferredIntervalInMinutes - minutesDIFFERENCE);
        System.out.println("Task " + id + " - getMinutesUntil() - timeUntil in minutes: " + timeUntilMINUTES);
        return timeUntilMINUTES;
    }

    /**
     * Calculates how many hours, days, weeks, months or years that are left until the task should be preformed
     * it chooses primarily the unit that was chosen when entering the task
     * switches unit when time passes and the time left is lower then one of the current timeunit
     *
     * @return
     */
    public TimeSpan getTimeUntil() {
        TimeUnit timeUnit = preferredInterval.getTimeUnit();
        int time = preferredInterval.getTime();
        int timeUntil = -5;

        //If the task is not yet done, return the preferred interval
        if (lastPerformed == null) {
            timeUntil = time;
        } else {
            int timeUntilMINUTES = getMinutesUntil();
            if (timeUntilMINUTES <= 60 * 23 && timeUntilMINUTES >= -60 * 23) {
                timeUnit = TimeUnit.hour;
            } else if (timeUntilMINUTES <= 60 * 24 * 6 && timeUntilMINUTES > -60 * 24 * 7 && timeUnit.equals(TimeUnit.week)) {
                timeUnit = TimeUnit.day;
            } else if (timeUntilMINUTES <= 60 * 24 * 29 && timeUntilMINUTES > -60 * 24 * 30 && (timeUnit.equals(TimeUnit.month) || timeUnit.equals(TimeUnit.year))) {
                timeUnit = TimeUnit.day;
            } else if (timeUntilMINUTES <= 60 * 24 * 364 && timeUntilMINUTES > -60 * 24 * 365 && timeUnit.equals(TimeUnit.year)) {
                timeUnit = TimeUnit.month;
            }

            //calculates the time left of the task and returns it in the unit specified in preferredInterval.
            if (timeUntilMINUTES < 0) {
                switch (timeUnit) {
                    case hour:
                        timeUntil = timeUntilMINUTES / 60;
                        break;
                    case day:
                        timeUntil = timeUntilMINUTES / (24 * 60);
                        break;
                    case week:
                        timeUntil = timeUntilMINUTES / (7 * 24 * 60);
                        break;
                    case month:
                        timeUntil = timeUntilMINUTES / (30 * 24 * 60);
                        break;
                    case year:
                        timeUntil = timeUntilMINUTES / (365 * 24 * 60);
                        break;
                }
            } else {

                switch (timeUnit) {
                    case hour:
                        timeUntil = (timeUntilMINUTES - 1) / 60 + 1;
                        break;
                    case day:
                        timeUntil = (timeUntilMINUTES - 1) / (24 * 60) + 1;
                        break;
                    case week:
                        timeUntil = (timeUntilMINUTES - 1) / (7 * 24 * 60) + 1;
                        break;
                    case month:
                        timeUntil = (timeUntilMINUTES - 1) / (30 * 24 * 60) + 1;
                        break;
                    case year:
                        timeUntil = (timeUntilMINUTES - 1) / (365 * 24 * 60) + 1;
                        break;
                }
            }
            System.out.println("Task " + id + " - getTimeUntil() - timeUntil in minutes: " + timeUntilMINUTES + " time Until: " + timeUntil + " " + timeUnit);
        }
        TimeSpan interval = new TimeSpan(timeUntil, timeUnit);
        return interval;
    }

    /**
     * Calculates the date that the notification should come
     * uses the method getMinutesUntil() to set the time,
     * if the timeuntil is less than 0, the method intervalGroups() is used to decide how soon the notification should come again
     *
     * @return the date for the notification
     */
    public Date getDateForNotification() {
        Date date = new Date();
        int timeUntilMINUTES = getMinutesUntil();
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        long millisecondsNOW = dateNow.getTime();

        long millisecondsThen = 0;
        long millisecondsUntil = 0;
        if (timeUntilMINUTES > 0) {

            millisecondsUntil = (long) timeUntilMINUTES * 60000;
            millisecondsThen = millisecondsNOW + millisecondsUntil;
        } else {
            millisecondsUntil = intervalGroups();
            millisecondsThen = millisecondsNOW + millisecondsUntil;
        }
        date.setTime(millisecondsThen);
        System.out.println("Task " + id + " - getDateForNotification date = " + date);
        boolean possible = possibleTimeForExecution.getPossibleHours().isInInterval(date);
        if (!possible) {
            date = adjustToPossibleTime(date, millisecondsUntil / (1000 * 60 * 60));
        }
        System.out.println("Task " + id + " - getDateForNotification adjusted date = " + date);
        return date;
    }

    /**
     * Adjust the date for the notification so it fitts with the possible time
     *
     * @param date                   the current planed date for the notification
     * @param hoursUntilNotification the hours from now to the planned date
     * @return an new Date object with the adjusted time
     */
    private Date adjustToPossibleTime(Date date, long hoursUntilNotification) {
        Date adjustedDate = possibleTimeForExecution.changeToReasonableTime(date, hoursUntilNotification);
        return adjustedDate;
    }

    /**
     * Decides the time until next interval when it is the second reminder.
     * the time increase depending on the normal interval for the task
     *
     * @return a long with the milliseconds
     */
    public long intervalGroups() {
        long nextInterval = 0;
        int intervalInMinutes = preferredInterval.getInMinutes();
        if (intervalInMinutes <= 120) { //under 2h
            nextInterval = 15 * 60000; //15m in milliseconds
        } else if (intervalInMinutes >= 120 && intervalInMinutes <= 240) { //between 2h and 4h
            nextInterval = 30 * 60000; //30m in milliseconds
        } else if (intervalInMinutes >= 240 && intervalInMinutes <= 1440) { //between 4h and 1d
            nextInterval = 60 * 60000; //1h in milliseconds
        } else if (intervalInMinutes >= 1441 && intervalInMinutes <= 4320) { //between 1d and 3d
            nextInterval = 180 * 60000; //2h in milliseconds
        } else if (intervalInMinutes >= 4321 && intervalInMinutes <= 8640) { //between 3d and 6d
            nextInterval = 480 * 60000; //3h in milliseconds
        } else if (intervalInMinutes >= 8641 && intervalInMinutes <= 20159) { //between 6d and 2w
            nextInterval = 1440 * 60000; //1d in milliseconds
        } else if (intervalInMinutes >= 20160 && intervalInMinutes <= 43799) { //between 2w ands 1m
            nextInterval = 2880 * 60000; //2d in milliseconds
        } else if (intervalInMinutes >= 43800 && intervalInMinutes <= 131399) { //between 1m and 3m
            nextInterval = 4320 * 60000; // 3d in milliseconds
        } else if (intervalInMinutes >= 131400 && intervalInMinutes <= 1051200) { //between 3m and 2y
            nextInterval = 10080 * 60000; //1w in milliseconds
        } else { //over 2y
            nextInterval = 20160 * 60000; //2w in milliseconds
        }
        return nextInterval;
    }

    //method can be used for further development
    public TimeUnit getTimeUnit() {
        return preferredInterval.getTimeUnit();
    }


    public int getId() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int compareTo(Object o) {
        Task t = (Task) o;
        return Integer.compare(id, t.getId());
    }

    /**
     * generates a string with basic info
     *
     * @return a string
     */
    public String toString() {
        String str = "Task id: " + id + "\nTask title: " + title + "\nTask notes: " + notes;
        return str;
    }

}