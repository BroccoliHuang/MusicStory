package org.metol.musicstory.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.metol.musicstory.Common;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.model.Setting;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/8.
 */

public class Firestore {
    //Setting
    private static final String COLLECTION_SETTING = "Setting";
    private static final String DOCUMENT_ANDROID = "Android";

    //Member
    private static final String COLLECTION_MEMBER = "Member";
    private static final String FIELD_UID = "uid";
    private static final String FIELD_CREATE_TIME = "createTime";

    //MusicStory
    private static final String COLLECTION_MUSICSTORY = "MusicStory";

    public static void getSetting(@Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        if(progressBar!=null) progressBar.show();
        Common.getFirebaseFirestore().collection(COLLECTION_SETTING)
                .document(DOCUMENT_ANDROID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(progressBar!=null) progressBar.hide();
                        if (task.isSuccessful()) {
                            try {
                                callback.onSuccess(task.getResult().toObject(Setting.class));
                            }catch (IllegalStateException ise){
                                callback.onFailed("找不到設定資料");
                            }
                        } else {
                            callback.onFailed("找不到設定資料");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                    }
                });
    }

    public static void insertMember(Member member, @Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        getMember(member.getUid(), progressBar, new Callback() {
            @Override
            public void onSuccess(Object... object) {
                if(callback!=null) callback.onSuccess(((Member)object[0]).getUid());
            }

            @Override
            public void onFailed(String reason) {
                if(progressBar!=null) progressBar.show();
                Common.getFirebaseFirestore().collection(COLLECTION_MEMBER)
                        .document(member.getUid())
                        .set(member)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if(progressBar!=null) progressBar.hide();
                                if(callback!=null) callback.onSuccess(member.getUid());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(progressBar!=null) progressBar.hide();
                                if(callback!=null) callback.onFailed("加入會員失敗="+e.getMessage());
                            }
                        });
            }
        });
    }

    public static void getMember(String uid, @Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        if(progressBar!=null) progressBar.show();
        Common.getFirebaseFirestore().collection(COLLECTION_MEMBER)
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(progressBar!=null) progressBar.hide();
                        if (task.isSuccessful()) {
                            try {
                                callback.onSuccess(task.getResult().toObject(Member.class));
                            }catch (IllegalStateException ise){
                                callback.onFailed("找不到會員資料");
                            }
                        } else {
                            callback.onFailed("找不到會員資料");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                    }
                });
    }

    public static void updateMember(Member member, @Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        if(progressBar!=null) progressBar.show();
        Common.getFirebaseFirestore().collection(COLLECTION_MEMBER)
                .document(member.getUid())
                .set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(progressBar!=null) progressBar.hide();
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                        callback.onFailed("更新會員失敗="+e.getMessage());
                    }
                });
    }

    public static void insertMusicStory(MusicStory musicStory, @Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        if(progressBar!=null) progressBar.show();
        Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                .add(musicStory)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if(progressBar!=null) progressBar.hide();
                        if(callback!=null) callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                        if(callback!=null) callback.onFailed("新增故事失敗="+e.getMessage());
                    }
                });
    }

    public static void getMusicStoryByUid(String uid, @Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        if(progressBar!=null) progressBar.show();
        Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                .whereEqualTo(FIELD_UID, uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(progressBar!=null) progressBar.hide();
                        if (task.isSuccessful()) {
                            try {
                                ArrayList<String> alDocumentId = new ArrayList();
                                for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    alDocumentId.add(documentSnapshot.getId());
                                }

                                callback.onSuccess(task.getResult().toObjects(MusicStory.class), alDocumentId);
                            }catch (IllegalStateException ise){
                                callback.onSuccess();
                            }
                        } else {
                            callback.onFailed("尋找故事失敗");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                        callback.onFailed("尋找故事失敗="+e.getMessage());
                    }
                });
    }

    public static void getMusicStoryByDocumentId(String documentId, @Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        if(progressBar!=null) progressBar.show();
        Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                .document(documentId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(progressBar!=null) progressBar.hide();
                        if (task.isSuccessful()) {
                            try {
                                callback.onSuccess(task.getResult().toObject(MusicStory.class));
                            }catch (IllegalStateException ise){
                                callback.onSuccess();
                            }
                        } else {
                            callback.onFailed("尋找故事失敗");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                        callback.onFailed("尋找故事失敗="+e.getMessage());
                    }
                });
    }

    public static void updateMusicStory(String musicStoryDocumentId, MusicStory musicStory, @Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        if(progressBar!=null) progressBar.show();
        Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                .document(musicStoryDocumentId)
                .set(musicStory)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(progressBar!=null) progressBar.hide();
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                        callback.onFailed("修改故事失敗="+e.getMessage());
                    }
                });
    }

    public static void deleteMusicStory(String musicStoryDocumentId, @Nullable ContentLoadingProgressBar progressBar, @Nullable Callback callback){
        if(progressBar!=null) progressBar.show();
        Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                .document(musicStoryDocumentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(progressBar!=null) progressBar.hide();
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                        callback.onFailed("刪除故事失敗="+e.getMessage());
                    }
                });
    }

    private static Query first;
    private static DocumentSnapshot searchedDocument;
    public static void getMusicStory(@Nullable DocumentSnapshot lastVisible, int limit, @Nullable ContentLoadingProgressBar progressBar, @Nullable CallbackLoadMore callbackLoadMore){
        if(progressBar!=null) progressBar.show();
        if(lastVisible==null){
            first = Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                    .orderBy(FIELD_CREATE_TIME, Query.Direction.DESCENDING)
                    .limit(limit);
        }else{
            if(searchedDocument == lastVisible) return;
            first = Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                    .orderBy(FIELD_CREATE_TIME, Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(limit);
            searchedDocument = lastVisible;
        }

        first.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if(progressBar!=null) progressBar.hide();
                        if(documentSnapshots.size()==0) {
                            callbackLoadMore.onLoadMore(null);
                        }else{
                            callbackLoadMore.onLoadMore(documentSnapshots.getDocuments().get(documentSnapshots.size() - 1));
                        }
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(progressBar!=null) progressBar.hide();
                        callbackLoadMore.onSuccess(task.getResult().toObjects(MusicStory.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressBar!=null) progressBar.hide();
                        callbackLoadMore.onFailed("尋找故事失敗="+e.getMessage());
                    }
                });
    }

    public interface Callback{
        void onSuccess(Object... object);
        void onFailed(String reason);
    }

    public interface CallbackLoadMore{
        void onSuccess(Object object);
        void onLoadMore(DocumentSnapshot lastVisible);
        void onFailed(String reason);
    }
}
