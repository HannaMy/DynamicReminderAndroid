package a25.grupp.dynamicreminderandroid.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a task .....
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

    }

    public Task(int taskId)
    {
        id= taskId;
    }

    //TODO: Flera konstruktorer eller skicka in null och ?? i parametrarna n�r vissa val saknas?
    public Task(String title, String info, TimeSpan preferredInterval, TimeSpan maximum, PossibleTime possibleTimeForExecution)
    {
        this.title = title;
        this.info = info;
        this.preferredInterval = preferredInterval;
        this.maximum = maximum;
        this.possibleTimeForExecution = possibleTimeForExecution;
        markAsDoneNow();
        nextNotification = generateNotification();


    }
    public Task(String title, String info, TimeSpan preferredInterval)
    {
        this.title = title;
        this.info = info;
        this.preferredInterval = preferredInterval;
        nextNotification = generateNotification();
        markAsDoneNow();

    }

    public void performed(Date date)
    {

    }

    public void markAsDoneNow(){
        Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        lastPerformed = dateNow;
        //TODO: skriva metod som avbryter nästa notifikation och skapar en ny
    }

    public Notification generateNotification(){
        Date timeForNotification = new Date();
        String randomPhase = "Did you remember to ";//TODO: hämta en random fråga
        String message = randomPhase + title + "?";

        //TODO: kontrollera om det finns custom possible time
        if (possibleTimeForExecution != null){
            //kod som anpassar timeForNotification så att det stämmer överens med possible time
        }

        timeForNotification.setTime(getTimeUntil());
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

    public int getTimeUntil(){
        int time = preferredInterval.getTime();
        int timeUntil = -5;

        //If the task is not yet done, return the preferred interval
        if(lastPerformed == null){
            timeUntil = time;
        }else{

            //calculates the time left of the task and returns it in the unit specified in preferredInterval.
            Calendar cal = Calendar.getInstance();
            Date dateNow = cal.getTime();
            dateNow.compareTo(lastPerformed);

            long millisecondsNOW = dateNow.getTime();
            long millisecondsDONE = lastPerformed.getTime();
            long millisecondsDIFFERENCE = millisecondsNOW-millisecondsDONE;
            long minutesDIFFERENCE = millisecondsDIFFERENCE/60000;

            int preferredIntervalInMinutes = preferredInterval.getInMinutes();
            int timeUntilMINUTES = (int) (preferredIntervalInMinutes - minutesDIFFERENCE);

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