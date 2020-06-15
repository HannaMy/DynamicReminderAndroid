package a25.grupp.dynamicreminderandroid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import a25.grupp.dynamicreminderandroid.model.Notification;
import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;
import a25.grupp.dynamicreminderandroid.model.TimeUnit;

import static a25.grupp.dynamicreminderandroid.R.color;
import static a25.grupp.dynamicreminderandroid.R.id;
import static a25.grupp.dynamicreminderandroid.R.layout;

/**
 * Adapts the data in to the list view in the overview in {@link MainActivity}
 *
 * @author Hanna My Jansson, Minna Röriksson, Cornelia Sköld
 * @version 1.3
 */

public class AdapterTaskOverview extends ArrayAdapter {
    private Activity mainActivity;
    private Task[] taskArray;
    private Context context;

    /**
     * Constructor
     *
     * @param context the context from where the constructor is called
     * @param titles  teh titles of the tasks
     */
    public AdapterTaskOverview(@NonNull Context context, String[] titles) {
        super(context, layout.task_list_item, id.tvTitle, titles);
        this.mainActivity = (MainActivity) context;
        this.context = context;
    }

    /**
     * Sets the array with tasks
     *
     * @param taskArray the array with tasks
     */
    public void setTaskArray(Task[] taskArray) {
        this.taskArray = taskArray;
    }

    /**
     * Returns a message to be received when a task is performed
     *
     * @return a random message
     */
    public String getToastMessage() {
        String[] toastMessages = {"Great job!", "You're doing great!", "Well done!", "Keep up the good work!"
                , "Way to go!", "Fantastic work!", "Wow! Nice work", "Terrific!", "Good for you!",
                "You should be proud!", "That's awesome!", "Keep it up!"};
        Random rand = new Random();
        int randomIndex = rand.nextInt(12);
        return toastMessages[randomIndex];
    }

    /**
     * Generates a view item for the list
     *
     * @param position    of the view
     * @param convertView not used
     * @param parent      not used
     * @return the view generated
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View taskListItem = layoutInflater.inflate(layout.task_list_item, parent, false);

        Button btnDetails = taskListItem.findViewById(id.btnExpand);
        TextView tvAmountTime = taskListItem.findViewById(id.tvNbrTimeLeft);
        TextView tvTimeUnit = taskListItem.findViewById(id.tvTimeUnit);
        TextView tvTitle = taskListItem.findViewById(id.tvTitle);
        TextView tvInterval = taskListItem.findViewById(id.tvInterval);
        Button btnDone = taskListItem.findViewById(id.btn_Done);

        if (taskArray != null) {
            tvInterval.setText(taskArray[position].getPreferredInterval().toString());
            tvTitle.setText(taskArray[position].getTitle());
            int timeUntil = taskArray[position].getTimeUntil().getTime();
            TimeUnit timeUnit = taskArray[position].getTimeUntil().getTimeUnit();
            String timeUnitString = timeUnit.toString();
            if (timeUntil > 1) {
                tvAmountTime.setText(String.valueOf(timeUntil));
                timeUnitString = timeUnitString + "s left";
                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));

            }
            if (timeUntil == 1) {
                tvAmountTime.setText(String.valueOf(timeUntil));
                timeUnitString = timeUnitString + " left";
                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));

            }
            if (timeUntil == 0) {
                tvAmountTime.setText(String.valueOf(timeUntil));
                timeUnitString = timeUnitString + "s left";

                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));

            }
            if (timeUntil == -1) {
                tvAmountTime.setText(String.valueOf(Math.abs(timeUntil)));
                timeUnitString = timeUnitString + " late";

                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.cheekRedDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.cheekRedDark));
            }
            if (timeUntil < -1) {
                tvAmountTime.setText(String.valueOf(Math.abs(timeUntil)));
                timeUnitString = timeUnitString +"s late";

                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.cheekRedDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.cheekRedDark));
            }
            tvTimeUnit.setText(timeUnitString);

            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent(mainActivity, DetailActivity.class);
                    detail.putExtra("taskId", taskArray[position].getId());
                    mainActivity.startActivity(detail);
                }
            });

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskArray[position].markAsDoneNow();
                    notifyDataSetChanged();
                    addNotification(mainActivity,taskArray[position].getNextNotification(),taskArray[position].getId());
                    TaskRegister taskRegister = TaskRegister.getInstance(context);
                    taskRegister.saveRegister(context);
                    Toast.makeText(mainActivity.getApplicationContext(),
                            getToastMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        } else {
            System.err.println("AdapterTaskOverview - rad 157 - problem with loading taskregister to adapter");
        }
        return taskListItem;
    }


    /**
     * This method creates an intent for a scheduled {@link Notification} using a {@link Calendar ) object.
     *
     * @param context      The context
     * @param notification The selected notificaiton to be scheduled.
     * @param taskId       The id of the {@link Task} connected to the {@link Notification}
     */
    private void addNotification(Context context, Notification notification, int taskId) {
        Calendar nextNotification = notification.getCalendarTimeForNotification();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", notification.getMessage());
        intent.putExtra("taskId", taskId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainActivity.getApplicationContext(), taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, nextNotification.getTimeInMillis(), pendingIntent);
        System.out.println("Time until next notification = " + nextNotification.getTimeInMillis());
        createNotificationChannel();
    }

    /**
     * Checks if the device runs on Android 8.0 and above (Oreo) and then creates the notification channel for
     * the notifications in the category of reminders.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "channelReminders";
            CharSequence name = "Reminders channel";
            String description = "Includes all the reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
