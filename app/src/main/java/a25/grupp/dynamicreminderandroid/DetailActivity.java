package a25.grupp.dynamicreminderandroid;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import a25.grupp.dynamicreminderandroid.model.Notification;
import a25.grupp.dynamicreminderandroid.model.PossibleTime;
import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;
import a25.grupp.dynamicreminderandroid.model.TimeInterval;
import a25.grupp.dynamicreminderandroid.model.TimeSpan;
import a25.grupp.dynamicreminderandroid.model.TimeUnit;

/**
 * The activity that shows the details of the tasks, also the activity to use when adding a new task
 *
 * @author Hanna My Jansson, Anni Johansson, Cornelia Sköld, Hanna Ringkvist, Minna Röriksson
 * @version 1.0
 */


public class DetailActivity extends AppCompatActivity implements Serializable {

    private Date lastPerformed;
    private int possibleStartTime;
    private int possibleEndTime;


    /**
     * Method is called to when activity is created
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int taskId = getTaskID();
        Log.i("DetailActivity", "taskID = " + "" + taskId);
        start(taskId);
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

    /**
     * On click listener for the delete button.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("Clicked on delete");
        if (item.getItemId() == R.id.delete_button) {
            //deleteTask();
            PopUp popUp = new PopUp();
            //Shows a pop-up dialog asking if the user wants to return without saving
            popUp.popUpOnDeleteBtn(DetailActivity.this, DetailActivity.this);


            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Starts the Activity
     *
     * @param taskId the id of the task presented in the view
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("DefaultLocale")
    private void start(final int taskId) {
        /*
       ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutHideable);
       constraintLayout.setVisibility(View.GONE);

        final Spinner dropDownAlways = findViewById(R.id.ddAvailability);
        ArrayAdapter<CharSequence> adapterStart = ArrayAdapter.createFromResource(this, R.array.available, android.R.layout.simple_spinner_item);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownAlways.setAdapter(adapterStart);
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

 */

