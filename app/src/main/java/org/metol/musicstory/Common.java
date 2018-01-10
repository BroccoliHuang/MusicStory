package org.metol.musicstory;

import android.app.Application;
import android.text.TextUtils;

import com.google.firebase.firestore.FirebaseFirestore;

import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.util.Api;
import org.metol.musicstory.util.SharedPreferencesManager;
import org.metol.musicstory.util.TapTargetManager;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class Common extends Application {
    private static Common app = null;
    public static boolean IS_DEBUG;
    private static FirebaseFirestore db = null;
    private static String fbID;
    private static Member member;
    private static TapTargetManager tapTargetManager;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        IS_DEBUG = isDebug();
    }

    public static synchronized Common getApp() {
        return app;
    }

    public static FirebaseFirestore getFirebaseFirestore(){
        if(db == null) db = FirebaseFirestore.getInstance();
        return db;
    }

    public static void setFbID(String fbID){
        Common.fbID = fbID;
        SharedPreferencesManager.putString(SharedPreferencesManager.FB_ID, fbID);
    }

    public static String getFbID(){
        if(TextUtils.isEmpty(fbID)){
            fbID = SharedPreferencesManager.getString(SharedPreferencesManager.FB_ID, "");
        }else{
            getMember(false, new CallbackMember() {
                @Override
                public void onMember(Member member) {
                    fbID = member.getFbId();
                }
            });
        }

        return fbID;
    }

    public static void setMember(Member member){
        Common.member = member;
    }

    public static void getMember(boolean forceReload, CallbackMember callback){
        if(forceReload || member==null) {
            Firestore.getMember(getFbID(), new Firestore.Callback() {
                @Override
                public void onSuccess(Object... object) {
                    if(object[0] instanceof Member){
                        setFbID(((Member)object[0]).getFbId());
                        setMember((Member)object[0]);
                        callback.onMember((Member)object[0]);
                    }else{
                        callback.onMember(null);
                    }
                }

                @Override
                public void onFailed(String reason) {
                    callback.onMember(null);
                }
            });
        }else{
            callback.onMember(member);
        }
    }

    public static TapTargetManager getTapTargetManager(){
        if(tapTargetManager==null) tapTargetManager = new TapTargetManager();
        return tapTargetManager;
    }

    public boolean isDebug() {
        //上線請改false
        return true;
    }

    public interface CallbackMember{
        void onMember(Member member);
    }
}
