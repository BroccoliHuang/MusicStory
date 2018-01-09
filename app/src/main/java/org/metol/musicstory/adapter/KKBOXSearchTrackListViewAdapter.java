package org.metol.musicstory.adapter;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.metol.musicstory.Common;
import org.metol.musicstory.R;
import org.metol.musicstory.activity.EditStoryActivity;
import org.metol.musicstory.activity.BaseActivity;
import org.metol.musicstory.activity.SearchActivity;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.model.KKBOX.Tracks;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.util.Callback;
import org.metol.musicstory.util.GlideManager;
import org.metol.musicstory.util.SharedPreferencesManager;
import org.metol.musicstory.util.TapTargetManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.PorterDuff.Mode.MULTIPLY;

/**
 * Created by broccoli on 2017/3/30.
 */

public class KKBOXSearchTrackListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int mRecyclerViewCategoryIndex  = -1;
    private int mRecyclerViewSearchKeywordIndex = -1;
    private String mSearchKeyword;
    private final ArrayList<Tracks> mData;
    private int mFirstSongItemPosition = -1;

    public KKBOXSearchTrackListViewAdapter(ArrayList<Tracks> data, String searchKeyword) {
        this.mData = data;
        this.mRecyclerViewSearchKeywordIndex = 0;
        this.mSearchKeyword = searchKeyword;

        //position 0為SearchKeywordViewHolder
        mData.add(0, null);
        notifyItemInserted(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if(viewType == Constants.RECYCLER_VIEW_TYPE_LOADING){
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_loading, parent, false));
        }else if(viewType == Constants.RECYCLER_VIEW_TYPE_SEARCH_KEYWORD){
            return new SearchKeywordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_search_keyword, parent, false), mSearchKeyword);
        }else{
            if(viewType == Constants.RECYCLER_VIEW_TYPE_ITEM){
                return new KKBOXSearchTrackListViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_song, parent, false));
            }else{
                return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof KKBOXSearchTrackListViewAdapter.LoadingViewHolder){
            KKBOXSearchTrackListViewAdapter.LoadingViewHolder loadingViewHolder = (KKBOXSearchTrackListViewAdapter.LoadingViewHolder) viewHolder;
            loadingViewHolder.pb_load_more.setIndeterminate(true);
        }else if(viewHolder instanceof KKBOXSearchTrackListViewAdapter.SearchKeywordViewHolder){
            KKBOXSearchTrackListViewAdapter.SearchKeywordViewHolder searchWordViewHolder = (KKBOXSearchTrackListViewAdapter.SearchKeywordViewHolder) viewHolder;
        }else{
            if(viewHolder instanceof KKBOXSearchTrackListViewAdapter.ViewHolder) {
                final KKBOXSearchTrackListViewAdapter.ViewHolder holder = (KKBOXSearchTrackListViewAdapter.ViewHolder) viewHolder;
                Tracks tracks = getItem(position);

                holder.itemView.setTag(R.id.tag_position, position);
                holder.iv_add_story.setTag(R.id.tag_position, position);
                holder.iv_song_cover.setTag(R.id.tag_position, position);
                holder.iv_song_playstate.setTag(R.id.tag_position, position);

                GlideManager.setSongImage(holder.iv_song_cover.getContext(), tracks.getAlbum().getImages().get(tracks.getAlbum().getImages().size()-1).getUrl(), holder.iv_song_cover);
                holder.tv_song_name.setText(tracks.getName());
                holder.tv_song_singer.setText(tracks.getAlbum().getArtist().getName());
                holder.iv_song_playstate.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);

                holder.iv_add_story.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        MusicStory musicStory = new MusicStory(tracks.getAlbum().getArtist().getId(), tracks.getAlbum().getArtist().getName(), tracks.getAlbum().getId(), tracks.getAlbum().getName(), tracks.getId(), tracks.getName(), tracks.getAlbum().getImages().get(tracks.getAlbum().getImages().size()-1).getUrl(), "", "", "", "", "", "", "", "", "", "", null);
                        bundle.putParcelable(Constants.ARGUMENTS_MUSICSTORY, musicStory);
                        Intent intent = new Intent(v.getContext(), EditStoryActivity.class);
                        intent.putExtra(Constants.ARGUMENTS_MUSICSTORY, bundle);

                        v.getContext().startActivity(intent, ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
                    }
                });

                View.OnClickListener onClick_CoverOrPlayState = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((BaseActivity)v.getContext()).openKKBOXUrlScheme(tracks.getUrl());
                    }
                };
                holder.iv_song_cover.setOnClickListener(onClick_CoverOrPlayState);
                holder.iv_song_playstate.setOnClickListener(onClick_CoverOrPlayState);

