package a25.grupp.dynamicreminderandroid.model;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.*;


public class FileHandler {
    private TaskRegister taskRegister;
    private Context context;

    public FileHandler(Context context){
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveToFile(TaskRegister taskRegister){ //Eventuellt måste en lägga till en Context också
        try{
            FileOutputStream fos = context.openFileOutput("taskRegister", Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(taskRegister);
            System.out.println("FileHandler: taskregister written: " + taskRegister.toString());
        }catch(FileNotFoundException e){
            System.out.println("FileHandler: File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public TaskRegister readFromFile(){
        try
        {
            FileInputStream fis = context.openFileInput("taskRegister");
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            taskRegister =  (TaskRegister)ois.readObject();
            System.out.println("FileHandler: Task read: " + taskRegister.toString());
        }catch(FileNotFoundException e){
            System.out.println("FileHandler: File not found");
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return taskRegister;
    }
}
