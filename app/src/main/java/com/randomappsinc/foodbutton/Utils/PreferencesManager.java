package com.randomappsinc.foodbutton.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.randomappsinc.foodbutton.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexanderchiou on 3/28/16.
 */
public class PreferencesManager {
    private static final String NUM_APP_OPENS_KEY = "numAppOpens";
    private static final String DEFAULT_LOCATION_KEY = "defaultLocation";
    private static final String SAVED_LOCATIONS_KEY = "savedLocations";
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
        return prefs.getString(DEFAULT_LOCATION_KEY, MyApplication.getAppContext().getString(R.string.automatic));
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

    private Set<String> getSavedLocations() {
        return prefs.getStringSet(SAVED_LOCATIONS_KEY, new HashSet<String>());
    }

    public void addSavedLocation(String location) {
        Set<String> savedLocations = getSavedLocations();
        prefs.edit().remove(SAVED_LOCATIONS_KEY).apply();
        savedLocations.add(location);
        prefs.edit().putStringSet(SAVED_LOCATIONS_KEY, savedLocations).apply();
    }

    public void removeSavedLocation(String location) {
        Set<String> savedLocations = getSavedLocations();
        prefs.edit().remove(SAVED_LOCATIONS_KEY).apply();
        savedLocations.remove(location);
        prefs.edit().putStringSet(SAVED_LOCATIONS_KEY, savedLocations).apply();
    }

    public void changeSavedLocation(String oldLocation, String newLocation) {
        removeSavedLocation(oldLocation);
        addSavedLocation(newLocation);
    }

    public boolean alreadyHasLocation(String location) {
        return getSavedLocations().contains(location);
    }

    public String[] getLocationsArray() {
        Set<String> savedLocations = getSavedLocations();
        ArrayList<String> locationsList = new ArrayList<>();
        locationsList.add(MyApplication.getAppContext().getString(R.string.automatic));

        ArrayList<String> savedLocationsList = new ArrayList<>();
        for (String location : savedLocations) {
            savedLocationsList.add(location);
        }
        Collections.sort(savedLocationsList);

        locationsList.addAll(savedLocationsList);

        return locationsList.toArray(new String[locationsList.size()]);
    }

    public int getCurrentLocationIndex() {
        String[] savedLocations = getLocationsArray();
        String currentDefault = getDefaultLocation();
        for (int i = 0; i < savedLocations.length; i++) {
            if (savedLocations[i].equals(currentDefault)) {
                return i;
            }
        }
        return 0;
    }
}
