package com.randomappsinc.foodbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.randomappsinc.foodbutton.API.ApiUtils;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiUtils.getSearchQueryMap("");
    }
}
