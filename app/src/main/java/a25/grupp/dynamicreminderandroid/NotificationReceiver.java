package a25.grupp.dynamicreminderandroid;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Random;

/**
 * This class creates and generate a notification
 * @author Cornelia Sköld, Hanna My Jansson, Minna Röriksson
 * @version 1.3
 */

public class NotificationReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "channelReminders";
    private int NOTIFICATION_ID;
    private String title;
    private String message;
    private Context context;
    private PendingIntent yesPendingIntent;
    private PendingIntent yesEarlierPendingIntent;
    private PendingIntent noPendingIntent;
    private int taskId;

    /**
     * This method creates a notification that it will broadcast at a given time
     *
     * @param context the context from where the notification is called
     * @param intent  is the intent of this broadcast receiver (the notification)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        taskId = intent.getIntExtra("taskId", 0);
        System.out.println("NotificationReceiver - taskid: " + taskId);

        NOTIFICATION_ID = taskId;
        this.context = context;
        title = randomGreeting();
        message = intent.getStringExtra("message");

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.grey_cockatiel_2);

        Intent landingIntent = new Intent(context, DetailActivity.class);
        landingIntent.putExtra("taskId", taskId);
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(context, taskId, landingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        createButtonIntents(taskId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.small_purple_cockatiel)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(context.getResources().getColor(R.color.colorMediumDark))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(landingPendingIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.small_purple_cockatiel, "Did it now", yesPendingIntent)
                .addAction(R.mipmap.small_purple_cockatiel, "Did it earlier", yesEarlierPendingIntent)
                .addAction(R.mipmap.small_purple_cockatiel, "Remind me again", noPendingIntent)
                .setDeleteIntent(noPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * This method creates the pending intent of the buttons in the notification
     *
     * @param taskId is the unique id of the task that the notification belongs to
     */
    public void createButtonIntents(int taskId) {
        Intent yesIntent = new Intent(context, ButtonReceiver.class);
        yesIntent.putExtra("action", "didItNow");
        yesIntent.putExtra("taskId", taskId);
        yesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        yesPendingIntent = PendingIntent.getBroadcast(context, 200 + taskId, yesIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent yesEarlierIntent = new Intent(context, DetailActivity.class);
        yesEarlierIntent.putExtra("taskId", taskId);
        yesEarlierIntent.putExtra("action", "didItEarlier");
        yesEarlierIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        yesEarlierPendingIntent = PendingIntent.getActivity(context, taskId + 1000, yesEarlierIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent noIntent = new Intent(context, ButtonReceiver.class);
        noIntent.putExtra("action", "no");
        noIntent.putExtra("taskId", taskId);
        noIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        noPendingIntent = PendingIntent.getBroadcast(context, 300 + taskId, noIntent, PendingIntent.FLAG_ONE_SHOT);
    }

    /**
     * This method choose a random greeting out of an array
     *
     * @return A string for the title in the notification
     */
    public String randomGreeting() {
        String[] greetings = {"Cheep chirp",
                "Piddywee my friend",
                "Squawk! Hello",
                "Coo coo mate!",
                "Tweet tweet",
                "Cuckoo to you",
                "Chatter chatter"};
        Random rand = new Random();
        int randomIndex = rand.nextInt(5);

        return greetings[randomIndex];
    }
}
