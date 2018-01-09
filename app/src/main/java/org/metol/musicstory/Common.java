package org.metol.musicstory;

import android.app.Application;
import android.text.TextUtils;

import com.google.firebase.firestore.FirebaseFirestore;

import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.util.Api;
import org.metol.musicstory.util.SharedPreferencesManager;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class Common extends Application {
    private static Common app = null;
    public static boolean IS_DEBUG;
    private static FirebaseFirestore db = null;
    private static String fbID;
    private static Member member;

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
        }

        return fbID;
    }

    public static void setMember(Member member){
        Common.member = member;
    }

    public static void getMember(CallbackMember callback){
        if(member==null) {
            Firestore.getMember(getFbID(), new Firestore.Callback() {
                @Override
                public void onSuccess(Object object) {
                    if(object instanceof Member){
                        callback.onMember((Member)object);
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

    public boolean isDebug() {
        //上線請改false
        return true;
    }

    public interface CallbackMember{
        void onMember(Member member);
    }
}
