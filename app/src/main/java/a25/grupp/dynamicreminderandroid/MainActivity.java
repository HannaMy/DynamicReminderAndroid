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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.Calendar;

import a25.grupp.dynamicreminderandroid.model.Notification;
import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;
import a25.grupp.dynamicreminderandroid.model.TimeSpan;

public class MainActivity extends AppCompatActivity {

    private UpdateThread updateThread;

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

        AdapterTaskOverview adapterTaskOverview = initiateAdapter();

        /*boolean bool = true;
        while (bool) {

            System.out.println("Thread: Task Updated ÖÖÖÖÖÖ");

            Task[] taskArray = TaskRegister.getInstance(this).getTaskArray();
            if (taskArray.length > 0) {
                adapterTaskOverview.setTaskArray(taskArray);
                adapterTaskOverview.notifyDataSetChanged();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

         */

        //testar notifikation
        //Task task = new Task();
        //addNotification(this, task.generateNotification()); //TODO: flytta till korrekt ställe
    }


    /**
     * Creates a menu button for deleting in the toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_button:
                this.recreate();
                return true;
            case R.id.delete_button:
                //TODO: kod för att ta bort flera uppgifter samtidigt
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AdapterTaskOverview initiateAdapter() {

       /* FileHandler fh =  new FileHandler(this);
        fh.saveToFileString("Test30");
        String s = fh.readFromFileString();
        System.out.println("Test save file on string: " + s);

        */


        //Temporär data
        String[] titles = {"Water Plants", "Dance", "Clean Bathroom", "Call Hilda"};
        String[] intervalInfos = {"Every 5 days", "Every 3 day", "Every 1 week", "Every 5 weeks"};
        int[] times = {4, 2, 1, 4};
        String[] timeUnits = {"Days left", "Days left", "Weeks left", "Weeks left"};
        int[] taskIds = {1, 2, 3, 4};


        TaskRegister taskRegister = TaskRegister.getInstance(this);
        /*
        System.out.println("ÄÄÄÄÄÄÄÄÄÄÄÄÄÄ task test: "+taskRegister.getTaskWithId(30));
        Task task1 = new Task();
        task1.setTitle("test 30");
        taskRegister.addTask(task1, this);

        System.out.println("ÖÖÖÖÖÖÖÖÖÖÖÖÖ task test: "+taskRegister.getTaskWithId(30));

         */


        System.out.println("MAIN ACTIVITY task register size: " + taskRegister.getSize());
        Task[] taskArray = taskRegister.getTaskArray();
        System.out.println("ÖÖÖÖÖÖÖÖÖÖÖÄÄÄÄÄÄÄÄÄÄÄÄÅÅÅÅÅÅÅÅÅÅÅ: taskArray: " + taskArray);

        AdapterTaskOverview adapterTaskOverview;

        if (taskArray.length > 0) {

            int size = taskArray.length;
            titles = new String[size];
            intervalInfos = new String[size];
            times = new int[size];
            timeUnits = new String[size];
            taskIds = new int[size];

            for (int i = 0; i < taskArray.length; i++) {

                Task task = taskArray[i];
                System.out.println("ÖÖ task: " + task + " i: " + i);
                titles[i] = task.getTitle();
                TimeSpan timeSpan = task.getPreferredInterval();
                if (timeSpan != null) {
                    intervalInfos[i] = task.getPreferredInterval().toString();
                    times[i] = task.getTimeUntil();
                    timeUnits[i] = "time unit";
                }

                //TODO fixa rätt timeunit
                taskIds[i] = task.getId();
            }


            //Listview
            adapterTaskOverview = new AdapterTaskOverview(this, titles);
            adapterTaskOverview.setTaskArray(taskArray);

            //adapterTaskOverview.updateListData(titles,intervalInfos,times,timeUnits,taskIds);
            ListView listView = findViewById(R.id.listviewOverview);
            listView.setAdapter(adapterTaskOverview);


        } else {
            //Listview
            adapterTaskOverview = new AdapterTaskOverview(this, titles);
            adapterTaskOverview.updateListData(titles, intervalInfos, times, timeUnits, taskIds);
            ListView listView = findViewById(R.id.listviewOverview);
            listView.setAdapter(adapterTaskOverview);
        }

       // taskRegister.saveRegister(this);

        /*  updateThread = new UpdateThread(adapterTaskOverview, this);

         */


        return adapterTaskOverview;


    }


    /**
     * This method creates an intent for a scheduled notification, using a Calendar object
     */
    public void addNotification(Context context, Notification notification) {
        Calendar nextNotification = notification.getCalendarTimeForNotification();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", notification.getMessage());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Denna ställer in när notifikationen ska visas
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextNotification.getTimeInMillis(), pendingIntent);
        createNotificationChannel();
    }

    /**
     * This method creates the notification channel for the notifications in the category of reminders
     */
    private void createNotificationChannel() {
        //Checks if the device runs on Android 8.0 and above (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateThread != null) {
            updateThread.setRunning(false);
            updateThread = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (updateThread != null) {
            updateThread.setRunning(false);
            updateThread = null;
        }
    }

    public class UpdateThread extends Thread {

        private AdapterTaskOverview adapter;
        private Context context;
        private boolean running = true;

        public UpdateThread(AdapterTaskOverview adapter, Context context) {
            this.adapter = adapter;
            this.context = context;
            start();
        }

        @Override
        public void run() {
            while (running) {

                System.out.println("Thread: Task Updated");
                try {
                    Task[] taskArray = TaskRegister.getInstance(context).getTaskArray();
                    if (taskArray.length > 0) {
                        adapter.setTaskArray(taskArray);
                    }
                    adapter.notifyDataSetChanged();
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }

}


