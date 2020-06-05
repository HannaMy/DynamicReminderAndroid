package a25.grupp.dynamicreminderandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * this class creates a PopUp window when a user tries to do something in the application
 * that the user needs to be warned about, or does something that is not possible to do
 *
 * @author Minna Röriksson, Hanna Ringkvist
 */

public class PopUp extends AppCompatDialogFragment {

    /**
     * Creates a popup-window when the user tries to return to the overview-window without saving the task
     *
     * @param context        the Context where the method was called
     * @param detailActivity the Activity where the pop up origins
     */
    public void returnWithoutSaving(Context context, final DetailActivity detailActivity) {
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
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Creates a popup-window when the user tries to enter an invalid interval in a task
     *
     * @param context is the Context where the pop up was created
     */
    public void invalidInterval(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Invalid interval");
        builder.setMessage("You need to add a preferred interval as a number");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Creates a popup-window when the user press the delete-button
     *
     * @param context
     * @param detailActivity
     */
    public void popUpOnDeleteBtn(Context context, final DetailActivity detailActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete a task");
        builder.setMessage("Are you sure you want to delete this task?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                detailActivity.deleteTask();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Creates a popup-window when the user tries to save a task without adding a title to the task
     *
     * @param context
     */
    public void invalidTitle(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Invalid Title");
        builder.setMessage("You need to add a title");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Creates a popup-window when the user tries to save a task without adding a title to the task and
     * when the user tries to enter an invalid interval in a task
     *
     * @param context
     */
    public void invalidTitleAndInterval(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Invalid title and interval");
        builder.setMessage("You need to enter a title and an interval");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
