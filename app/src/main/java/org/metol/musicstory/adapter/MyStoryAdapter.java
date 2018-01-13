package org.metol.musicstory.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.metol.musicstory.R;
import org.metol.musicstory.activity.BaseActivity;
import org.metol.musicstory.activity.EditStoryActivity;
import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.util.GlideManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Broccoli.Huang on 2018/1/10.
 */

public class MyStoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<MusicStory> mData;
    private final ArrayList<String> mDataMusicStoryDocumentId;

    public MyStoryAdapter(ArrayList<MusicStory> data, ArrayList<String> dataMusicStoryDocumentId) {
        this.mData = data;
        this.mDataMusicStoryDocumentId = dataMusicStoryDocumentId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == Constants.RECYCLER_VIEW_TYPE_MY_STORY) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_story_list, parent, false));
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            MusicStory musicStory = getItem(position);
            holder.itemView.setTag(R.id.tag_position, position);
            Context cnx = holder.itemView.getContext();

            GlideManager.setCardImage(cnx, musicStory.getCoverUrl(), holder.iv_card_cover_center);
            GlideManager.setFbAvatarImage(cnx, musicStory.getUid(), GlideManager.FbAvatarType.TYPE_SMALL, holder.iv_card_author_avatar);
            GlideManager.setBackgroundImageWithGaussianBlur(cnx, musicStory.getCoverUrl(), holder.iv_card_background);
            holder.tv_card_title.setText(musicStory.getStoryTitle());
            holder.iv_card_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity)cnx).openKKBOXUrlScheme(musicStory.getSongUrl());
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    ((BaseActivity)cnx).showBottomSheet(musicStory);
                }
            });

            if(TextUtils.isEmpty(musicStory.getArtistName())){
                holder.tv_card_artist_name_and_song_name.setVisibility(View.GONE);
            }else{
                holder.tv_card_artist_name_and_song_name.setText(musicStory.getArtistName()+" - "+musicStory.getSongName());
            }

            holder.iv_card_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.ARGUMENTS_MUSICSTORY, mData.get(position));
                    Intent intent = new Intent(cnx, EditStoryActivity.class);
                    intent.putExtra(Constants.ARGUMENTS_TYPE, EditStoryActivity.TYPE_EDIT);
                    intent.putExtra(Constants.ARGUMENTS_MUSICSTORY, bundle);
                    intent.putExtra(Constants.ARGUMENTS_STORY_ID, mDataMusicStoryDocumentId.get(position));

                    cnx.startActivity(intent, ActivityOptions.makeCustomAnimation(cnx, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
                }
            });

            holder.iv_card_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(cnx)
                            .setTitle("刪除")
                            .setMessage("確定要刪除?")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Firestore.deleteMusicStory(mDataMusicStoryDocumentId.get(position), ((BaseActivity)cnx).getProgressBar(), new Firestore.Callback() {
                                        @Override
                                        public void onSuccess(Object... object) {
                                            mData.remove(position);
                                            mDataMusicStoryDocumentId.remove(position);
                                            notifyDataSetChanged();
                                            ((BaseActivity)cnx).showSnack("已刪除");
                                        }

                                        @Override
                                        public void onFailed(String reason) {
                                            ((BaseActivity)cnx).showSnack("刪除失敗="+reason);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public MusicStory getItem(int position) {
        if(mData != null && position >= 0 && position < mData.size()) {
            return mData.get(position);
        }else{
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_card_cover_center)                ImageView iv_card_cover_center;
        @BindView(R.id.iv_card_background)                  ImageView iv_card_background;
        @BindView(R.id.iv_card_author_avatar)               ImageView iv_card_author_avatar;
        @BindView(R.id.iv_card_play)                        ImageView iv_card_play;
        @BindView(R.id.tv_card_musicstory_title)            TextView  tv_card_title;
        @BindView(R.id.tv_card_artist_name_and_song_name)   TextView  tv_card_artist_name_and_song_name;
        @BindView(R.id.iv_card_edit)                        ImageView iv_card_edit;
        @BindView(R.id.iv_card_delete)                      ImageView iv_card_delete;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}
