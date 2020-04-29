package a25.grupp.dynamicreminderandroid.model;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.HashMap;

import a25.grupp.dynamicreminderandroid.DetailActivity;
import a25.grupp.dynamicreminderandroid.MainActivity;

/**
 * @author Hanna My Jansson
 * @version 1.0
 */
public class TaskRegister implements Serializable {
    private HashMap<Integer, Task> taskHashMap;
    private static final long serialVersionUID = 655296850; //g�r s� att man kan l�sa fr�n filen
    private int lastId;
    private static final TaskRegister register = new TaskRegister();

    private TaskRegister() {
        taskHashMap = new HashMap();
        lastId = 0;
        //loadTaskRegister();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadTaskRegister(Context context) {
        FileHandler fileHandler = new FileHandler(context);
        TaskRegister loadedRegister = fileHandler.readFromFile();
        register.setTaskHashMap(loadedRegister.getTaskHashMap());
        register.setLastId(loadedRegister.getBiggestID());
    }

    public static TaskRegister getInstance() {
        return register;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public void addTask(Task task) {
        if (task != null) {
            int id = generateId();
            task.setID(id);
            taskHashMap.put(id, task);
        }

    }

    public void removeWithId(int taskId) {

        taskHashMap.remove(taskId);
    }

    public void removeTask(Task task) {
        taskHashMap.remove(task);
    }

    public void removeWithIndex(int index) {
        taskHashMap.remove(index);
    }

    public Task getTaskWithIndex(int index) {
        return taskHashMap.get(index);

    }

    public int getBiggestID() {
        return lastId;
    }

    public int getSize() {
        return taskHashMap.size();
    }

    public Task getTaskWithId(int id) {
        Task task = taskHashMap.get(id);
        return task;
    }

    private int generateId() {
        lastId++;
        return lastId;
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public void setTaskHashMap(HashMap<Integer, Task> taskHashMap) {
        this.taskHashMap = taskHashMap;
    }

    public Task[] getTaskArray() {

        Task[] taskArray = new Task[taskHashMap.size()];
        for (int i = 1; i <= lastId; i++) {
            Task task = getTaskWithId(i);
            if (task != null) {
                taskArray[i] = task;

            }
        }
        return taskArray;
    }


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
