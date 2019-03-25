package com.example.user.geofence1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.button2;
import android.widget.Button;

/**
 * Created by User on 18/04/2017.
 */

public class GeoFenceTransitionIntentService extends IntentService {
    protected static final String TAG="Geofservices";
    public GeoFenceTransitionIntentService()
    {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private String getTransitionString(int geofenceTransition)
    {
        String msg=" ";
        switch (geofenceTransition)
        {
            case 1: msg="Entry";
                break;
            case 2: msg="Exit";
                break;
            default: msg="unknown";
                break;
        }
        return msg;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        startService(intent);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        //Toast.makeText(this, "sending notification18", Toast.LENGTH_SHORT).show();
        if (geofencingEvent.hasError()) {
            String errorMessage = GeoFenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }
        //Toast.makeText(this, "sending notification18", Toast.LENGTH_SHORT).show();
        // Get the transition type.
        List triggeringGeofences = geofencingEvent.getTriggeringGeofences();
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        //Toast.makeText(this, "geo"+geofenceTransition, Toast.LENGTH_SHORT).show();
        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER )//||geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
        {
            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(this,geofenceTransition,triggeringGeofences);
            sendNotification(geofenceTransitionDetails);
            Intent dialogIntent = new Intent(getBaseContext(), Login.class);
            dialogIntent.putExtra("message","Login allowed ! ");
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(dialogIntent);
            stopService(intent);

        }
        else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
           // sendNotification("Exited");
            //List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
             String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                  geofenceTransition,
                triggeringGeofences
            );

            // Send notification and log the transition details.
            //Toast.makeText(this, "sending notification13", Toast.LENGTH_SHORT).show();
            sendNotification(geofenceTransitionDetails);
            //Toast.makeText(this,"bbb", Toast.LENGTH_SHORT).show();
            //Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, "Error ");

        }
    }

    private String getGeofenceTransitionDetails(Context context,int geofenceTransition, List<Geofence> triggeringGeofences)
    {
        String geofenceTransitionString = getTransitionString(geofenceTransition);
        ArrayList triggeringGeofencesIdlist= new ArrayList();
        for(Geofence geofence : triggeringGeofences)
        {
            triggeringGeofencesIdlist.add(geofence.getRequestId());
        }
        //Toast.makeText(this, "transition details", Toast.LENGTH_SHORT).show();
        String triggeringGeofencesIdString= TextUtils.join(", ",triggeringGeofencesIdlist);
        return geofenceTransitionString+": "+triggeringGeofencesIdString;
    }


    private void sendNotification(String notificationDetails)
    {
        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 3. Create and send a notification
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("notification,")
                .setContentText(notificationDetails)
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationDetails))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);




    }

}
