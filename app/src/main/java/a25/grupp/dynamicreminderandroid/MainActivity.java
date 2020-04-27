package a25.grupp.dynamicreminderandroid;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.Calendar;

import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;

public class MainActivity extends AppCompatActivity {

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
                startActivity(detail);
            }
        });

        initiateAdapter();
        addNotification();
    }

    private void initiateAdapter(){
        //Temporär data
        String[] titles  ={"Water Plants", "Dance", "Clean Bathroom", "Call Hilda"};
        String[] intervalInfos= {"Every 5 days", "Every 3 day", "Every 1 week", "Every 5 weeks"};
        int[] times={4, 2, 1, 4 };
        String[] timeUnits = {"Days left", "Days left", "Weeks left", "Weeks left"};
        int[] taskIds={1,2,3,4};

        TaskRegister taskRegister = TaskRegister.getInstance();

        Task[] taskArray = taskRegister.getTaskArray();
        //Listview
        AdapterTaskOverview adapterTaskOverview = new AdapterTaskOverview(this, titles);
        adapterTaskOverview.updateListData(titles,intervalInfos,times,timeUnits,taskIds);
        ListView listView = findViewById(R.id.listviewOverview);
        listView.setAdapter(adapterTaskOverview);
    }


    /**
     * This method creates an intent for a scheduled notification, using a Calendar object
     */
    public void addNotification(){
        Calendar calendar = Calendar.getInstance();
        //Date date = new Date();
        long tenSecondsMillis = 1000 * 10;

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Denna ställer in när notifikationen ska visas
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + tenSecondsMillis, pendingIntent);
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
