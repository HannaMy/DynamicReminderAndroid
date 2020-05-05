package a25.grupp.dynamicreminderandroid.model;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.*;

/**
 * Stores the data on file
 *
 * @author Minna Röriksson, Hanna My Jansson
 * @version 1.1
 */

public class FileHandler {
    private TaskRegister taskRegister;
    private Context context;

    public FileHandler(Context context) {
        this.context = context;
    }

    /**
     * saves a taskregister object to a file
     *
     * @param taskRegister the object that is to be saved
     */

    public void saveToFile(TaskRegister taskRegister) { //Eventuellt måste en lägga till en Context också
        try {
            FileOutputStream fos = context.openFileOutput("taskRegisterFile", Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(taskRegister);

            oos.close();
            fos.close();
            // System.out.println("FileHandler: taskregister written: " + taskRegister.toString());
        } catch (FileNotFoundException e) {
            System.out.println("FileHandler: File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads a taskregister object from a file
     *
     * @return the taskregister from the file
     */
    public TaskRegister readFromFile() {
        try {
            FileInputStream fis = context.openFileInput("taskRegisterFile");
            //BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(fis);
            taskRegister = (TaskRegister) ois.readObject();
            System.out.println("FileHandler: Task read: " + taskRegister.toString());
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
