package com.example.user.geofence1;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by User on 18/04/2017.
 */

public final class Constants
{
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 200;
    private Constants()
    {}
    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";
    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    public static final HashMap<String,LatLng> LAND_MARKS= new HashMap<String,LatLng>();
    static {
        // San Francisco International Airport.
        LAND_MARKS.put("DESAI", new LatLng(15.4530437,74.9781061));
        LAND_MARKS.put("RAIL", new LatLng(15.442605,75.001981));
        LAND_MARKS.put("RAM", new LatLng(15.4461024,75.0034904));
        //LAND_MARKS.put("DESAI", new LatLng(15.4307654,75.0142297));
        LAND_MARKS.put("ASHWIN", new LatLng(15.4588429,74.9920602));
        LAND_MARKS.put("SDM", new LatLng(15.4305508,75.0140419));

    }
}
