package com.pet.app.resources;

import android.content.Context;
import android.content.SharedPreferences;

public class LocationPrefs {
    private static volatile LocationPrefs session;

    private SharedPreferences preferences;
    private Context context;

    private LocationPrefs(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
    }

    public static LocationPrefs getSession(Context context) {
        if (session == null)
            session = new LocationPrefs(context);
        return session;
    }

    private double latitude;
    private double longitude;

    public double getLatitude() {
        return  preferences.getFloat("latitude", 0);
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
        preferences.edit().putFloat("latitude", (float) latitude).apply();
    }


    public double getLongitude() {
        return  preferences.getFloat("longitude", 0);
    }

    public void setLongitude(double longitude)
    {
        this.longitude =longitude;
        preferences.edit().putFloat("longitude", (float) longitude).apply();
    }




    public void destroy() {
        preferences.edit().clear().apply();
    }
}
