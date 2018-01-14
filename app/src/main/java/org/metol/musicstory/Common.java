package org.metol.musicstory;

import android.app.Application;
import android.text.TextUtils;

import com.google.firebase.firestore.FirebaseFirestore;

import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.util.SharedPreferencesManager;
import org.metol.musicstory.util.TapTargetManager;

//印出KeyHash
//import org.metol.musicstory.util.SystemManager;
//import android.content.ClipData;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
//import android.util.Base64;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class Common extends Application {
    private static Common app = null;
    private static FirebaseFirestore db = null;
    private static String email = "";
    private static Member member;
    private static TapTargetManager tapTargetManager;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        //印出KeyHash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(SystemManager.getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.i("KeyHash:", KeyHash);
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

    public static void setEmail(String email){
        Common.email = email;
        SharedPreferencesManager.putString(SharedPreferencesManager.EMAIL, email);
    }

    public static String getEmail(){
        if(TextUtils.isEmpty(email)){
            email = SharedPreferencesManager.getString(SharedPreferencesManager.EMAIL, "");
        }else{
            getMember(false, new CallbackMember() {
                @Override
                public void onMember(Member member) {
                    email = member.getEmail();
                }
            });
        }

        return email;
    }

    public static void setMember(Member member){
        Common.member = member;
    }

    public static void getMember(boolean forceReload, CallbackMember callback){
        if(forceReload || member==null) {
            Firestore.getMember(getEmail(), null, new Firestore.Callback() {
                @Override
                public void onSuccess(Object... object) {
                    if(object[0] instanceof Member){
                        setEmail(((Member)object[0]).getEmail());
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

    public interface CallbackMember{
        void onMember(Member member);
    }
}
