package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.API.OAuth.ApiUtils;
import com.randomappsinc.foodbutton.Models.Filter;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;
import com.rey.material.widget.CheckBox;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alexanderchiou on 4/28/16.
 */
public class FilterActivity extends StandardActivity {
    public static String FILTER_KEY = "filter";

    public static final int[] categoryIds = new int[] {R.id.american, R.id.chinese, R.id.fast_food, R.id.french,
            R.id.indian, R.id.japanese, R.id.korean, R.id.mediterranean, R.id.middle_eastern,
            R.id.mexican, R.id.pizza, R.id.thai};

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filter = getIntent().getParcelableExtra(FILTER_KEY);

        for (int categoryId : categoryIds) {
            String category = ApiUtils.getCategoryFromId(categoryId);
            if (filter.getCategories().contains(category)) {
                int checkboxId = ApiUtils.getCheckboxId(categoryId);
                CheckBox checkBox = (CheckBox) findViewById(checkboxId);
                checkBox.setCheckedImmediately(true);
            }
        }
    }

    @OnClick({R.id.american, R.id.chinese, R.id.fast_food, R.id.french, R.id.indian, R.id.japanese, R.id.korean,
              R.id.mediterranean, R.id.middle_eastern, R.id.mexican, R.id.pizza, R.id.thai})
    public void categoryClicked (View view) {
        int checkboxId = ApiUtils.getCheckboxId(view.getId());
        CheckBox checkBox = (CheckBox) findViewById(checkboxId);
        processClick(ApiUtils.getCategoryFromId(view.getId()), checkBox);
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
