package a25.grupp.dynamicreminderandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import a25.grupp.dynamicreminderandroid.model.Task;

public class AdapterTaskOverview extends ArrayAdapter {

    private String[] titles;
    private String[] intervalInfos;
    private int[] times;
    private String[] timeUnits;
    private int[] taskIds;
    private Activity mainActivity;
    private Task[] taskArray;


    public AdapterTaskOverview(@NonNull Context context, String[] titles) {
        super(context, R.layout.task_list_item,R.id.tvTitle, titles);
        System.out.println("adaper: construct");
        this.mainActivity =(MainActivity) context;
    }

    public void updateListData(String[] titles, String[] intervalInfos, int[] times, String[] timeUnits, int[] taskIds) {
        System.out.println("Adapter: update");
        this.titles = titles;
        this.intervalInfos = intervalInfos;
        this.times = times;
        this.taskIds = taskIds;
        this.timeUnits = timeUnits;
    }

    public void setTaskArray(Task[] taskArray) {
        this.taskArray = taskArray;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View taskListItem = layoutInflater.inflate(R.layout.task_list_item,parent, false);

        Button btnDetails = taskListItem.findViewById(R.id.btnExpand);
        TextView tvAmountTime = taskListItem.findViewById(R.id.tvNbrTimeLeft);
        TextView tvTimeUnit = taskListItem.findViewById(R.id.tvTimeUnit);
        TextView tvTitle = taskListItem.findViewById(R.id.tvTitle);
        TextView tvInterval = taskListItem.findViewById(R.id.tvInterval);

       if(taskArray != null){

           tvAmountTime.setText(String.valueOf(taskArray[position].getTimeUntil()));
           tvInterval.setText(taskArray[position].getPreferredInterval().toString());
           tvTitle.setText(taskArray[position].getTitle());
           tvTimeUnit.setText(taskArray[position].getPreferredInterval().getTimeUnit().toString());

           btnDetails.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent detail = new Intent(mainActivity, DetailActivity.class);
                   detail.putExtra("taskId", taskArray[position].getId());
                   System.out.println("put extra taskid " + taskArray[position].getId());
                   mainActivity.startActivity(detail);
               }
           });


       }else{
           System.out.println("Adapter: getView " + position);
           tvAmountTime.setText(String.valueOf(times[position]));
           tvInterval.setText(intervalInfos[position]);
           tvTitle.setText(titles[position]);
           tvTimeUnit.setText(timeUnits[position]);

           btnDetails.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent detail = new Intent(mainActivity, DetailActivity.class);
                   detail.putExtra("taskId", taskIds[position]);
                   mainActivity.startActivity(detail);
               }
           });

       }



        return taskListItem;
    }
}
