package a25.grupp.dynamicreminderandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatDialogFragment;

public class PopUp extends AppCompatDialogFragment {

    private int returnValue;
    private DetailActivity detailActivity;

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
