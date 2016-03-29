package com.randomappsinc.foodbutton.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alexanderchiou on 3/28/16.
 */
public class PreferencesManager {
    private static final String NUM_APP_OPENS_KEY = "numAppOpens";
    private static final String DEFAULT_LOCATION_KEY = "defaultLocation";
    private static final String FIRST_TIME_KEY = "firstTime";
    private static PreferencesManager instance;
    private SharedPreferences prefs;

    public static PreferencesManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized PreferencesManager getSync() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    private PreferencesManager() {
        prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
    }

    public String getDefaultLocation() {
        return prefs.getString(DEFAULT_LOCATION_KEY, "");
    }

    public void setDefaultLocation(String defaultLocation) {
        prefs.edit().putString(DEFAULT_LOCATION_KEY, defaultLocation).apply();
    }

    public boolean shouldAskToRate() {
        int numAppOpens = prefs.getInt(NUM_APP_OPENS_KEY, 0) + 1;
        prefs.edit().putInt(NUM_APP_OPENS_KEY, numAppOpens).apply();
        return numAppOpens == 5;
    }

    public boolean shouldShowInstructions() {
        boolean shouldShowInstructions = prefs.getBoolean(FIRST_TIME_KEY, true);
        prefs.edit().putBoolean(FIRST_TIME_KEY, false).apply();
        return shouldShowInstructions;
    }
}
