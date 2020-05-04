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
            FileOutputStream fos = context.openFileOutput("taskRegisterFile", Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(taskRegister);

            oos.close();
            fos.close();
           // System.out.println("FileHandler: taskregister written: " + taskRegister.toString());
        }catch(FileNotFoundException e){
            System.out.println("FileHandler: File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveToFileString(String string){ //Eventuellt måste en lägga till en Context också
        try{
            FileOutputStream fos = context.openFileOutput("str", Context.MODE_APPEND);
            //BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(string);
            oos.close();
            System.out.println("Oos: "+ oos + ", string: " + string);
            // System.out.println("FileHandler: taskregister written: " + taskRegister.toString());
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
            FileInputStream fis = context.openFileInput("taskRegisterFile");
            //BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(fis);
            taskRegister =  (TaskRegister)ois.readObject();
            System.out.println("FileHandler: Task read: " + taskRegister.toString());
            fis.close();
            ois.close();
        }catch(FileNotFoundException e){
            System.out.println("FileHandler: File not found");
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return taskRegister;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readFromFileString(){
        System.out.println("read from file");
        String string = "";
        try
        {
            FileInputStream fis = context.openFileInput("str");
            System.out.println("fis: " + fis);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(fis);
            System.out.println("ois: " + ois);
            string =  (String)ois.readObject();
            System.out.println("ois: " + ois + " , string: " + string);
            //System.out.println("FileHandler: Task read: " + taskRegister.toString());
            fis.close();
            ois.close();
        }catch(FileNotFoundException e){
            System.out.println("FileHandler: File not found");
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return string;
    }

}
