package a25.grupp.dynamicreminderandroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * @author Cornelia Sköld
 * @version 1.0
 */
public class NotificationReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "channelReminders";
    private final int NOTIFICATION_ID = 001;
    private String title;
    private String message;
    private Context context;
    private PendingIntent yesPendingIntent;
    private PendingIntent yesEarlierPendingIntent;
    private PendingIntent noPendingIntent;

    /**
     * This method creates a notification that it will broadcast at a given time
     * @param context the context from where the notification is called
     * @param intent is the intent of this broadcast receiver (the notification)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        title = "Hey you"; //metod för att hämta titel
        message = "This is a test message"; //metod för att hämta meddelande

        //Large icon
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.cockatiel);

        //What happens when the user clicks on the notification
        Intent landingIntent = new Intent(context, DetailActivity.class);
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(context, 0, landingIntent, PendingIntent.FLAG_ONE_SHOT);

        createButtonIntents();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.cockatiel)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(Color.GREEN)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(landingPendingIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.cockatiel, "Yes", yesPendingIntent)
                .addAction(R.mipmap.cockatiel, "Yes, earlier", yesEarlierPendingIntent)
                .addAction(R.mipmap.cockatiel, "No", noPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * This method creates the pending intent of the buttons in the notification
     */
    public void createButtonIntents(){
        //Yes button (DetailActivity.class är landing activity just nu)
        Intent yesIntent = new Intent(context, DetailActivity.class);
        yesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        yesPendingIntent = PendingIntent.getActivity(context, 0, yesIntent, PendingIntent.FLAG_ONE_SHOT);

        //Yes earlier button (DetailActivity.class är landing activity just nu)
        Intent yesEarlierIntent = new Intent(context, DetailActivity.class);
        yesEarlierIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        yesEarlierPendingIntent = PendingIntent.getActivity(context, 0, yesEarlierIntent, PendingIntent.FLAG_ONE_SHOT);

        //No button (DetailActivity.class är landing activity just nu)
        Intent noIntent = new Intent(context, DetailActivity.class);
        noIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        noPendingIntent = PendingIntent.getActivity(context, 0, noIntent, PendingIntent.FLAG_ONE_SHOT);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
