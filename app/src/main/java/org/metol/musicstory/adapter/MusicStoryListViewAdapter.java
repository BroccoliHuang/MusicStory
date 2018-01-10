package org.metol.musicstory.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.metol.musicstory.Common;
import org.metol.musicstory.R;
import org.metol.musicstory.activity.BaseActivity;
import org.metol.musicstory.activity.MainActivity;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.util.Callback;
import org.metol.musicstory.util.GlideManager;
import org.metol.musicstory.util.SharedPreferencesManager;
import org.metol.musicstory.util.TapTargetManager;
import org.metol.musicstory.widget.CategoryButtons;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.graphics.PorterDuff.Mode.MULTIPLY;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class MusicStoryListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int mRecyclerViewCategoryIndex  = -1;
    private String[] mCategoryText;
    private int mCategoryIndex;
    private int mFirstSongItemPosition = -1;
    private CategoryButtons.Callback_CategoryButtons mCallback_CategoryButtons;
    private final ArrayList<MusicStory> mData;

    public MusicStoryListViewAdapter(ArrayList<MusicStory> data) {
        this.mData = data;
    }

    public MusicStoryListViewAdapter(ArrayList<MusicStory> data, String[] categoryText, int categoryIndex, CategoryButtons.Callback_CategoryButtons callback_CategoryButtons) {
        this.mData = data;
        this.mRecyclerViewCategoryIndex = 0;
        this.mCategoryText = categoryText;
        this.mCategoryIndex = categoryIndex;
        this.mCallback_CategoryButtons = callback_CategoryButtons;

        //position 0為CategoryViewHolder
        mData.add(0, null);
        notifyItemInserted(0);
    }

    public void cleanData(){
        mData.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if(viewType == Constants.RECYCLER_VIEW_TYPE_LOADING){
            return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_loading, parent, false));
        }else if(viewType == Constants.RECYCLER_VIEW_TYPE_CATEGORY){
            return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_category, parent, false), mCategoryText, mCategoryIndex, mCallback_CategoryButtons);
        }else{
            if(viewType == Constants.RECYCLER_VIEW_TYPE_ITEM){
                return new MusicStoryListViewAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_musicstory_list, parent, false));
            }else{
                return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
            loadingViewHolder.pb_load_more.setIndeterminate(true);
        }else if(viewHolder instanceof CategoryViewHolder){
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) viewHolder;
        }else if(viewHolder instanceof MusicStoryListViewAdapter.ViewHolder) {
            MusicStoryListViewAdapter.ViewHolder holder = (MusicStoryListViewAdapter.ViewHolder) viewHolder;
            MusicStory musicStory = getItem(position);
            holder.itemView.setTag(R.id.tag_position, position);
            Context cnx = holder.itemView.getContext();

            GlideManager.setCardImage(cnx, musicStory.getCoverUrl(), holder.iv_card_cover_center);
            GlideManager.setFbAvatarImage(cnx, musicStory.getFbId(), GlideManager.FbAvatarType.TYPE_SMALL, holder.iv_card_author_avatar);
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

            if(mFirstSongItemPosition == -1){
                mFirstSongItemPosition = position;
                if(!SharedPreferencesManager.getBoolean(SharedPreferencesManager.IS_TAP_TARGET_MUSIC_STORY_LIST_BUTTON_LISTEN_SHOWN, false)){
                    ((MainActivity)cnx).requestShowTarget(
                            TapTargetManager.getTapTargetForView(holder.iv_card_play, "到KKBOX聽音樂囉~", "♪~♫~♪~♫~")
                    );
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mRecyclerViewCategoryIndex){
            return Constants.RECYCLER_VIEW_TYPE_CATEGORY;
        }else if(getItem(position) == null){
            return Constants.RECYCLER_VIEW_TYPE_LOADING;
        }else{
            return Constants.RECYCLER_VIEW_TYPE_ITEM;
        }
    }

    protected static class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView((R.id.pb_load_more))
        ProgressBar pb_load_more;
        public LoadingViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
            pb_load_more.getIndeterminateDrawable().setColorFilter(Common.getApp().getResources().getColor(R.color.app_theme), MULTIPLY);
        }
    }

    protected static class CategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_listitem_category)
        LinearLayout ll_listitem_category;
        @BindView(R.id.categorybuttons_listitem_category) CategoryButtons categorybuttons_listitem_category;
        public CategoryViewHolder(View v, String[] category, int index_category, CategoryButtons.Callback_CategoryButtons callback_CategoryButtons) {
            super(v);
            ButterKnife.bind(this, itemView);
            ll_listitem_category.setBackgroundColor(Common.getApp().getResources().getColor(R.color.categorybuttons_background));
            categorybuttons_listitem_category.setCategory(category, index_category, callback_CategoryButtons);
        }
    }

    public Callback.LoadMore loadMore(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mData.size()>0 && mData.get(mData.size()-1)!=null) {
                    mData.add(null);
                    notifyItemInserted(mData.size() - 1);
                }
            }
        });

        return new Callback.LoadMore() {
            @Override
            public void onLoadMore(Object object) {
                if(mData.size()>1 && mData.get(mData.size()-1)==null) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mData.remove(mData.size() - 1);
                            notifyItemRemoved(mData.size());

                            mData.addAll((ArrayList<MusicStory>) object);
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public MusicStory getItem(int position) {
        if(mCategoryText != null && position == 0){
            return null;
        }else if(mData != null && position >= 0 && position < mData.size()) {
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

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}
