package a25.grupp.dynamicreminderandroid.model;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * This class represents a register where all saved tasks a stored
 *
 * @author Hanna My Jansson
 * @version 1.3
 */
public class TaskRegister implements Serializable {
    private HashMap<Integer, Task> taskHashMap;
    private static final long serialVersionUID = 655296850; //g�r s� att man kan l�sa fr�n filen
    private int lastId;
    private static TaskRegister register;

    /**
     * Private Constructor of the TaskRegister, creates a hashmap and sets the lastId to 0
     */
    private TaskRegister() {
        taskHashMap = new HashMap();
        lastId = 0;


    }

    /**
     * loads the taskregister from file
     * @param context
     */

    private void loadTaskRegister(Context context) {
        try {
            FileHandler fileHandler = new FileHandler(context);
            TaskRegister loadedRegister = fileHandler.readFromFile();
            if (loadedRegister != null) {
                register.setTaskHashMap(loadedRegister.getTaskHashMap());
                register.setLastId(loadedRegister.getBiggestID());
            } else {

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Creates a new task register if one does not already exist and returns it
     * @param context the context of the activity calling the method
     * @return the task register
     */
    public static TaskRegister getInstance(Context context) {
        if(register == null) {
            register = new TaskRegister();
            register.loadTaskRegister(context);
        }
        return register;
    }

    /**
     * Sets the lasId which keeps track of which ids has been used
     * only used when loading a saved file
     * @param lastId the value it is to be set to
     */
    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    /**
     * Adds the task to the task register
     * it only adds the task if it is not null
     * also saves the taskregister to file
     * @param task the task that is to be added
     * @param context the context of the activity that adds the task
     */
    public void addTask(Task task, Context context) {
        if (task != null) {
            FileHandler fileHandler = new FileHandler(context);
            int id = generateId();
            if(task.getId()==0) {
                task.setID(id);
            }
            taskHashMap.put(task.getId(), task);
            fileHandler.saveToFile(this);
        }
    }

    /**
     * removes the task with the task id
     * @param taskId tha id of the task that is to be removed
     */
    public void removeWithId(int taskId) {
        taskHashMap.remove(taskId);
    }

    /**
     * saves the taskRegister to file
     * @param context the context of the activity calling for the save
     */
    public void saveRegister(Context context){
        FileHandler fileHandler = new FileHandler(context);
        fileHandler.saveToFile(this);
    }


    /**
     * Returns the highest value of a id generated
     * @return the highest value of a id used in any task
     */
    public int getBiggestID() {
        return lastId;
    }

    /**
     * Returns the size of the taskregister
     * @return the sise of the taskregister
     */
    public int getSize() {
        return taskHashMap.size();
    }

    /**
     * finds the task with that id in tha hashmap and returnes it
     * @param taskId the id of the task
     * @return the task with that task id, returns null if the task could not be found
     */
    public Task getTaskWithId(int taskId) {
        Task task = taskHashMap.get(taskId);
        return task;
    }

    /**
     * returns the next id and adds 1 to the lastId variable
     * @return a id that has not been used yet
     */
    private int generateId() {
        lastId++;
        return lastId;
    }

    /**
     * Returns the hashmap of the taskregister
     * @return the hashmap of the taskregister
     */
    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    /**
     * Sets the hashmap of the taskregister
     * @param taskHashMap the hashMap that is to be set to the register
     */
    public void setTaskHashMap(HashMap<Integer, Task> taskHashMap) {
        this.taskHashMap = taskHashMap;
    }

    /**
     * creates an array with all task in
     * @return the array with all tasks
     */
    public Task[] getTaskArray() {

        Task[] taskArray = new Task[taskHashMap.size()];
        int counter = 0;
        for (int i = 1; i <= lastId; i++) {
            Task task = getTaskWithId(i);
            if (task != null) {
                taskArray[counter] = task;
                counter++;
            }
        }
        Arrays.sort(taskArray, new Comparator<Task>() {
            @Override
            public int compare(Task first, Task second) {
                    return first.getMinutesUntil() - second.getMinutesUntil();
                }
        });

        for (int i = 0; i < taskArray.length; i++){
            System.out.println("TaskRegister efter sortering minutes until: " + taskArray[i].getMinutesUntil());
        }

        Log.i("TaskRegister", "Size of taskArray = " + "" + taskArray.length);
        return taskArray;
    }

    /**
     *  Creates a string with all tasks
     * @return the string with all tasks
     */
    public String toString() {
        String str = "";

        for (int i = 1; i <= taskHashMap.size(); i++) {
            str += taskHashMap.get(i).toString() + "\n";
            str += "\n--------------------------------";
        }
        str += "\nLastID: " + lastId;
        str += "\n--------------------------------";
        return str;
    }
}
