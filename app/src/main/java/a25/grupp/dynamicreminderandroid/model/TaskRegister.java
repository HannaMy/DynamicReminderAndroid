package a25.grupp.dynamicreminderandroid.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Hanna My Jansson
 * @version 1.0
 */
public class TaskRegister implements Serializable {
    private HashMap<Integer, Task> taskList;
    private static final long serialVersionUID = 655296850; //g�r s� att man kan l�sa fr�n filen
    private int lastId;
    private static final TaskRegister register = new TaskRegister();

    private TaskRegister() {
        taskList = new HashMap();
        lastId = 0;
        loadTaskRegister();
    }

    private void loadTaskRegister() {
        FileHandler fileHandler = new FileHandler();
        TaskRegister loadedRegister = fileHandler.readFromFile();
        register.setTaskList(loadedRegister.getTaskList());
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
            taskList.put(id, task);
        }

    }

    public void removeWithId(int taskId) {

        taskList.remove(taskId);
    }

    public void removeTask(Task task) {
        taskList.remove(task);
    }

    public void removeWithIndex(int index) {
        taskList.remove(index);
    }

    public Task getTaskWithIndex(int index) {
        return taskList.get(index);

    }

    public int getBiggestID() {
        return lastId;
    }

    public int getSize() {
        return taskList.size();
    }

    public Task getTaskWithId(int id) {
        Task task = taskList.get(id);
        return task;
    }

    private int generateId() {
        lastId++;
        return lastId;
    }

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(HashMap<Integer, Task> taskList) {
        this.taskList = taskList;
    }

    public String toString() {
        String str = "";
        for (int i = 1; i <= taskList.size(); i++) {
            str += taskList.get(i).toString() + "\n";
            str += "\n--------------------------------";
        }
        str += "\nLastID: " + lastId;
        str += "\n--------------------------------";
        return str;
    }


}
