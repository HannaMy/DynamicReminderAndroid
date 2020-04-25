package a25.grupp.dynamicreminderandroid.controller;

import android.content.Intent;

import java.time.LocalTime;
import java.util.Date;

import a25.grupp.dynamicreminderandroid.DetailActivity;
import a25.grupp.dynamicreminderandroid.MainActivity;
import a25.grupp.dynamicreminderandroid.controller.MainController;
import a25.grupp.dynamicreminderandroid.model.TimeUnit;

public class ViewController {

    private MainController mainController;


    public ViewController(MainController mainController)
    {
        this.mainController = mainController;
    }


    public void showDetails(){

    }

    public void showDetails(int taskId){
        //TODO;
    }

    public void showOverview(){
    //TODO
    }


    public String getTaskTitle() {
        //TODO;
        return null;
    }

    public void setTaskTitle(String title) {
        //TODO;
    }

    public String getIntervalAmount() {
        //TODO;
        return null;
    }


    public TimeUnit getIntervalUnit() {
        //TODO;
        return null;
    }

    public void setTaskInterval(int amount, TimeUnit unit) {
        //TODO;
    }

    public LocalTime[] getPossibleHours() {
        //TODO;
        return null;
    }

    public void setPossibleHours(LocalTime from, LocalTime to) {
       //TODO
    }

    public boolean[] getPossibleDates() {
        //TODO;
        return null;
    }

    public void setPossibleDates(boolean[] dates) {
      //TODO
    }

    public boolean[] getPossibleWeekdays() {
        //TODO;
        return null;
    }

    public void setPossibleWeekdays(boolean[] possibleWeekdays) {
        //TODO;
    }

    public String getNotes(){
        //TODO;
        return null;
    }

    public void setNotes(String notes) {
        //TODO;
    }

    public void setLastPerformed(Date lastPerformed) {
        //TODO;
    }

    public void addTask(String title, int timeRemaining, TimeUnit timeUnit, int taskId) {
        //TODO;
    }

    public void updateAllTasks() {
        //TODO;
    }

    public void setDefaultFields() {
        //TODO;
    }

    public int getSelectedTaskId() {
        //TODO;
        return 0;

    }

    public void removeTaskList() {
        //TODO;
    }

    public void setSelectedTaskId(int id) {
        //TODO;
    }

}



