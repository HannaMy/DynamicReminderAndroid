package a25.grupp.dynamicreminderandroid.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * This class represents a task
 *
 * @author Anni Johansson
 * @version 1.0
 */
public class Task implements Comparable<Object>, Serializable {
    private int id;
    private String title;
    private String info;
    private TimeSpan preferredInterval;
    private TimeSpan maximum;
    private PossibleTime possibleTimeForExecution;
    private Date lastPerformed;
    private Notification nextNotification;


    public Task()
    {
        setPossibleHours(8,22);
    }

    public Task(int taskId) {
        id = taskId;
        setPossibleHours(8,22);
    }

    public Task(String title, String info, TimeSpan preferredInterval, TimeSpan maximum, PossibleTime possibleTimeForExecution) {
        this.title = title;
        this.info = info;
        this.preferredInterval = preferredInterval;
        this.maximum = maximum;
        this.possibleTimeForExecution = possibleTimeForExecution;
        setPossibleHours(8,22);
        markAsDoneNow();
    }

    public Task(String title, String info, TimeSpan preferredInterval) {
        this.title = title;
        this.info = info;
        this.preferredInterval = preferredInterval;
        markAsDoneNow();
    }

    public void performed(Date date) {

    }

    /**
     * Changes time for the variabel lastPerformed to the current time.
     */
    public void markAsDoneNow() {
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        lastPerformed = dateNow;
        //TODO: skriva metod som avbryter nästa notifikation och skapar en ny
    }

    public void setNextNotification() {
        nextNotification = generateNotification();
    }

    public void setNextNotification(Notification nextNotification) {
        this.nextNotification = nextNotification;
    }

    /**
     * Creates a new notification with a randomly generated title
     *
     * @return {@link Notification}
     */
    public Notification generateNotification() {
        Date timeForNotification = getDateForNotification();

        //TODO: Jämför om det är första påminnelsen eller senare. I så fall byt string array.
        String[] questions = getQuestionHappy();
        Random rand = new Random();
        int randomIndex = rand.nextInt(5);
        String randomPhase = questions[randomIndex];
        String message = randomPhase + " " + title + "?";

        //TODO: kontrollera om det finns custom possible time
        if (!possibleTimeForExecution.possible(timeForNotification)) {
            //kod som anpassar timeForNotification så att det stämmer överens med possible time

        }
        nextNotification = new Notification(this, timeForNotification, message);
        return nextNotification;
    }

    private String[] getQuestionHappy()
    {
        String[] happyQuestion = {"Isn't this the perfect time to",
            "Maybe it's time to",
            "Just a friendly reminder to",
            "I just wanted to remind you to",
            "Seems like a good time to",
            "Such a nice day to"};

        return happyQuestion;
    }

    private String[] getQuestionsSerious()
    {
        String[] seriousQuestion = {"Don't forget to",
                "It really is time to",
                "Don't forget that you're supposed to",
                "Did you remember to",
                "Finish what you're doing and then",
                "Pleeeease don't forget to"};

        return seriousQuestion;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public TimeSpan getPreferredInterval() {
        return preferredInterval;
    }

    public void setPreferredInterval(TimeSpan preferredInterval) {
        this.preferredInterval = preferredInterval;
    }

    public TimeSpan getMaximum() {
        return maximum;
    }

    public void setMaximum(TimeSpan maximum) {
        this.maximum = maximum;
    }

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


    public boolean[] getPossibleDates() {
        return possibleTimeForExecution.getPossibleDates();
    }

    public boolean[] getPossibleWeekdays() {
        return possibleTimeForExecution.getPossibleWeekdays();
    }

    public TimeInterval getPossibleHour() {
        return possibleTimeForExecution.getPossibleHours();
    }


    public void setPossibleHours(int from, int to) {
        possibleTimeForExecution.setPossibleHours(from, to);
    }


    public int getMinutesUntil() {
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        dateNow.compareTo(lastPerformed);//Gör väl inget?

        long millisecondsNOW = dateNow.getTime();
        long millisecondsDONE = lastPerformed.getTime();
        long millisecondsDIFFERENCE = millisecondsNOW - millisecondsDONE;
        long minutesDIFFERENCE = millisecondsDIFFERENCE / 60000;

        int preferredIntervalInMinutes = preferredInterval.getInMinutes();
        int timeUntilMINUTES = (int) (preferredIntervalInMinutes - minutesDIFFERENCE);

        System.out.println("Time until in minutes = " + timeUntilMINUTES);
        return timeUntilMINUTES;
    }

    public int getTimeUntil() {

        int time = preferredInterval.getTime();

        int timeUntil = -5;

        //If the task is not yet done, return the preferred interval
        if (lastPerformed == null) {
            timeUntil = time;
        } else {

            //calculates the time left of the task and returns it in the unit specified in preferredInterval.
            int timeUntilMINUTES = getMinutesUntil();

            switch (preferredInterval.getTimeUnit()) {
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


            //TODO
        }


        return timeUntil;
    }

    /**
     * Calculates the date that the notification should come
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
            millisecondsUntil = timeUntilMINUTES * 60000;
            millisecondsThen = millisecondsNOW + millisecondsUntil;
        } else {
            millisecondsUntil = intervalGroups();
            millisecondsThen = millisecondsNOW + millisecondsUntil;
            //millisecondsThen = millisecondsNOW + 1000 * 60; //TODO lägger på en minut på tiden som är nu för att lättare kunna debugga, SKA vara 1 h eller dynamiskt


        }
        date.setTime(millisecondsThen);

        date = adjustToPossibleTime(date, millisecondsUntil / (1000 * 60 * 60));
        return date;
    }

    private Date adjustToPossibleTime(Date date, long hoursUntilNotification) {
           Date adjustedDate =  possibleTimeForExecution.changeToReasonableTime(date, hoursUntilNotification);

           return  adjustedDate;
    }

    public long intervalGroups() {
        long nextInterval = 0;
        int intervalInMinutes = preferredInterval.getInMinutes();
        if (intervalInMinutes <= 1440) { //under 1d
            nextInterval = 60;
        } else if (intervalInMinutes >= 1441 && intervalInMinutes <= 4320) { //between 1d and 3d
            nextInterval = 180 * 60000; //1h in milliseconds
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

    public String toString() {
        String str = "Task id: " + id + "\nTask title: " + title + "\nTask info: " + info; //todo l�gg till mer sen? om vi vill ha det?
        return str;
    }

}