package com.randomappsinc.foodbutton.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/3/16.
 */
public class FavoritesFilterActivity extends StandardActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.apply_filters, IoniconsIcons.ion_checkmark);
        UIUtils.loadMenuIcon(menu, R.id.clear_filters, IoniconsIcons.ion_android_close);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apply_filters:
                setResult(RESULT_OK);
                finish();
                return true;
            case R.id.clear_filters:
                new MaterialDialog.Builder(this)
                        .title(R.string.clear_confirmation)
                        .content(R.string.remove_all_filters)
                        .positiveText(android.R.string.yes)
                        .negativeText(android.R.string.no)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
