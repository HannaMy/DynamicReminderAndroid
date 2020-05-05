package a25.grupp.dynamicreminderandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialogFragment;

public class PopUp extends AppCompatDialogFragment {

    private int returnValue;
    private DetailActivity detailActivity;

    public int returnWithoutSaving(Context context, final DetailActivity detailActivity){
        this.detailActivity = detailActivity;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Return without saving");
        builder.setMessage("Are you sure you want to return without saving?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //todo gå tillbaka utan att spara
                returnValue = 1;
                detailActivity.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                returnValue = 0;
                //dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return returnValue;
    }

    public void invalidInterval(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Invalid interval");
        builder.setMessage("You need to add a preferred interval as a number");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //return builder.create();
    }
}
