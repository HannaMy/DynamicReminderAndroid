package a25.grupp.dynamicreminderandroid.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.*;


public class FileHandler {
    private TaskRegister taskRegister;

    public FileHandler(){

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveToFile(TaskRegister taskRegister){
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("files/taskregister")))){
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
        try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("files/taskregister")))){
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
