package a25.grupp.dynamicreminderandroid.controller;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalTime;
import java.util.Date;

import a25.grupp.dynamicreminderandroid.model.FileHandler;
import a25.grupp.dynamicreminderandroid.model.PossibleTime;
import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;
import a25.grupp.dynamicreminderandroid.model.TimeSpan;
import a25.grupp.dynamicreminderandroid.model.TimeUnit;

/**
 * @author Hanna My Jansson, Anni Johansson
 * @version 1.1
 */
public class MainController {

    private ViewController viewController;
    private TaskRegister taskRegister;
    private FileHandler fileHandler;

    public MainController()
    {
        viewController = new ViewController(this);
    }

    // Loading saved data from file to the app
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loadApp(Context context) {

        fileHandler = new FileHandler(context);
        TaskRegister loadedRegister = null;
        try {
            loadedRegister = fileHandler.readFromFile();
        } catch (Exception e) {
            System.err.println("Controller: Fanns ingen fil att läsa.");
        }
        if(loadedRegister!= null)
            taskRegister=loadedRegister;

        System.out.println("Controller: filehandler created");
        loadTasksToGUI();

        // todo Ska vi använda/göra om denna tråd?
       // UpdateThread updateThread = new UpdateThread(this);
        //updateThread.start();
    }

    // Add a new task when clicking SAVE-button in the detailed view
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addTask()
    {
        Task task = null;
        int selectedTaskId = viewController.getSelectedTaskId();

        String title = viewController.getTaskTitle();
        String info = viewController.getNotes();

        //preferredIntervall
        int intervalAmount = 0;
        try {
            intervalAmount = Integer.parseInt(viewController.getIntervalAmount());
        } catch (Exception e) {
           //todo Här ska läggas in ett felmeddelande

        }

        TimeUnit timeUnit = viewController.getIntervalUnit();
        System.out.println("Controller - intervall: " + intervalAmount + " " + timeUnit);
        TimeSpan preferredInterval = new TimeSpan(intervalAmount, timeUnit);

        PossibleTime possibleTime = new PossibleTime();
        possibleTime.setPossibleWeekDays(viewController.getPossibleWeekdays());
        possibleTime.setPossibleDates(viewController.getPossibleDates());
        LocalTime[] localTimes = viewController.getPossibleHours();
        possibleTime.setPossibleHours(localTimes[0], localTimes[1]);

        if (selectedTaskId <= 0) {
            task = new Task(title, info, preferredInterval);
            task.setPossibleTimeForExecution(possibleTime);
           // taskRegister.addTask(task);
            viewController.addTask(task.getTitle(), task.getTimeUntil(), task.getTimeUnit(), task.getId());

        } else {
            task = taskRegister.getTaskWithId(selectedTaskId);
            task.setInfo(info);
            task.setTitle(title);
            task.setPreferredInterval(preferredInterval);
            task.setPossibleTimeForExecution(possibleTime);

        }


        //todo
        updateGUI();
        //viewController.setCard("1");

        fileHandler.saveToFile(taskRegister); //todo flytta! Just nu sparar den bara 1? tror jag

    }

    public void markTaskAsDone(int taskId) {

        /*
        Task task = taskRegister.getTaskWithId(taskId);
        Date lastPerformed;
        System.out.println("Task id: " + taskId);
        task.markAsDoneNow();
        System.out.println("Last performed: " + task.getLastPerformed().toString());
        lastPerformed = task.getLastPerformed();
        frame.setLastPerformed(lastPerformed);
        updateGUI();

         */
    }

    public void setLastPerformed(int taskId, Date date) {
        Task task = taskRegister.getTaskWithId(taskId);
        task.setLastPerformed(date);
    }


    public void deleteTask(int taskId) {

        /*
        System.out.println("Controller: Delete Task: " + taskId);
        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this task?", "Delete", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            taskRegister.removeWithId(taskId);
            loadTasksToGUI();
            frame.setCard("1");
            fileHandler.saveToFile(taskRegister);
        }

         */

    }

    public void openTask(int taskId) {

        /*
        Task task = taskRegister.getTaskWithId(taskId);
        Date lastPerformed = task.getLastPerformed();
        frame.setSelectedTaskId(taskId);
        frame.setTaskInterval(task.getPreferredInterval().getTime(), task.getTimeUnit());
        frame.setTaskTitle(task.getTitle());
        frame.setNotes(task.getInfo());
        frame.setPossibleDates(task.getPossibleDates());
        frame.setPossibleWeekdays(task.getPossibleWeekdays());
        TimeInterval timeInterval = task.getPossibleTimeInterval();
        if (timeInterval != null)
            frame.setPossibleHours(timeInterval.getFrom(), timeInterval.getTo());

        frame.setLastPerformed(lastPerformed);

        frame.setCard("2");

         */
    }

    public void loadTasksToGUI() {
        /*
        frame.removeTaskList();
        int size = taskRegister.getBiggestID();
        System.out.println("Controller - loadTaskToGUI: taskregisterSize: " + size);
        for (int i = 1; i <= size; i++) {
            Task task = taskRegister.getTaskWithId(i);
            if (task != null) {
                frame.addTask(task.getTitle(), task.getTimeUntil(), task.getTimeUnit(), task.getId());
                System.out.println("Controller - loadTaskToGUI: Add task:" + task.toString());
            }
        }

         */

    }

    public void updateGUI() {
        /*
        frame.updateAllTasks();

         */
    }


    public String getTaskTitle(int id) {
        return taskRegister.getTaskWithId(id).getTitle();
    }

    public int getTaskTimeRemaining(int id) {
        return taskRegister.getTaskWithId(id).getTimeUntil();
    }


}
