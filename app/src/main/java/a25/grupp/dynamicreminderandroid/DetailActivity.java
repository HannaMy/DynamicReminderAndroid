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
import android.support.v4.app.NotificationManagerCompat;
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
import android.widget.Toast;

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
 * The activity that shows the details of the {@link Task}s. Also the activity used when adding a new {@link Task},
 * editing an existing {@link Task} or deleting a {@link Task}.
 *
 * @author Hanna My Jansson, Anni Johansson, Cornelia Sköld, Hanna Ringkvist, Minna Röriksson
 * @version 1.3
 */
public class DetailActivity extends AppCompatActivity implements Serializable {

    private Date lastPerformed;
    private int possibleStartTime;
    private int possibleEndTime;

    /**
     * Sets the content view on creation.
     *
     * @param savedInstanceState The saved instance state to be restored if not null.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        int taskId = getTaskID();
        setupDetailView(taskId);
    }


    /**
     * Creates a menu button for deleting in the toolbar if the task id is not equal to 0.
     *
     * @param menu The menu where the button is to be placed.
     * @return true if the the task id is not equal to 0, else it returns false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getTaskID() != 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.delete_menu, menu);
            return true;
        } else {
            return false;
        }
    }

    /**
     * On click listener for the delete button. Shows a pop-up dialog for delete confirmation.
     *
     * @param item The menu item which has been clicked.
     * @return True if the user clicks on "Yes" in the pop-up, else false.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_button) {

            PopUp popUp = new PopUp();
            //Shows a pop-up dialog asking if the user wants to delete the task.
            popUp.popUpOnDeleteBtn(DetailActivity.this, DetailActivity.this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Sets up some of the components for the activity.
     *
     * @param taskId The id of the {@link Task} presented in the view
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("DefaultLocale")
    private void setupDetailView(final int taskId) {

        String action = getIntent().getStringExtra("action");
        if (action != null) {
            System.out.println("Detail - getExtra - action = " + action);
            if (action.equals("didItEarlier")) {
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                notificationManagerCompat.cancel(taskId);
            }
        }

        // Dropdown menu for time units.
        final Spinner dropDownTimeUnit = findViewById(R.id.dropDown_timeUnit);
        ArrayAdapter<CharSequence> adapterTimeUnit = ArrayAdapter.createFromResource(this, R.array.timeunits, R.layout.custom_spinner);
        adapterTimeUnit.setDropDownViewResource(R.layout.custom_spinner);
        dropDownTimeUnit.setAdapter(adapterTimeUnit);
        dropDownTimeUnit.setSelection(1);

        // Dropdown menu for possible start time.
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

        // Dropdown menu for possible end time.
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
            setSelectedTaskInfo(taskId);
        }

        // If the task doesn't exist, show the current time in the field for last performed.
        if (taskId == 0) {
            Date currentTime = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            String date = formatDate.format(currentTime);
            final Button btnCalendar = findViewById(R.id.btnCalendarLastPreformed);

            btnCalendar.setText(date);
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
     * Exits the {@link DetailActivity} view without saving.
     */
    public void cancel() {
        Intent cancel = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(cancel);
        finish();
    }

