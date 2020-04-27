package a25.grupp.dynamicreminderandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
        start();
    }

    private void start() {
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
                int selectedTaskId = 0;  //Todo Fixa task-id!

                String title = findViewById(R.id.editText).toString();

                //preferredIntervall
                int intervalAmount = 0;
                try {
                    intervalAmount = Integer.parseInt(findViewById(R.id.editText2).toString());
                } catch (Exception e) {
                   //Todo Felmeddelande med texten "You need to add a preferred interval as a number"

                }

                // Get the correct TimeUnit from dropdown menu.
                TimeUnit timeUnit = null;
                switch (dropDownTimeUnit.getSelectedItem().toString()) {
                    case "hour":
                        timeUnit = TimeUnit.hour;
                        break;
                    case "day":
                        timeUnit = TimeUnit.day;
                        break;
                    case "week":
                        timeUnit = TimeUnit.week;
                        break;
                    case "month":
                        timeUnit = TimeUnit.month;
                        break;
                    case "year":
                        timeUnit = TimeUnit.year;
                        break;
                }

                String info = findViewById(R.id.editText4).toString();

                TimeSpan preferredInterval = new TimeSpan(intervalAmount, timeUnit);

                // Handles possibleTime depending on choice in dropdown menu
                PossibleTime possibleTime = new PossibleTime();
                switch(dropDownAlways.getSelectedItem().toString())
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
                    //Todo Fixa ett intent som hoppar tillbaka till MainActivity och visar den nya tasken
                   // frame.addTask(task.getTitle(), task.getTimeUntil(), task.getTimeUnit(), task.getId());
                    Intent save = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(save);

                } else {
                    task = TaskRegister.getInstance().getTaskWithId(selectedTaskId);
                    task.setInfo(info);
                    task.setTitle(title);
                    task.setPreferredInterval(preferredInterval);
                    task.setPossibleTimeForExecution(possibleTime);

                }

            }
        });

    }
}
