package a25.grupp.dynamicreminderandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import static a25.grupp.dynamicreminderandroid.R.*;

/**
 * Adapts the data in to tle list view in the overview in mainActivity
 *
 * @author Hanna My Jansson
 * @version 1.0
 */

public class AdapterTaskOverview extends ArrayAdapter {

    private String[] titles;
    private String[] intervalInfos;
    private int[] times;
    private String[] timeUnits;
    private int[] taskIds;
    private Activity mainActivity;
    private Task[] taskArray;
    private String[] lateOrLeft;
    private  TextView tvAmountTime;

    /**
     * Constructor
     *
     * @param context the context from where the constructor is called
     * @param titles  teh titles of the tasks
     */
    public AdapterTaskOverview(@NonNull Context context, String[] titles) {
        super(context, layout.task_list_item, id.tvTitle, titles);
        System.out.println("adaper: construct");
        this.mainActivity = (MainActivity) context;
    }

    /**
     * Updates the list with data
     *
     * @param titles       of the tasks
     * @param intervalInfo of the tasks
     * @param times        of the tasks
     * @param timeUnits    of the tasks
     * @param taskIds      of the tasks
     */
    public void updateListData(String[] titles, String[] intervalInfo, int[] times, String[] timeUnits, int[] taskIds) {
        System.out.println("Adapter: update");
        this.titles = titles;
        this.intervalInfos = intervalInfo;
        this.times = times;
        this.taskIds = taskIds;
        this.timeUnits = timeUnits;
    }

    private String lateOrLeft(Task task) {
        int timeUntil = task.getTimeUntil();
        String leftOrLate = "";
        if (timeUntil > 1) {
            leftOrLate = "s left";
        }else if(timeUntil == 1){
            leftOrLate = " left";
        }else if(timeUntil == 0){
            leftOrLate = " left";
        }else if(timeUntil == -1){
            leftOrLate = " late";
        }else {
            leftOrLate = "s late";
        }
        return leftOrLate;
    }

    private int textColor(Task task){
        int timeUntil = task.getTimeUntil();
        int color;
        if (timeUntil >= 0) {
            color = 0x99009900;
        }else {
            color = 0x99990000;
        }
        return color;
    }

    /**
     * Sets the array with tasks
     *
     * @param taskArray the array with tasks
     */
    public void setTaskArray(Task[] taskArray) {
        this.taskArray = taskArray;
    }

    /**
     * Generates a view item for the list
     *
     * @param position    of the view
     * @param convertView not used
     * @param parent      not used
     * @return the view generated
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View taskListItem = layoutInflater.inflate(layout.task_list_item, parent, false);

        Button btnDetails = taskListItem.findViewById(id.btnExpand);
        TextView tvAmountTime = taskListItem.findViewById(id.tvNbrTimeLeft);
        TextView tvTimeUnit = taskListItem.findViewById(id.tvTimeUnit);
        TextView tvTitle = taskListItem.findViewById(id.tvTitle);
        TextView tvInterval = taskListItem.findViewById(id.tvInterval);
        Button btnDone = taskListItem.findViewById(id.btn_Done);

        if (taskArray != null) {
            tvInterval.setText(taskArray[position].getPreferredInterval().toString());
            tvTitle.setText(taskArray[position].getTitle());

            //set the time unit
            int timeUntil = taskArray[position].getTimeUntil();

            if (timeUntil > 1){
                tvAmountTime.setText(String.valueOf(timeUntil));
                String timeUnit = taskArray[position].getPreferredInterval().getTimeUnit().toString() + "s left";
                tvTimeUnit.setText(timeUnit);
                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.green));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.green));

            } if (timeUntil == 1){
                tvAmountTime.setText(String.valueOf(timeUntil));
                String timeUnit = taskArray[position].getPreferredInterval().getTimeUnit().toString() + " left";
                tvTimeUnit.setText(timeUnit);
                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.green));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.green));

            } if (timeUntil == 0){
                tvAmountTime.setText(String.valueOf(timeUntil));
                String timeUnit = taskArray[position].getPreferredInterval().getTimeUnit().toString() + " left";
                tvTimeUnit.setText(timeUnit);
                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.green));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.green));

            }if (timeUntil < 0){
                tvAmountTime.setText(String.valueOf(Math.abs(timeUntil)));
                String timeUnit = taskArray[position].getPreferredInterval().getTimeUnit().toString() + "s late";
                tvTimeUnit.setText(timeUnit);
                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.red));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.red));
            }

            //String leftOrLate  = lateOrLeft(taskArray[position]);
            //int color = textColor(taskArray[position]);
            //tvTimeUnit.setText(taskArray[position].getPreferredInterval().getTimeUnit().toString() + leftOrLate);
            //tvAmountTime.setTextColor(color);
            //tvTimeUnit.setTextColor(color);

            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent(mainActivity, DetailActivity.class);
                    detail.putExtra("taskId", taskArray[position].getId());
                    System.out.println("put extra taskid " + taskArray[position].getId());
                    mainActivity.startActivity(detail);
                }
            });

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskArray[position].markAsDoneNow();

                }
            });


        } else {
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

    private void updateList(){
        this.notifyDataSetChanged();
    }
}
