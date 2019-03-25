package com.example.user.geofence1;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.GeofenceStatusCodes;

/**
 * Created by User on 22/04/2017.
 */

public class GeoFenceErrorMessages {
    private GeoFenceErrorMessages() {
    }

    public static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return ("Geo fence not available");
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "geofence_too_many_geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "geofence_too_many_pending_intents";
            default:
                return "unknown_geofence_error";
        }
    }
}
    /*public static String getErrorString(Context context, int errcode)
    {
        Resources mResorces= context.getResources();
        switch(errcode)
        {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "geo fence service is not available";
            default:
                return "cannot have geo fence right now";
        }
    }*/



