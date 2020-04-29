package a25.grupp.dynamicreminderandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import a25.grupp.dynamicreminderandroid.model.PossibleTime;
import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;
import a25.grupp.dynamicreminderandroid.model.TimeSpan;
import a25.grupp.dynamicreminderandroid.model.TimeUnit;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int taskId = getTaskId();
        Log.i("DetailActivity", "taskID = " + "" + taskId);
        start(taskId);
    }

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

                String info = findViewById(R.id.editText4).toString();

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
                    TaskRegister.getInstance().addTask(task);
                    int id = task.getId();


                } else {
                    task = TaskRegister.getInstance().getTaskWithId(selectedTaskId);
                    task.setInfo(info);
                    task.setTitle(title);
                    task.setPreferredInterval(preferredInterval);
                    task.setPossibleTimeForExecution(possibleTime);

                    Log.i("tag", "Size of taskregister: " + "" + TaskRegister.getInstance().getSize());
                }

                // Jumps back to MainActivity and shows the new task in the list
                Intent save = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(save);

            }
        });

        // If the tasks exists fill in the task information in fields
        if(taskId > 0)
        {
            TaskRegister taskregister = TaskRegister.getInstance();
            Task task = taskregister.getTaskWithId(taskId);
            EditText editTextSetTitle = findViewById(R.id.editText);
            editTextSetTitle.setText(task.getTitle());

            EditText editTextSetInterval = findViewById(R.id.editText2);
            editTextSetInterval.setText(Integer.toString(task.getId()));


        }

    }

    //TODO Få fram taskId om det skickats från MainActivity, annars returnera 0
    public int getTaskId()
    {
        Intent intent = getIntent();
        int taskId = intent.getIntExtra("taskID",0 );

        Log.i("tag", "Here is the taskId" + "" + taskId);


        return taskId;
    }
}
