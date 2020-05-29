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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import a25.grupp.dynamicreminderandroid.model.Task;
import a25.grupp.dynamicreminderandroid.model.TimeUnit;

import static a25.grupp.dynamicreminderandroid.R.color;
import static a25.grupp.dynamicreminderandroid.R.id;
import static a25.grupp.dynamicreminderandroid.R.layout;

/**
 * Adapts the data in to tle list view in the overview in mainActivity
 *
 * @author Hanna My Jansson, Minna Röriksson, Cornelia Sköld
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

    /**
     * Sets the array with tasks
     *
     * @param taskArray the array with tasks
     */
    public void setTaskArray(Task[] taskArray) {
        this.taskArray = taskArray;
    }

    /**
     * Returns a message to be received when a task is performed
     *
     * @return a random message
     */
    public String getToastMessage() {
        String[] toastMessages = {"Great job!", "You're doing great!", "Well done!", "Keep up the good work!"
                , "Way to go!", "Fantastic work!", "Wow! Nice work", "Terrific!", "Good for you!",
                "You should be proud!", "That's awesome!", "Keep it up!"};
        Random rand = new Random();
        int randomIndex = rand.nextInt(12);
        return toastMessages[randomIndex];
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
            int timeUntil = taskArray[position].getTimeUntil().getTime();
            TimeUnit timeUnit = taskArray[position].getTimeUntil().getTimeUnit();
            String timeUnitString = timeUnit.toString();
            if (timeUntil > 1) {
                tvAmountTime.setText(String.valueOf(timeUntil));
                timeUnitString = timeUnitString + "s left";
                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));

            }
            if (timeUntil == 1) {
                tvAmountTime.setText(String.valueOf(timeUntil));
                timeUnitString = timeUnitString + " left";
                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));

            }
            if (timeUntil == 0) {
                tvAmountTime.setText(String.valueOf(timeUntil));
                timeUnitString = timeUnitString + "s left";

                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.colorDarkDark));

            }
            if (timeUntil == -1) {
                tvAmountTime.setText(String.valueOf(Math.abs(timeUntil)));
                timeUnitString = timeUnitString + " late";

                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.cheekRedDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.cheekRedDark));
            }
            if (timeUntil < -1) {
                tvAmountTime.setText(String.valueOf(Math.abs(timeUntil)));
                timeUnitString = timeUnitString +"s late";

                tvAmountTime.setTextColor(mainActivity.getResources().getColor(color.cheekRedDark));
                tvTimeUnit.setTextColor(mainActivity.getResources().getColor(color.cheekRedDark));
            }
            tvTimeUnit.setText(timeUnitString);

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
                    mainActivity.recreate();
                    Toast.makeText(mainActivity.getApplicationContext(),
                            getToastMessage(),
                            Toast.LENGTH_LONG)
                            .show();
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

    /**
     * Updating the list with tasks
     */
    private void updateList() {
        this.notifyDataSetChanged();
    }
}
