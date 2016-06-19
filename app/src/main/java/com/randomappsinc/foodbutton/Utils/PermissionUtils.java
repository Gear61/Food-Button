package com.randomappsinc.foodbutton.Utils;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by alexanderchiou on 3/28/16.
 */
public class PermissionUtils {
    public static void requestPermission(Fragment fragment, String permission, int requestCode) {
        FragmentCompat.requestPermissions(fragment, new String[]{permission}, requestCode);
    }

    public static boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(MyApplication.getAppContext(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
