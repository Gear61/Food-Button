package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.foodbutton.Adapters.OptionsAdapter;
import com.randomappsinc.foodbutton.Models.FavoritesFilter;
import com.randomappsinc.foodbutton.Persistence.DatabaseManager;
import com.randomappsinc.foodbutton.R;
import com.randomappsinc.foodbutton.Utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by alexanderchiou on 7/5/16.
 */
public class OptionsActivity extends StandardActivity {
    public static final String MODE_KEY = "mode";

    @Bind(R.id.search_term) EditText searchInput;
    @Bind(R.id.content) ListView content;
    @Bind(R.id.no_options) View noOptions;

    private FavoritesFilter filter;
    private int mode;
    private OptionsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_options);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mode = getIntent().getIntExtra(MODE_KEY, FavoritesFilterActivity.CATEGORIES_REQUEST);
        filter = getIntent().getParcelableExtra(FilterActivity.FILTER_KEY);
        List<String> optionPool;
        List<String> alreadyChosen;
        if (mode == FavoritesFilterActivity.CATEGORIES_REQUEST) {
            setTitle(R.string.categories);
            alreadyChosen = filter.getCategories();
            optionPool = DatabaseManager.get().getCategories();
        } else {
            setTitle(R.string.cities);
            alreadyChosen = filter.getCities();
            optionPool = DatabaseManager.get().getCities();
        }

        adapter = new OptionsAdapter(this, noOptions, optionPool, alreadyChosen);
        content.setAdapter(adapter);
    }

    @OnTextChanged(value = R.id.search_term, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        adapter.refreshList(input.toString());
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        searchInput.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        UIUtils.loadMenuIcon(menu, R.id.confirm_choices, IoniconsIcons.ion_checkmark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm_choices:
                List<String> chosen = adapter.getChosen();
                if (mode == FavoritesFilterActivity.CATEGORIES_REQUEST) {
                    filter.setCategories(chosen);
                } else {
                    filter.setCities(chosen);
                }
                Intent intent = new Intent();
                intent.putExtra(FilterActivity.FILTER_KEY, filter);
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
