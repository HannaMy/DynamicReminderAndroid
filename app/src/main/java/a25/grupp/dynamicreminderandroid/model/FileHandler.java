package a25.grupp.dynamicreminderandroid.model;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Stores the data on file
 *
 * @author Minna Röriksson, Hanna My Jansson, Cornelia Sköld
 * @version 1.3
 */

public class FileHandler {
    private TaskRegister taskRegister;
    private Context context;

    public FileHandler(Context context) {
        this.context = context;
    }

    /**
     * Saves a TaskRegister object to a file
     *
     * @param taskRegister the object that is to be saved
     */

    public void saveToFile(TaskRegister taskRegister) {
        try {
            FileOutputStream fos = context.openFileOutput("taskRegisterFile", Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(taskRegister);
            oos.flush();
            oos.close();
            bos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileHandler: File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a TaskRegister object from a file
     *
     * @return the TaskRegister from the file
     */
    public TaskRegister readFromFile() {
        try {
            FileInputStream fis = context.openFileInput("taskRegisterFile");
            ObjectInputStream ois = new ObjectInputStream(fis);
            taskRegister = (TaskRegister) ois.readObject();
            fis.close();
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileHandler: File not found");
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return taskRegister;
    }
}
