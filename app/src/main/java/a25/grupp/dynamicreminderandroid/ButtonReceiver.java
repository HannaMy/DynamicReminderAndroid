package a25.grupp.dynamicreminderandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;

/**
 *
 * @author Hanna My Jansson
 */
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
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                notificationManagerCompat.cancel(taskId);
            } else if (action.equals("no")) {
                System.out.println("NotificationREceiver - no btn clicked");
                remindAgain(taskId);
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                notificationManagerCompat.cancel(taskId);
            }
        }

    }

    private void markAsDone(int taskId){
     Task task = taskRegister.getTaskWithId(taskId);
     task.markAsDoneNow();
    }

    private void remindAgain(int taskId){
        Task task = taskRegister.getTaskWithId(taskId);
        task.setNextNotification();
    }
}
