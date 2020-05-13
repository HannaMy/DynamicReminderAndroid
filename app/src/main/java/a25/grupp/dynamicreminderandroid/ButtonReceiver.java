package a25.grupp.dynamicreminderandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import a25.grupp.dynamicreminderandroid.model.TaskRegister;

public class ButtonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("ButtonReceiver");
        String action = intent.getStringExtra("action");
        int taskId = intent.getIntExtra("taskId", 0);
        if(action!=null) {
            if (action.equals("yesNow")) {
                System.out.println("NotificationREceiver - yeNow btn clicked");

            } else if (action.equals("no")) {
                System.out.println("NotificationREceiver - no btn clicked");
            }
        }

    }
}
