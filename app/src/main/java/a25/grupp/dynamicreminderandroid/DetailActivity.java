package a25.grupp.dynamicreminderandroid;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import a25.grupp.dynamicreminderandroid.model.Notification;
import a25.grupp.dynamicreminderandroid.model.PossibleTime;
import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;
import a25.grupp.dynamicreminderandroid.model.TimeSpan;
import a25.grupp.dynamicreminderandroid.model.TimeUnit;

public class DetailActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int taskId = getTaskId();
        Log.i("DetailActivity", "taskID = " + "" + taskId);
        start(taskId);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("DefaultLocale")
    private void start(final int taskId) {
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutHideable);
        constraintLayout.setVisibility(View.GONE);


        final Spinner dropDownAlways = findViewById(R.id.dropDownAlways);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.available, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownAlways.setAdapter(adapter);
        dropDownAlways.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutHideable);
                if (position == 0) {
                    constraintLayout.setVisibility(View.GONE);

                } else if (position == 1) {
                    constraintLayout.setVisibility(View.VISIBLE);

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Spinner dropDownTimeUnit = findViewById(R.id.dropDown_timeUnit);
        ArrayAdapter<CharSequence> adapterTimeUnit = ArrayAdapter.createFromResource(this,R.array.timeunits,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownTimeUnit.setAdapter(adapterTimeUnit);


        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(cancel);
                //TODO här behövs en pop up som bekräftar att användaren vill gå tillbaka utan att spara
            }
        });
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                Task task = null;
                int selectedTaskId = taskId;  //Todo Fixa task-id! Skickas med intent både från add-knappen och från AdapterTaskOverview-knapp

                EditText editTextTitle = findViewById(R.id.editText);
                String title = editTextTitle.getText().toString();
                Log.i("DetailActivity", "The title of the task is: " + title);

                //preferredIntervall
                int intervalAmount = 5;
                try {
                    EditText editTextInterval = findViewById(R.id.editText2);
                    intervalAmount = Integer.parseInt(editTextInterval.getText().toString());
                    Log.i("DetailActivity", "The preferred interval is: " + "" + intervalAmount);
                } catch (Exception e) {
                   //Todo Felmeddelande med texten "You need to add a preferred interval as a number"

                }

                // Get the correct TimeUnit from dropdown menu.
                Spinner dropDownTU = findViewById(R.id.dropDown_timeUnit);
                TimeUnit timeUnit = TimeUnit.hour;

                TextView textView = (TextView)dropDownTU.getSelectedView();
                String result = textView.getText().toString();

                switch (result) {
                    case "hours":
                        timeUnit = TimeUnit.hour;
                        break;
                    case "days":
                        timeUnit = TimeUnit.day;
                        break;
                    case "weeks":
                        timeUnit = TimeUnit.week;
                        break;
                    case "months":
                        timeUnit = TimeUnit.month;
                        break;
                    case "years":
                        timeUnit = TimeUnit.year;
                        break;
                }

               EditText editTextInfo  = findViewById(R.id.editText4);
                String info = editTextInfo.getText().toString();
                TimeSpan preferredInterval = new TimeSpan(intervalAmount, timeUnit);

                // Handles possibleTime depending on choice in dropdown menu
                PossibleTime possibleTime = new PossibleTime();
                TextView textView1 = (TextView)dropDownAlways.getSelectedView();
                String result1 = textView1.getText().toString();
                switch(result1)
                {
                    case "Always":
                        //todo Fixa detta
                        break;
                    case "Custom":
                        //todo Fixa detta

                      //  possibleTime.setPossibleWeekDays(frame.getPossibleWeekdays()); //Todo
                      //  possibleTime.setPossibleDates(frame.getPossibleDates());        //Todo
                      //  LocalTime[] localTimes = frame.getPossibleHours();              //Todo
                      //  possibleTime.setPossibleHours(localTimes[0], localTimes[1]);
                        break;
                }

                if (selectedTaskId <= 0) {
                    task = new Task(title, info, preferredInterval);
                    task.setPossibleTimeForExecution(possibleTime);
                    TaskRegister.getInstance(getBaseContext()).addTask(task, getBaseContext());
                    System.out.println("ÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖÖ task sparad" );
                    //Todo Fixa ett intent som hoppar tillbaka till MainActivity och visar den nya tasken
                   // frame.addTask(task.getTitle(), task.getTimeUntil(), task.getTimeUnit(), task.getId());
                    Intent save = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(save);
                    int id = task.getId();
                    addNotification(getApplicationContext(), task.getNextNotification());

                } else {
                    task = TaskRegister.getInstance(getBaseContext()).getTaskWithId(selectedTaskId);
                    task.setInfo(info);
                    task.setTitle(title);
                    task.setPreferredInterval(preferredInterval);
                    task.setPossibleTimeForExecution(possibleTime);
                    addNotification(getApplicationContext(), task.getNextNotification());

                    Log.i("tag", "Size of taskregister: " + "" + TaskRegister.getInstance(getBaseContext()).getSize());
                }

                // Jumps back to MainActivity and shows the new task in the list
                Intent save = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(save);

            }
        });


        // If the tasks exists fill in the task information in fields
        if(taskId > 0)
        {
            setTaskInfo(taskId);
        }
        final Button btnCalendar = findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(DetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                btnCalendar.setText(dayOfMonth + "/" + month + "/" + year + "\n" + hourOfDay + ":" + minute);
                            }
                        }, hour, minute, android.text.format.DateFormat.is24HourFormat(DetailActivity.this));
                        timePickerDialog.show();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }



    //TODO Få fram taskId om det skickats från MainActivity, annars returnera 0
    public int getTaskId()
    {
        Intent intent = getIntent();
        int taskId = intent.getIntExtra("taskId",0 );

        Log.i("tag", "Here is the taskId" + " " + taskId);


        return taskId;
    }

    // If the tasks exists fill in the task information in fields
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTaskInfo(int taskId)
    {
        TaskRegister taskregister = TaskRegister.getInstance(this.getBaseContext());
        Task task = taskregister.getTaskWithId(taskId);

        //Sets the title text
        EditText editTextSetTitle = findViewById(R.id.editText);
        editTextSetTitle.setText(task.getTitle());

        //Sets the interval number
        EditText editTextSetInterval = findViewById(R.id.editText2);
        editTextSetInterval.setText(String.format("%d", task.getId()));

        //Sets the interval time unit
        Spinner intervalTimeUnit = findViewById(R.id.dropDown_timeUnit);
        TimeUnit timeUnit = task.getPreferredInterval().getTimeUnit();
        switch (timeUnit) {
            case hour:
                intervalTimeUnit.setSelection(0);
                break;
            case day:
                intervalTimeUnit.setSelection(1);
                break;
            case week:
                intervalTimeUnit.setSelection(2);
                break;
            case month:
                intervalTimeUnit.setSelection(3);
                break;
            case year:
                intervalTimeUnit.setSelection(4);
                break;
        }

        EditText editTextInfo = findViewById(R.id.editText4);
        editTextInfo.setText(task.getInfo());



    }

    /**
     * This method creates an intent for a scheduled notification, using a Calendar object
     */
    public void addNotification(Context context, Notification notification){
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
