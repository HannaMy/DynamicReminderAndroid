package a25.grupp.dynamicreminderandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

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

//Temporär data


        String[] titles  ={"Water Plants", "Dance", "Clean Bathroom", "Call Hilda"};
        String[] intervalInfos= {"Every 5 days", "Every 3 day", "Every 1 week", "Every 5 weeks"};
        int[] times={4, 2, 1, 4 };
        String[] timeUnits = {"Days left", "Days left", "Weeks left", "Weeks left"};
        int[] taskIds={1,2,3,4};

        //Listview
        AdapterTaskOverview adapterTaskOverview = new AdapterTaskOverview(this, titles);
        adapterTaskOverview.updateListData(titles,intervalInfos,times,timeUnits,taskIds);
        ListView listView = findViewById(R.id.listviewOverview);






        listView.setAdapter(adapterTaskOverview);

    }

}
