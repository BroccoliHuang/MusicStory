package org.metol.musicstory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;

import org.metol.musicstory.activity.BaseActivity;
import org.metol.musicstory.adapter.MusicStoryListViewAdapter;
import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.listener.EndlessRecyclerOnScrollListener;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.util.Callback;
import org.metol.musicstory.widget.CategoryButtons;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class MusicStoryListFragment extends BaseFragment implements CategoryButtons.Callback_CategoryButtons{
    MusicStoryListViewAdapter adapter;
    private int category;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        category = getArguments().getInt(Constants.ARGUMENTS_CATEGORY);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(endlessRecyclerOnScrollListener != null) endlessRecyclerOnScrollListener.reset(0, true);
    }

    @Override
    protected void getRecyclerViewAdapter(final Callback_Adapter callback_adapter, final int index_category) {
        if(adapter!=null) adapter.cleanData();
        loadData(category, 1, new Callback.API() {
            @Override
            public void onSuccess(Object object) {
                adapter = new MusicStoryListViewAdapter((ArrayList<MusicStory>)object, getCategoryText(), index_category, new CategoryButtons.Callback_CategoryButtons() {
                    @Override
                    public void onButtonClick(int position) {
                        category = getCategoryIndex()[position];
                        load(position);
                    }
                });
                callback_adapter.onAdapter(adapter);
                setLoadMore();
            }
            @Override
            public void onFailed(String reason) {

            }
        });
    }

    private void setLoadMore(){
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener((LinearLayoutManager) rv_card.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {

                final Callback.LoadMore callback_LoadMore = adapter.loadMore();

                loadData(category, current_page, new Callback.API() {
                    @Override
                    public void onSuccess(Object object) {
                        callback_LoadMore.onLoadMore(object);
                    }

                    @Override
                    public void onFailed(String reason) {

                    }
                });
            }
        };
        rv_card.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @Override
    public void onButtonClick(int position) {
        switch (position){
            case 0:
                category = Constants.CATEGORY_ALL;
                break;
        }
        load(position);
    }

    public static Fragment newInstance(int category) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ARGUMENTS_CATEGORY, category);
        MusicStoryListFragment albumListFragment = new MusicStoryListFragment();
        albumListFragment.setArguments(bundle);
        return albumListFragment;
    }

    private DocumentSnapshot myLastVisible = null;
    private void loadData(int category, int now_page, Callback.API callback_Api) {
        Firestore.getMusicStory(myLastVisible, 10, (now_page==0 ? ((BaseActivity)getActivity()).getProgressBar() : null), new Firestore.CallbackLoadMore() {
            @Override
            public void onSuccess(Object object) {
                callback_Api.onSuccess(object);
            }

            @Override
            public void onLoadMore(DocumentSnapshot lastVisible) {
                myLastVisible = lastVisible;
            }

            @Override
            public void onFailed(String reason) {

            }
        });
    }
    @Override
    protected String[] getCategoryText() {
        return new String[]{"全部"};
    }

    @Override
    protected int[] getCategoryIndex() {
        return new int[]{Constants.CATEGORY_ALL};
    }

    @Override
    protected boolean getSwipeRefreshLayoutEnable() {
        return true;
    }

    @Override
    protected boolean refreshOnResume() {
        return true;
    }
}
