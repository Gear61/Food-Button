package com.randomappsinc.foodbutton.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.randomappsinc.foodbutton.Adapters.LocationsAdapter;
import com.randomappsinc.foodbutton.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 4/5/16.
 */
public class EditLocationsActivity extends StandardActivity{
    @Bind(R.id.locations) ListView configs;
    @Bind(R.id.no_locations) View noConfigs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_locations);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configs.setAdapter(new LocationsAdapter(this, noConfigs));
    }
}
