package org.metol.musicstory.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.metol.musicstory.Common;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class SharedPreferencesManager {
    public static final String IS_TAP_TARGET_TOOLBAR_SEARCH_BUTTON_SHOWN = "is_tap_target_toolbar_search_button_shown";
    public static final String IS_TAP_TARGET_TOOLBAR_PROFILE_BUTTON_SHOWN = "is_tap_target_toolbar_profile_button_shown";
    public static final String IS_TAP_TARGET_PROFILE_MY_STORY_BUTTON_SHOWN = "is_tap_target_profile_my_story_button_shown";
    public static final String IS_TAP_TARGET_SEARCH_BUTTON_LISTEN_SHOWN = "is_tap_target_search_button_listen_shown";
    public static final String IS_TAP_TARGET_SEARCH_BUTTON_ADD_STORY_SHOWN = "is_tap_target_search_button_add_story_shown";
    public static final String IS_TAP_TARGET_MUSIC_STORY_LIST_BUTTON_LISTEN_SHOWN = "is_tap_target_music_story_list_button_listen_shown";
    public static final String UID = "uid";

    protected static SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Common.getApp());
    protected static SharedPreferences.Editor spEditor = sp.edit();

    public static String getString(String key, String defaultValue){
        return sp.getString(key, defaultValue);
    }

    public static void putString(String key, String value){
        spEditor.putString(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue){
        return sp.getBoolean(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value){
        spEditor.putBoolean(key, value).commit();
    }
}