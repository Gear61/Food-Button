package com.randomappsinc.foodbutton.Utils;

import android.app.Application;
import android.content.Context;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;

/**
 * Created by alexanderchiou on 3/27/16.
 */
public final class MyApplication extends Application {
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new IoniconsModule())
            .with(new FontAwesomeModule());
        instance = getApplicationContext();
    }

    public static Context getAppContext() {
        return instance;
    }
}