        final Spinner dropDownPosStart = findViewById(R.id.posStartTime);
        ArrayAdapter<CharSequence> adapterStart = ArrayAdapter.createFromResource(this, R.array.availableHours, R.layout.custom_spinner);
        adapterStart.setDropDownViewResource(R.layout.custom_spinner);
        dropDownPosStart.setAdapter(adapterStart);
        dropDownPosStart.setSelection(7);
        dropDownPosStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                possibleStartTime = (position + 1);
                System.out.println("Possible start time = " + possibleStartTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner dropDownPosEnd = findViewById(R.id.posEndTime);
        ArrayAdapter<CharSequence> adapterEnd = ArrayAdapter.createFromResource(this, R.array.availableHours, R.layout.custom_spinner);
        adapterEnd.setDropDownViewResource(R.layout.custom_spinner);
        dropDownPosEnd.setAdapter(adapterEnd);
        dropDownPosEnd.setSelection(19);
        dropDownPosEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                possibleEndTime = (position + 1);
                System.out.println("Possible end time = " + possibleEndTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner dropDownTimeUnit = findViewById(R.id.dropDown_timeUnit);
        ArrayAdapter<CharSequence> adapterTimeUnit = ArrayAdapter.createFromResource(this, R.array.timeunits, R.layout.custom_spinner);
        adapterTimeUnit.setDropDownViewResource(R.layout.custom_spinner);
        dropDownTimeUnit.setAdapter(adapterTimeUnit);
        dropDownTimeUnit.setSelection(1);

        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUp popUp = new PopUp();
                //Shows a pop-up dialog asking if the user wants to return without saving
                popUp.returnWithoutSaving(DetailActivity.this, DetailActivity.this);
            }
        });
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                saveTask(taskId);
            }
        });

        // If the tasks exists fill in the task information in fields
        if (taskId > 0) {
            setTaskInfo(taskId);
        }

        if (taskId == 0) {
            Date currentTime = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            String date = formatDate.format(currentTime);
            final Button btnCalendar = findViewById(R.id.btnCalendarLastPreformed);

            btnCalendar.setText(date); // sets button with current time and date
            btnCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar();
                }
            });

            Objects.requireNonNull(getSupportActionBar()).setTitle("New task");
        }

    }

    /**
     * connects the gui components to the logic
     */
    private void createGuiComponents() {
    }

    /**
     * Creates the listview adapter and puts the data in the listview
     */
    private void createListViewAdapter() {
    }

    /**
     * exits the detailactivity view without saving
     */
    public void cancel() {
        Intent cancel = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(cancel);
    }

    public void calendar() {
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int months = calendar.get(Calendar.MONTH);
        int years = calendar.get(Calendar.YEAR);
        final int hours = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(DetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, dayOfMonth, hourOfDay, minute);
                        lastPerformed = cal.getTime();
                        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
                        String strDate = formatDate.format(lastPerformed);
                        final Button btnCalendar = findViewById(R.id.btnCalendarLastPreformed);
                        btnCalendar.setText(strDate);
                    }
                }, hours, minutes, android.text.format.DateFormat.is24HourFormat(DetailActivity.this));
                timePickerDialog.show();
            }
        }, years, months, day);
        datePickerDialog.show();
    }

    /**
     * Deletes the selected task and the scheduled notification.
     */
    public void deleteTask() {
        int taskId = getTaskID();
        TaskRegister taskRegister = TaskRegister.getInstance(this);

        Task task = taskRegister.getTaskWithId(taskId);
        Notification notification = task.getNextNotification();

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", notification.getMessage());
        intent.putExtra("taskId", taskId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        taskRegister.removeWithId(taskId);
        System.out.println("Detail vid delete taskregister size: " + taskRegister.getSize());
        taskRegister.saveRegister(DetailActivity.this);

        Intent delete = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(delete);

    }

    /**
     * the task is saved with all the data from the detailsview
     */
    private void saveTask(int taskId) {
        Task task = null;
        int selectedTaskId = taskId;

        EditText editTextTitle = findViewById(R.id.etTitle);
        String title = editTextTitle.getText().toString();
        Log.i("DetailActivity", "The title of the task is: " + title);

        //preferredInterval
        int intervalAmount = 0;
        try {
            EditText editTextInterval = findViewById(R.id.etTimeInterval);
            intervalAmount = Integer.parseInt(editTextInterval.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intervalAmount != 0) {

            //Log.i("DetailActivity", "The preferred interval is: " + "" + intervalAmount);
        }

        // Get the correct TimeUnit from dropdown menu.
        Spinner dropDownTU = findViewById(R.id.dropDown_timeUnit);
        TimeUnit timeUnit = TimeUnit.hour;

        TextView textView = (TextView) dropDownTU.getSelectedView();
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

        EditText editTextInfo = findViewById(R.id.etNotes);
        String info = editTextInfo.getText().toString();
        TimeSpan preferredInterval = new TimeSpan(intervalAmount, timeUnit);


        // Handles possibleTime depending on choice in dropdown menu
        PossibleTime possibleTime = new PossibleTime();
        possibleTime.setPossibleHours(possibleStartTime, possibleEndTime);

        //Gamla possibleTime-koden
        /*
        final Spinner dropDownAlways = findViewById(R.id.posStartTime);
        TextView textView1 = (TextView) dropDownAlways.getSelectedView();
        String result1 = textView1.getText().toString();
        switch (result1) {
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
*/
        // Checks title and interval and jumps back to MainActivity and shows the new task in the list if everything is correct
        Intent save = new Intent(DetailActivity.this, MainActivity.class);
        if(title.equals("") && intervalAmount == 0){
            PopUp popUp = new PopUp();
            popUp.invalidTitleAndInterval(DetailActivity.this);
        } else if(title.equals("")){
            PopUp popUp = new PopUp();
            popUp.invalidTitle(DetailActivity.this);
        } else if (intervalAmount == 0) {
            PopUp popUp = new PopUp();
            popUp.invalidInterval(DetailActivity.this);
        }else{

            if (selectedTaskId <= 0) {
                task = new Task(title, info, preferredInterval);
                task.setPossibleTimeForExecution(possibleTime);

                if (lastPerformed == null) {
                    Calendar calendar = Calendar.getInstance();
                    lastPerformed = calendar.getTime();
                }
                task.setLastPerformed(lastPerformed);
                task.setNextNotification();


                //System.out.println(" lastperformed =" + lastPerformed);
                TaskRegister.getInstance(getBaseContext()).addTask(task, getBaseContext());
                System.out.println("task sparad");
                int id = task.getId();
                addNotification(getApplicationContext(), task.getNextNotification(), id);

            } else {
                TaskRegister taskRegister = TaskRegister.getInstance(getBaseContext());
                task = taskRegister.getTaskWithId(selectedTaskId);
                task.setInfo(info);
                task.setTitle(title);
                task.setPreferredInterval(preferredInterval);
                task.setPossibleTimeForExecution(possibleTime);
                task.setLastPerformed(lastPerformed);
                task.setNextNotification();
                addNotification(getApplicationContext(), task.getNextNotification(), taskId);
                taskRegister.saveRegister(DetailActivity.this);
                Log.i("tag", "Size of taskregister: " + "" + TaskRegister.getInstance(getBaseContext()).getSize());
            }

            startActivity(save);
        }
    }

    /**
     * Gets the task id from the intent and retunrs it
     *
     * @return the id of the task
     */
    private int getTaskID() {
        Intent intent = getIntent();
        int taskId = intent.getIntExtra("taskId", 0);
        Log.i("tag", "Here is the taskId" + " " + taskId);
        return taskId;
    }


    /**
     * If the tasks exists fill in the task information in fields
     *
     * @param taskId the id of the task presented in the view
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTaskInfo(int taskId) {
        TaskRegister taskregister = TaskRegister.getInstance(this.getBaseContext());
        Task task = taskregister.getTaskWithId(taskId);

        //Sets the title text
        EditText editTextSetTitle = findViewById(R.id.etTitle);
        editTextSetTitle.setText(task.getTitle());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit task");

        //Sets the interval number
        EditText editTextSetInterval = findViewById(R.id.etTimeInterval);
        editTextSetInterval.setText(String.format("%d", task.getPreferredInterval().getTime()));

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

        EditText editTextInfo = findViewById(R.id.etNotes);
        editTextInfo.setText(task.getInfo());
        //possiblehour
        Spinner dropDownPosEnd = findViewById(R.id.posEndTime);
        Spinner dropDownPosStart = findViewById(R.id.posStartTime);

        TimeInterval possibleHours = task.getPossibleHour();

        dropDownPosStart.setSelection(possibleHours.getFrom()-1);
        dropDownPosEnd.setSelection(possibleHours.getTo()-1);

        //Sets possible

        //sets last performed button
        lastPerformed = task.getLastPerformed();
        Button btnCalendarLastPerformed = findViewById(R.id.btnCalendarLastPreformed);
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
       // System.out.println("Task last performed = " + task.getLastPerformed());
        String date = formatDate.format(task.getLastPerformed());
        btnCalendarLastPerformed.setText(date);
        btnCalendarLastPerformed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar();
            }
        });
    }

    /**
     * This method creates an intent for a scheduled notification, using a Calendar object
     */
    public void addNotification(Context context, Notification notification, int taskId) {
        Calendar nextNotification = notification.getCalendarTimeForNotification();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", notification.getMessage());
        intent.putExtra("taskId", taskId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Denna ställer in när notifikationen ska visas
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextNotification.getTimeInMillis(), pendingIntent);
        System.out.println("Time until next notification = " + nextNotification.getTimeInMillis());
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
}