    /**
     * Creates a calender for entering information about when the task was last performed.
     */
    private void calendar() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
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
     * The {@link Task} is saved with all the data from the details view.
     *
     * @param taskId The id for the {@link Task}.
     */
    private void saveTask(int taskId) {
        Task task = null;
        int selectedTaskId = taskId;

        EditText editTextTitle = findViewById(R.id.etTitle);
        String title = editTextTitle.getText().toString();

        //Gets the number of the preferred interval
        int intervalAmount = 0;
        try {
            EditText editTextInterval = findViewById(R.id.etTimeInterval);
            intervalAmount = Integer.parseInt(editTextInterval.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Gets the correct TimeUnit from the dropdown menu.
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

        // Gets the notes
        EditText editTextInfo = findViewById(R.id.etNotes);
        String info = editTextInfo.getText().toString();
        TimeSpan preferredInterval = new TimeSpan(intervalAmount, timeUnit);

        // Handles possibleTime depending on choices in dropdown menus
        PossibleTime possibleTime = new PossibleTime();
        possibleTime.setPossibleHours(possibleStartTime, possibleEndTime);

        // Checks if title and interval are correct or else shows pop-up dialogs accordingly
        Intent save = new Intent(DetailActivity.this, MainActivity.class);
        if (title.equals("") && intervalAmount == 0) {
            PopUp popUp = new PopUp();
            popUp.invalidTitleAndInterval(DetailActivity.this);
        } else if (title.equals("")) {
            PopUp popUp = new PopUp();
            popUp.invalidTitle(DetailActivity.this);
        } else if (intervalAmount == 0) {
            PopUp popUp = new PopUp();
            popUp.invalidInterval(DetailActivity.this);
        } else {

            // Creates a new {@link Task} if the task id is 0 or else updates the existing one with the information from the detail view.
            if (selectedTaskId <= 0) {
                task = new Task(title, info, preferredInterval);
                task.setPossibleTimeForExecution(possibleTime);

                if (lastPerformed == null) {
                    Calendar calendar = Calendar.getInstance();
                    lastPerformed = calendar.getTime();
                }
                task.setLastPerformed(lastPerformed);
                task.setNextNotification();

                TaskRegister.getInstance(getBaseContext()).addTask(task, getBaseContext());
                int id = task.getId();
                addNotification(getApplicationContext(), task.getNextNotification(), id);

            } else {
                TaskRegister taskRegister = TaskRegister.getInstance(getBaseContext());
                task = taskRegister.getTaskWithId(selectedTaskId);
                task.setNotes(info);
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
            finish();
            Toast.makeText(this,
                    "Task saved!",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Gets the task id from the intent and returns it.
     *
     * @return taskId The id of the task.
     */
    private int getTaskID() {
        Intent intent = getIntent();
        int taskId = intent.getIntExtra("taskId", 0);
        return taskId;
    }

    /**
     * Fills in the task information in fields if the {@link Task} exists.
     *
     * @param taskId The id of the task presented in the view.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setSelectedTaskInfo(int taskId) {
        TaskRegister taskregister = TaskRegister.getInstance(this.getBaseContext());
        Task task = taskregister.getTaskWithId(taskId);
        if (task != null) {
            // Sets the title text
            EditText editTextSetTitle = findViewById(R.id.etTitle);
            editTextSetTitle.setText(task.getTitle());
            Objects.requireNonNull(getSupportActionBar()).setTitle("Edit task");

            // Sets the interval number
            EditText editTextSetInterval = findViewById(R.id.etTimeInterval);
            editTextSetInterval.setText(String.format("%d", task.getPreferredInterval().getTime()));

            // Sets the interval time unit
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

            // Sets the notes
            EditText editTextInfo = findViewById(R.id.etNotes);
            editTextInfo.setText(task.getNotes());

            //Sets the possible hours
            Spinner dropDownPosEnd = findViewById(R.id.posEndTime);
            Spinner dropDownPosStart = findViewById(R.id.posStartTime);

            TimeInterval possibleHours = task.getPossibleHour();

            dropDownPosStart.setSelection(possibleHours.getFrom() - 1);
            dropDownPosEnd.setSelection(possibleHours.getTo() - 1);

            // Sets the date and time in the last performed field
            lastPerformed = task.getLastPerformed();
            Button btnCalendarLastPerformed = findViewById(R.id.btnCalendarLastPreformed);
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            String date = formatDate.format(task.getLastPerformed());
            btnCalendarLastPerformed.setText(date);
            btnCalendarLastPerformed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendar();
                }
            });
        }
    }

    /**
     * Deletes the selected task and its scheduled notification.
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
        finish();

        Toast.makeText(this,
                "Task deleted!",
                Toast.LENGTH_LONG)
                .show();
    }


    /**
     * This method creates an intent for a scheduled {@link Notification} using a {@link Calendar) object.
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

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

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
