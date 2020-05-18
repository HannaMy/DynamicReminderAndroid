package a25.grupp.dynamicreminderandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * this class creats a PopUp window when a user tries to do something in the application
 * that the user needs to be warned about, or does something that is not possible to do
 *
 * @author Hanna Ringkvist
 */

public class PopUp extends AppCompatDialogFragment {

    private int returnValue;
    private DetailActivity detailActivity;


    /**
     * Creates a popup-window when the user tries to return to the overview-window without saving the task
     * @param context
     * @param detailActivity
     */
    public void returnWithoutSaving(Context context, final DetailActivity detailActivity){
        this.detailActivity = detailActivity;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Return without saving");
        builder.setMessage("Are you sure you want to return without saving?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                detailActivity.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * Creates a popup-window when the user tries to enter an invalid interval in a task
     * @param context
     */
    public void invalidInterval(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Invalid interval");
        builder.setMessage("You need to add a preferred interval as a number");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Intent intent = new Intent(context, DetailActivity.class);
                //startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //return builder.create();
    }

    /**
     * Creates a popup-window when the user press the delete-button
     * @param context
     * @param detailActivity
     */
    public void popUpOnDeleteBtn(Context context, final DetailActivity detailActivity){
        this.detailActivity = detailActivity;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete a task");
        builder.setMessage("Are you sure you want to delete this task?");
        //builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                detailActivity.deleteTask();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * Creates a popup-window when the user tries to save a task without adding a title to the task
     * @param context
     */
    public void invalidTitle(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Invalid Title");
        builder.setMessage("You need to add a title");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Intent intent = new Intent(context, DetailActivity.class);
                //startActivity(intent);
                //dialog.cancel();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //return builder.create();
    }


    /**
     * Creates a popup-window when the user tries to save a task without adding a title to the task and
     * when the user tries to enter an invalid interval in a task
     * @param context
     */
    public void invalidTitleAndInterval(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Invalid title and interval");
        builder.setMessage("You need to enter a title and an interval");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Intent intent = new Intent(context, DetailActivity.class);
                //startActivity(intent);
                //dialog.cancel();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //return builder.create();
    }
}
