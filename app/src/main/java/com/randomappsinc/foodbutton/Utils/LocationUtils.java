package com.randomappsinc.foodbutton.Utils;

import android.content.Context;
import android.location.Location;

import com.randomappsinc.foodbutton.Persistence.PreferencesManager;
import com.randomappsinc.foodbutton.R;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    public static String getLatLongString(Location location) {
        return location.getLatitude() + ", " + location.getLongitude();
    }

    public static String[] getLocationOptions(String location) {
        Context context = MyApplication.getAppContext();
        List<String> options = new ArrayList<>();
        if (!location.equals(PreferencesManager.get().getCurrentLocation())) {
            options.add(context.getString(R.string.set_as_current));
        }
        options.add(context.getString(R.string.edit_location));
        options.add(context.getString(R.string.delete_location));
        return options.toArray(new String[options.size()]);
    }
}
