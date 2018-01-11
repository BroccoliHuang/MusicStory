package org.metol.musicstory;

import android.app.Application;
import android.text.TextUtils;

import com.google.firebase.firestore.FirebaseFirestore;

import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.util.SharedPreferencesManager;
import org.metol.musicstory.util.TapTargetManager;

//印出KeyHash
//import android.content.ClipData;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
//import android.util.Base64;
//import android.util.Log;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;

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

        //印出KeyHash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(SystemManager.getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.d("KeyHash:", KeyHash);
//
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("KeyHash", KeyHash);
//                clipboard.setPrimaryClip(clip);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
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
