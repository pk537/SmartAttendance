package com.example.user.geofence1;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>
{

    PendingIntent mGeofencePendingIntent;
    private final String Log_Tag = "Vishals Test App";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Button mAddGeofencesButton;
    ArrayList mGeofenceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(this, "created", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        // Empty list for storing geofences.
       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();*/

        mGeofenceList = new ArrayList<Geofence>();
        buildGoogleApiClient();
        populateGeofenceList();

    }

    public void populateGeofenceList() {
        //Toast.makeText(this, "latitude", Toast.LENGTH_SHORT).show();
        for (Map.Entry<String, LatLng> entry : Constants.LAND_MARKS.entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT )
                    .build());
            //Toast.makeText(this, "longitude", Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //Toast.makeText(this, "building", Toast.LENGTH_SHORT).show();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        //Toast.makeText(this, "geofencing request", Toast.LENGTH_SHORT).show();
        return builder.build();
    }
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeoFenceTransitionIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    /*private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeoFenceTransitionIntentService.class);
       // this.startService(intent);
        Toast.makeText(this, "pending intent", Toast.LENGTH_SHORT).show();
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        //return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }*/

    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();

        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeoFenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(this, "connecting", Toast.LENGTH_SHORT).show();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            //Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        /*mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);*/
    }

   /* @Override
    public void onLocationChanged(Location location) {
        //Log.i(Log_Tag,location.toString());
        Toast.makeText(this,Double.toString(location.getLatitude()), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, Double.toString(location.getLongitude()), Toast.LENGTH_SHORT).show();
    }*/


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do something with result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public void addGeofencesButtonHandler(View view)
    {
        //Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client not connected11!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            //Toast.makeText(this, "Tryyyy", Toast.LENGTH_SHORT).show();
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()).setResultCallback(this); // Result processed in onResult().
            //Toast.makeText(this, "Requesting", Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.}

        }


    }
    /*private void test_sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Toast.makeText(this, "notifying", Toast.LENGTH_SHORT).show();
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher)).setContentTitle("DDD")
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Hello")
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }*/

}
