package org.metol.musicstory.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class SystemManager {
    public static String getPackageName(Context context) {
        String package_name = context.getApplicationContext().getPackageName();
        return package_name;
    }

    public static String getVersionCode(Context mContext) {
        PackageInfo pInfo = null;

        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var3) {
            MyLog.i("tag", "NameNotFoundException");
        }

        return String.valueOf(pInfo.versionCode);
    }
}
