package com.randomappsinc.foodbutton.Utils;

import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by alexanderchiou on 3/28/16.
 */
public class PermissionUtils {
    public static void requestPermission(Fragment fragment, String permission, int requestCode) {
        fragment.requestPermissions(new String[]{permission}, requestCode);
    }

    public static boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(MyApplication.getAppContext(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
