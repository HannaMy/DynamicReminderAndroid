package a25.grupp.dynamicreminderandroid.model;

import java.io.*;


public class FileHandler {
    private TaskRegister taskRegister;

    public FileHandler(){

    }

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
