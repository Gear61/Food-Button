package com.randomappsinc.foodbutton.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Adapters.LocationsAdapter;
import com.randomappsinc.foodbutton.Persistence.PreferencesManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 4/5/16.
 */
public class MyLocationsActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.location_input) EditText locationInput;
    @Bind(R.id.plus_icon) ImageView plusIcon;
    @Bind(R.id.locations) ListView locations;
    @Bind(R.id.no_locations) View noLocations;

    private LocationsAdapter locationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_locations);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        plusIcon.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_android_add).colorRes(R.color.white));

        locationsAdapter = new LocationsAdapter(this, noLocations, parent);
        locations.setAdapter(locationsAdapter);
    }

    @OnClick(R.id.add_location)
    public void addLocation() {
        UIUtils.hideKeyboard(this);
        String location = locationInput.getText().toString();
        locationInput.setText("");
        if (location.isEmpty()) {
            UIUtils.showSnackbar(parent, getString(R.string.empty_location));
        } else if (PreferencesManager.get().alreadyHasLocation(location)) {
            UIUtils.showSnackbar(parent, getString(R.string.duplicate_location));
        } else {
            locationsAdapter.addLocation(location);
        }
    }

    @OnItemClick(R.id.locations)
    public void showLocationOptions(int position) {
        locationsAdapter.showOptionsDialog(position);
    }
}
