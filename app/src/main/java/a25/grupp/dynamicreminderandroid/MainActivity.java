package a25.grupp.dynamicreminderandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TaskRegister;
import a25.grupp.dynamicreminderandroid.model.TimeSpan;

/**
 * This class creates the overview view.
 *
 * @author Hanna My Jansson, Hanna Ringkvist,
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Method is called when the activity is created.
     *
     * @param savedInstanceState is used to pass data between activities
     */
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
                Intent detail = new Intent(MainActivity.this, DetailActivity.class);
                detail.putExtra("taskId", 0);
                startActivity(detail);
            }
        });
        initiateAdapter();
    }

    /**
     * Creates a toolbar at the top of the overview view
     *
     * @param menu a standard menu object
     * @return always returns true, because the toolbar is always at the top of the overview view
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    /**
     * Puts a selected item in the overview view
     *
     * @param item the item that will be shown
     * @return returns a value depending on the parent
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        this.recreate();
        return super.onOptionsItemSelected(item);

    }

    /**
     * Puts all the saved tasks in an listview in the overview view
     */
    private void initiateAdapter() {
        //Temporary data
        String[] titles = {"Water Plants", "Dance", "Clean Bathroom", "Call Hilda"};
        String[] intervalInfos = {"Every 5 days", "Every 3 day", "Every 1 week", "Every 5 weeks"};
        int[] times = {4, 2, 1, 4};
        String[] timeUnits = {"Days left", "Days left", "Weeks left", "Weeks left"};
        int[] taskIds = {1, 2, 3, 4};

        TaskRegister taskRegister = TaskRegister.getInstance(this);

        Task[] taskArray = taskRegister.getTaskArray();

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
                titles[i] = task.getTitle();
                TimeSpan timeSpan = task.getPreferredInterval();
                if (timeSpan != null) {
                    intervalInfos[i] = task.getPreferredInterval().toString();
                    times[i] = task.getTimeUntil().getTime();
                    timeUnits[i] = "time unit";
                }

                //TODO fixa rätt timeunit
                taskIds[i] = task.getId();
            }

            //Listview
            adapterTaskOverview = new AdapterTaskOverview(this, titles);
            adapterTaskOverview.setTaskArray(taskArray);

            ListView listView = findViewById(R.id.listviewOverview);
            listView.setAdapter(adapterTaskOverview);
            listView.setEmptyView(findViewById(R.id.empty));
        } else {
            System.out.println("empty");
        }
    }
}


