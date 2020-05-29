package a25.grupp.dynamicreminderandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;

/**
 * @author Hanna My Jansson, Cornelia Sköld
 */
public class ButtonReceiver extends BroadcastReceiver {
    TaskRegister taskRegister;

    /**
     * This method handles the actions from the notification
     *
     * @param context is the context from where the notification was created
     * @param intent  is the intent created by the action in the notification
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        taskRegister = TaskRegister.getInstance(context);
        System.out.println("ButtonReceiver");
        String action = intent.getStringExtra("action");
        int taskId = intent.getIntExtra("taskId", 0);
        if (action != null) {
            if (action.equals("didItNow")) {
                System.out.println("NotificationREceiver - yeNow btn clicked");
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                notificationManagerCompat.cancel(taskId);
                markAsDone(taskId);
                Toast.makeText(context,
                        "Good for you!",
                        Toast.LENGTH_LONG)
                        .show();
            } else if (action.equals("no")) {
                System.out.println("NotificationReceiver - no btn clicked");
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                notificationManagerCompat.cancel(taskId);
                remindAgain(taskId);
                Toast.makeText(context,
                        "I'll remind you later then!",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }

    }

    /**
     * This method mark the task that belongs to the notification as performed now
     *
     * @param taskId is the task that the notification belongs to
     */
    private void markAsDone(int taskId) {
        Task task = taskRegister.getTaskWithId(taskId);
        task.markAsDoneNow();
    }

    /**
     * This method mark the task that belongs to the notification as not performed
     *
     * @param taskId is the task that the notification belongs to
     */
    private void remindAgain(int taskId) {
        Task task = taskRegister.getTaskWithId(taskId);
        task.setNextNotification();
    }
}
