package org.metol.musicstory.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

/**
 * Created by Broccoli.Huang on 2018/1/8.
 */

public class Firestore {
    //Member
    private static final String COLLECTION_MEMBER = "Member";
    private static final String FIELD_FB_ID = "fbId";
    private static final String FIELD_CREATE_TIME = "createTime";

    //MusicStory
    private static final String COLLECTION_MUSICSTORY = "MusicStory";


    public static void insertMember(Member member, @Nullable Callback callback){
        getMember(member.getFbId(), new Callback() {
            @Override
            public void onSuccess(Object object) {
                if(callback!=null) callback.onSuccess(null);
            }

            @Override
            public void onFailed(String reason) {
                Common.getFirebaseFirestore().collection(COLLECTION_MEMBER)
                        .document(member.getFbId())
                        .set(member)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if(callback!=null) callback.onSuccess(null);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(callback!=null) callback.onFailed("加入會員失敗="+e.getMessage());
                            }
                        });
            }
        });
    }

    public static void getMember(String fbID, @Nullable Callback callback){
        Common.getFirebaseFirestore().collection(COLLECTION_MEMBER)
                .document(fbID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
                });
    }

    public static void updateMember(Member member, @Nullable Callback callback){
        Common.getFirebaseFirestore().collection(COLLECTION_MEMBER)
                .document(member.getFbId())
                .set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailed("更新會員失敗="+e.getMessage());
                    }
                });
    }

    public static void insertMusicStory(MusicStory musicStory, @Nullable Callback callback){
        Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                .add(musicStory)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if(callback!=null) callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(callback!=null) callback.onFailed("新增故事失敗="+e.getMessage());
                    }
                });
    }

    public static void getMusicStoryByFbId(String fbId, @Nullable Callback callback){
        Common.getFirebaseFirestore().collection(COLLECTION_MUSICSTORY)
                .whereEqualTo(FIELD_FB_ID, fbId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                callback.onSuccess(task.getResult().toObjects(MusicStory.class));
                            }catch (IllegalStateException ise){
                                callback.onSuccess(null);
                            }
                        } else {
                            callback.onFailed("尋找故事失敗");
                        }

                    }
                });
    }

    private static Query first;
    private static DocumentSnapshot searchedDocument;
    public static void getMusicStory(@Nullable DocumentSnapshot lastVisible, int limit, @Nullable CallbackLoadMore callbackLoadMore){
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
                        callbackLoadMore.onSuccess(task.getResult().toObjects(MusicStory.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callbackLoadMore.onFailed("尋找故事失敗="+e.getMessage());
                    }
                });
    }

    public interface Callback{
        void onSuccess(Object object);
        void onFailed(String reason);
    }

    public interface CallbackLoadMore{
        void onSuccess(Object object);
        void onLoadMore(DocumentSnapshot lastVisible);
        void onFailed(String reason);
    }
}