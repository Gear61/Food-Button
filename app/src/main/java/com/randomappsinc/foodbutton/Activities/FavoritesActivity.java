package com.randomappsinc.foodbutton.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.randomappsinc.foodbutton.Adapters.FavoritesAdapter;
import com.randomappsinc.foodbutton.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alexanderchiou on 4/14/16.
 */
public class FavoritesActivity extends StandardActivity {
    @Bind(R.id.favorites) ListView favorites;
    @Bind(R.id.no_favorites) View noFavorites;

    private FavoritesAdapter favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        favoritesAdapter = new FavoritesAdapter(this, noFavorites);
        favorites.setAdapter(favoritesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoritesAdapter.syncWithDb();
    }
}
