package com.randomappsinc.foodbutton.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import com.randomappsinc.foodbutton.Adapters.IconItemsAdapter;
import com.randomappsinc.foodbutton.R;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by alexanderchiou on 3/27/16.
 */
public class SettingsActivity extends StandardActivity {
    public static final String SUPPORT_EMAIL = "chessnone@gmail.com";
    public static final String OTHER_APPS_URL = "https://play.google.com/store/apps/dev?id=9093438553713389916";

    @Bind(R.id.settings_options) ListView settingsOptions;
    @BindString(R.string.feedback_subject) String feedbackSubject;
    @BindString(R.string.send_email) String sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsOptions.setAdapter(new IconItemsAdapter(this,
                R.array.settings_options, R.array.settings_icons));
    }

    @OnItemClick(R.id.settings_options)
    public void onItemClick(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                String uriText = "mailto:" + SUPPORT_EMAIL + "?subject=" + Uri.encode(feedbackSubject);
                Uri mailUri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, mailUri);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(Intent.createChooser(sendIntent, sendEmail));
                return;
            case 1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(OTHER_APPS_URL));
                break;
            case 2:
                Uri uri =  Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                if (!(getPackageManager().queryIntentActivities(intent, 0).size() > 0)) {
                    return;
                }
                break;
        }
        startActivity(intent);
    }
}
