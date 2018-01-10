package org.metol.musicstory.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.metol.musicstory.Common;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class SystemManager {
    public static String getPackageName() {
        return Common.getApp().getPackageName();
    }

    public static String getAppVersionName() {
        String versionName = "";

        try {
            versionName = Common.getApp().getPackageManager().getPackageInfo(Common.getApp().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            MyLog.i("tag", "getAppVersionName NameNotFoundException="+e.getMessage());
        } catch(Exception e) {
            MyLog.i("tag", "getAppVersionName Exception="+e.getMessage());
        }
        return versionName;
    }

    public static void gotoGooglePlay(Context context, String packageName){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }
}
