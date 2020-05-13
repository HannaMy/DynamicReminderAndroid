package a25.grupp.dynamicreminderandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;

public class ButtonReceiver extends BroadcastReceiver {

    TaskRegister taskRegister;

    @Override
    public void onReceive(Context context, Intent intent) {
         taskRegister = TaskRegister.getInstance(context);
        System.out.println("ButtonReceiver");
        String action = intent.getStringExtra("action");
        int taskId = intent.getIntExtra("taskId", 0);
        if(action!=null) {
            if (action.equals("yesNow")) {
                System.out.println("NotificationREceiver - yeNow btn clicked");
                markAsDone(taskId);
            } else if (action.equals("no")) {
                System.out.println("NotificationREceiver - no btn clicked");
                remindAgain(taskId);
            }
        }




    }


    private void markAsDone(int taskId){
     Task task = taskRegister.getTaskWithId(taskId);
     task.markAsDoneNow();
    }

    private void remindAgain(int taskId){
        Task task = taskRegister.getTaskWithId(taskId);
        task.generateNotification();
        //TODO metod för att generera ny notifikation
    }
}