//            holder.tv_song_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((BaseActivity) v.getContext()).showSnack(holder.tv_song_name.getText(), Snackbar.LENGTH_LONG);
//                }
//            });
//            holder.tv_song_singer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((BaseActivity) v.getContext()).showSnack(holder.tv_song_singer.getText(), Snackbar.LENGTH_LONG);
//                }
//            });

                if(mFirstSongItemPosition == -1){
                    mFirstSongItemPosition = position;
                    if(!SharedPreferencesManager.getBoolean(SharedPreferencesManager.IS_TAP_TARGET_SEARCH_BUTTON_ADD_STORY_SHOWN, false) || !SharedPreferencesManager.getBoolean(SharedPreferencesManager.IS_TAP_TARGET_SEARCH_BUTTON_ADD_STORY_SHOWN, false)){
                        ((SearchActivity)holder.fl_song_cover.getContext()).requestShowTarget(
                                TapTargetManager.getTapTargetForView(holder.fl_song_cover, "到KKBOX聽音樂囉~", "♪~♫~♪~♫~"),
                                TapTargetManager.getTapTargetForView(holder.iv_add_story, "新增一段刻骨銘心的故事吧~", "歌只是文字與音符的組合，當人們賦予它故事，才開始有了意義")
                        );
                    }
                }
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 新增一個Activity顯示其他人的故事，不過不是寫在這裡，要多一顆按鈕
                    ((SearchActivity)viewHolder.itemView.getContext()).showSnack("未來將開放觀看其他人的故事哦~", Snackbar.LENGTH_LONG);
                }
            });
        }
    }

    public Callback.LoadMore loadMore(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(mData.get(mData.size() - 1)!=null) {
                    mData.add(null);
                    notifyItemInserted(mData.size() - 1);
                }
            }
        });

        return new Callback.LoadMore() {
            @Override
            public void onLoadMore(Object object) {
                new Handler().post(new Runnable() {
                   @Override
                   public void run() {
                       mData.remove(mData.size() - 1);
                       notifyItemRemoved(mData.size());

                       for(Tracks tracks: (ArrayList<Tracks>)object){
                           mData.add(tracks);
                       }
                       notifyDataSetChanged();
                   }
                });
            }
        };
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public Tracks getItem(int position) {
        if(position == 0){
            return null;
        }else if(mData != null && position > 0 && position < mData.size()) {
            return mData.get(position);
        }else{
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mRecyclerViewCategoryIndex){
            return Constants.RECYCLER_VIEW_TYPE_CATEGORY;
        }else if(position == mRecyclerViewSearchKeywordIndex){
            return Constants.RECYCLER_VIEW_TYPE_SEARCH_KEYWORD;
        }else if(mData.get(position) == null){
            return Constants.RECYCLER_VIEW_TYPE_LOADING;
        }else{
            return Constants.RECYCLER_VIEW_TYPE_ITEM;
        }
    }

    protected static class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView((R.id.pb_load_more)) ProgressBar pb_load_more;
        public LoadingViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
            pb_load_more.getIndeterminateDrawable().setColorFilter(Common.getApp().getResources().getColor(R.color.app_theme), MULTIPLY);
        }
    }

    protected static class SearchKeywordViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_listitem_search_keyword) LinearLayout ll_listitem_search_keyword;
        @BindView(R.id.tv_listitem_search_keyword) TextView tv_listitem_search_keyword;
        public SearchKeywordViewHolder(View v, String mSearchKeyWord) {
            super(v);
            ButterKnife.bind(this, itemView);

            ll_listitem_search_keyword.setBackgroundColor(Common.getApp().getResources().getColor(R.color.cardlist_background));
            tv_listitem_search_keyword.setText("您搜尋的是："+mSearchKeyWord);
            tv_listitem_search_keyword.setTextColor(Common.getApp().getResources().getColor(R.color.cardlist_search_keyword_wording));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fl_song_cover)       FrameLayout fl_song_cover;
        @BindView(R.id.iv_song_cover)       ImageView   iv_song_cover;
        @BindView(R.id.iv_song_playstate)   ImageView   iv_song_playstate;
        @BindView(R.id.pb_song_loading)     ProgressBar pb_song_loading;
        @BindView(R.id.tv_song_name)        TextView    tv_song_name;
        @BindView(R.id.tv_song_singer)      TextView    tv_song_singer;
        @BindView(R.id.iv_add_story)        ImageView   iv_add_story;
        @BindView(R.id.divider)             View        v_divider;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);

            v_divider.setBackgroundColor(Common.getApp().getResources().getColor(R.color.cardlist_divider));
        }
    }
}