package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Models.Filter;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;
import com.rey.material.widget.CheckBox;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 4/28/16.
 */
public class FilterActivity extends StandardActivity {
    public static String FILTER_KEY = "filter";

    @Bind(R.id.american_toggle) CheckBox americanToggle;
    @Bind(R.id.chinese_toggle) CheckBox chineseToggle;
    @Bind(R.id.fast_food_toggle) CheckBox fastFoodToggle;
    @Bind(R.id.french_toggle) CheckBox frenchToggle;
    @Bind(R.id.indian_toggle) CheckBox indianToggle;
    @Bind(R.id.japanese_toggle) CheckBox japaneseToggle;
    @Bind(R.id.korean_toggle) CheckBox koreanToggle;
    @Bind(R.id.mediterranean_toggle) CheckBox mediterraneanToggle;
    @Bind(R.id.middle_eastern_toggle) CheckBox middleEasternToggle;
    @Bind(R.id.mexican_toggle) CheckBox mexicanToggle;
    @Bind(R.id.pizza_toggle) CheckBox pizzaToggle;
    @Bind(R.id.thai_toggle) CheckBox thaiToggle;

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filter = getIntent().getParcelableExtra(FILTER_KEY);
    }

    @OnClick({R.id.american, R.id.chinese, R.id.fast_food, R.id.french, R.id.indian, R.id.japanese, R.id.korean,
              R.id.mediterranean, R.id.middle_eastern, R.id.mexican, R.id.pizza, R.id.thai})
    public void categoryClicked (View view) {
        switch (view.getId()) {
            case R.id.american:
                processClick("newamerican,tradamerican", americanToggle);
                break;
        }
    }

    private void processClick(String category, CheckBox checkBox) {
        if (filter.processCategory(category)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
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
                Intent returnIntent = new Intent();
                returnIntent.putExtra(FILTER_KEY, filter);
                setResult(RESULT_OK, returnIntent);
                finish();
                return true;
            case R.id.clear_filters:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
