package org.metol.musicstory.util;

import com.facebook.login.LoginManager;

import org.metol.musicstory.Common;

/**
 * Created by Broccoli on 2018/1/13.
 */

public class StatusManager {
    public static void Logout(){
        LoginManager.getInstance().logOut();
        SharedPreferencesManager.putString(SharedPreferencesManager.EMAIL, "");
        Common.setEmail("");
    }
}
