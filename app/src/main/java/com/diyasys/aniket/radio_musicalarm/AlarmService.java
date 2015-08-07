package com.diyasys.aniket.radio_musicalarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
/**
 * Created by Aniket on 24-07-2015.
 */
public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent)
    {
        sendNotification("Wake up! Wake up!");
    }
    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, WakeUp.class), 0);

        //Notification prototype
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(this).setContentTitle("Good Morning!").setSmallIcon(R.drawable.sunrise).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);

        alamNotificationBuilder.setAutoCancel(true);
        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }

}
