package com.randomappsinc.foodbutton.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.randomappsinc.foodbutton.Adapters.OptionsAdapter;
import com.randomappsinc.foodbutton.Models.FavoritesFilter;
import com.randomappsinc.foodbutton.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 7/5/16.
 */
public class OptionsActivity extends StandardActivity {
    public static final String MODE_KEY = "mode";

    @Bind(R.id.content) ListView content;
    @Bind(R.id.no_options) View noOptions;

    private FavoritesFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_options);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int mode = getIntent().getIntExtra(MODE_KEY, FavoritesFilterActivity.CATEGORIES_REQUEST);
        filter = getIntent().getParcelableExtra(FilterActivity.FILTER_KEY);
        List<String> alreadyChosen;
        switch (mode) {
            case FavoritesFilterActivity.CATEGORIES_REQUEST:
                setTitle(R.string.categories);
                alreadyChosen = filter.getCategories();
                break;
            case FavoritesFilterActivity.CITIES_REQUEST:
                setTitle(R.string.cities);
                alreadyChosen = filter.getCities();
                break;
            default:
                setTitle(R.string.categories);
                alreadyChosen = filter.getCategories();
                break;
        }

        content.setAdapter(new OptionsAdapter(this, noOptions, mode, alreadyChosen));
    }
}
