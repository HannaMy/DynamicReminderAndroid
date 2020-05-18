package a25.grupp.dynamicreminderandroid.model;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import a25.grupp.dynamicreminderandroid.R;

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
    private Context context;

    public Task() {

    }

    public Task(int taskId) {
        id = taskId;
    }

    public Task(String title, String info, TimeSpan preferredInterval, TimeSpan maximum, PossibleTime possibleTimeForExecution, Context context) {
        this.title = title;
        this.info = info;
        this.preferredInterval = preferredInterval;
        this.maximum = maximum;
        this.possibleTimeForExecution = possibleTimeForExecution;
        this.context = context;
        markAsDoneNow();
    }

    public Task(String title, String info, TimeSpan preferredInterval, Context context) {
        this.title = title;
        this.info = info;
        this.preferredInterval = preferredInterval;
        this.context = context;
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

    /**
     * Creates a new notification with a randomly generated title
     * @return {@link Notification}
     */
    public Notification generateNotification() {
        Date timeForNotification = getDateForNotification();


        //TODO: Jämför om det är första påminnelsen eller senare. I så fall byt string array.
        String[] questions = context.getResources().getStringArray(R.array.questionsHappy);
        Random rand = new Random();
        int randomIndex = rand.nextInt(5);
        String randomPhase = questions[randomIndex];
        String message = randomPhase + " " + title + "?";

        //TODO: kontrollera om det finns custom possible time
        if (possibleTimeForExecution != null) {
            //kod som anpassar timeForNotification så att det stämmer överens med possible time
        }
        nextNotification = new Notification(this, timeForNotification, message);
        return nextNotification;
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

    public void setNextNotification(Notification nextNotification) {
        this.nextNotification = nextNotification;
    }


    public boolean[] getPossibleDates(){
        return possibleTimeForExecution.getPossibleDates();
    }

    public boolean[] getPossibleWeekdays(){
        return possibleTimeForExecution.getPossibleWeekdays();
    }

    public TimeInterval getPossibleTimeInterval(){
        return possibleTimeForExecution.getPossibleHours();
    }


    private int getMinutesUntil(){
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        dateNow.compareTo(lastPerformed);

        long millisecondsNOW = dateNow.getTime();
        long millisecondsDONE = lastPerformed.getTime();
        long millisecondsDIFFERENCE = millisecondsNOW-millisecondsDONE;
        long minutesDIFFERENCE = millisecondsDIFFERENCE/60000;

        int preferredIntervalInMinutes = preferredInterval.getInMinutes();
        int timeUntilMINUTES = (int) (preferredIntervalInMinutes - minutesDIFFERENCE);

        System.out.println("Time until in minutes = " + timeUntilMINUTES);
        return timeUntilMINUTES;
    }
    public int getTimeUntil(){

            int time = preferredInterval.getTime();

        int timeUntil = -5;

        //If the task is not yet done, return the preferred interval
        if(lastPerformed == null){
            timeUntil = time;
        }else{

            //calculates the time left of the task and returns it in the unit specified in preferredInterval.
         int timeUntilMINUTES = getMinutesUntil();

            switch (preferredInterval.getTimeUnit()) {
                case hour:
                    timeUntil = timeUntilMINUTES/60;
                    break;
                case day:
                    timeUntil = timeUntilMINUTES/(24 * 60);
                    break;
                case week:
                    timeUntil = timeUntilMINUTES/ ( 7 * 24 * 60);
                    break;
                case month:
                    timeUntil = timeUntilMINUTES / (30 * 24 * 60);
                    break;
                case year:
                    timeUntil = timeUntilMINUTES /(365 * 24 * 60) ;
                    break;
            }



            //TODO
        }


        return timeUntil;
    }

    /**
     * Calculates the date that the notification should come
     * @return the date for the notification
     */
    public Date getDateForNotification(){
        Date date = new Date();
        int timeUntilMINUTES = getMinutesUntil();
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        long millisecondsNOW = dateNow.getTime();

        long millisecondsThen = 0;

        if(timeUntilMINUTES >0) {
            long millisecondsUntil = timeUntilMINUTES * 60000;
            millisecondsThen = millisecondsNOW + millisecondsUntil;
        }else{
            millisecondsThen = millisecondsNOW + 1000*60; //TODO lägger på en minut på tiden som är nu för att lättare kunna debugga, SKA vara 1 h eller dynamiskt
            int intervalInMinutes =  preferredInterval.getInMinutes();





        }
        date.setTime(millisecondsThen);
        return date;
    }

    public void intervalGroups(){
        int intervalInMinutes = preferredInterval.getInMinutes();
        if(intervalInMinutes <= 1440){ //under 1h
            //todo 1h
        }else if(intervalInMinutes >= 1441 && intervalInMinutes <= 4320){ //between 1h and 3d
            //todo 2/3/4h?
        } else if(intervalInMinutes >= 4321 && intervalInMinutes <= 8640){ //between 3d and 6d
            //todo 8h
        } else if(intervalInMinutes >= 8641 && intervalInMinutes <= 20159){ //between 6d and 2w
            //todo 1d
        } else if(intervalInMinutes >= 20160 && intervalInMinutes <= 43799){ //between 2w ands 1m
            //todo 2d
        } else if(intervalInMinutes >= 43800 && intervalInMinutes <= 131399){ //between 1m and 3m
            //todo 3d
        } else if(intervalInMinutes >= 131400 && intervalInMinutes <= 1051200){ //between 3m and 2y
            //todo 1w
        } else{ //over 2y
            //todo 2w
        }
    }

    public TimeUnit getTimeUnit(){
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
        return Integer.compare(id,t.getId());
    }

    public String toString(){
        String str = "Task id: " + id + "\nTask title: " + title + "\nTask info: " + info; //todo l�gg till mer sen? om vi vill ha det?
        return str;
    }

}