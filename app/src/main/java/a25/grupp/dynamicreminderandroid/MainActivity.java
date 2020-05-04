package a25.grupp.dynamicreminderandroid;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.Calendar;

import a25.grupp.dynamicreminderandroid.model.FileHandler;
import a25.grupp.dynamicreminderandroid.model.Notification;
import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;
import a25.grupp.dynamicreminderandroid.model.TimeSpan;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent detail = new Intent(MainActivity.this, DetailActivity.class);
                detail.putExtra("taskId", 0);
                startActivity(detail);
            }
        });

        initiateAdapter();

        //testar notifikation
        //Task task = new Task();
        //addNotification(this, task.generateNotification()); //TODO: flytta till korrekt ställe
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initiateAdapter(){

       /* FileHandler fh =  new FileHandler(this);
        fh.saveToFileString("Test30");
        String s = fh.readFromFileString();
        System.out.println("Test save file on string: " + s);

        */





        //Temporär data
        String[] titles  ={"Water Plants", "Dance", "Clean Bathroom", "Call Hilda"};
        String[] intervalInfos= {"Every 5 days", "Every 3 day", "Every 1 week", "Every 5 weeks"};
        int[] times={4, 2, 1, 4 };
        String[] timeUnits = {"Days left", "Days left", "Weeks left", "Weeks left"};
        int[] taskIds={1,2,3,4};


        TaskRegister taskRegister = TaskRegister.getInstance(this);
        /*
        System.out.println("ÄÄÄÄÄÄÄÄÄÄÄÄÄÄ task test: "+taskRegister.getTaskWithId(30));
        Task task1 = new Task();
        task1.setTitle("test 30");
        taskRegister.addTask(task1, this);

        System.out.println("ÖÖÖÖÖÖÖÖÖÖÖÖÖ task test: "+taskRegister.getTaskWithId(30));

         */



        Task[] taskArray = taskRegister.getTaskArray();

        if(taskArray.length > 0) {

            int size = taskArray.length;
            titles = new String[size];
            intervalInfos = new String[size];
            times = new int[size];
            timeUnits = new String[size];
            taskIds = new int[size];

            for (int i = 0; i < taskArray.length; i++)
            {

                Task task = taskArray[i];
                System.out.println("ÖÖ task: " + task + " i: " + i);
                titles[i] = task.getTitle();
                TimeSpan timeSpan = task.getPreferredInterval();
                if(timeSpan!= null) {
                    intervalInfos[i] = task.getPreferredInterval().toString();
                    times[i] = task.getTimeUntil();
                    timeUnits[i] = "time unit";
                }

                         //TODO fixa rätt timeunit
                taskIds[i] = task.getId();
            }

            //Listview
            AdapterTaskOverview adapterTaskOverview = new AdapterTaskOverview(this, titles);
            adapterTaskOverview.updateListData(titles,intervalInfos,times,timeUnits,taskIds);
            ListView listView = findViewById(R.id.listviewOverview);
            listView.setAdapter(adapterTaskOverview);
        }
        else
        {
            //Listview
            AdapterTaskOverview adapterTaskOverview = new AdapterTaskOverview(this, titles);
            adapterTaskOverview.updateListData(titles,intervalInfos,times,timeUnits,taskIds);
            ListView listView = findViewById(R.id.listviewOverview);
            listView.setAdapter(adapterTaskOverview);
        }

    }


    /**
     * This method creates an intent for a scheduled notification, using a Calendar object
     */
    public void addNotification(Context context, Notification notification){
        Calendar nextNotification = notification.getCalendarTimeForNotification();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", notification.getMessage());        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Denna ställer in när notifikationen ska visas
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextNotification.getTimeInMillis(), pendingIntent);
        createNotificationChannel();
    }

    /**
     * This method creates the notification channel for the notifications in the category of reminders
     */
    private void createNotificationChannel(){
        //Checks if the device runs on Android 8.0 and above (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String CHANNEL_ID = "channelReminders";
            CharSequence name = "Reminders channel"; //TODO: flytta till res?
            String description = "Includes all the reminders"; //TODO: flytta till res?
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
