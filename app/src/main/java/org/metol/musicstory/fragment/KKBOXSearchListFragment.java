package org.metol.musicstory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.metol.musicstory.activity.BaseActivity;
import org.metol.musicstory.adapter.KKBOXSearchTrackListViewAdapter;
import org.metol.musicstory.listener.EndlessRecyclerOnScrollListener;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.model.KKBOX.Search.Search;
import org.metol.musicstory.model.KKBOX.Tracks;
import org.metol.musicstory.util.Api;
import org.metol.musicstory.util.Callback;

import java.util.ArrayList;

/**
 * Created by broccoli on 2017/4/21.
 */

public class KKBOXSearchListFragment extends BaseFragment{
    private KKBOXSearchTrackListViewAdapter adapter;
    private int type;
    private String keyword;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    public static Fragment newInstanceForSearch(String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGUMENTS_KEYWORD, keyword);

        KKBOXSearchListFragment kkboxSearchListFragment = new KKBOXSearchListFragment();
        kkboxSearchListFragment.setArguments(bundle);
        return kkboxSearchListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt(Constants.ARGUMENTS_TYPE);
        keyword = getArguments().getString(Constants.ARGUMENTS_KEYWORD);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(endlessRecyclerOnScrollListener != null) endlessRecyclerOnScrollListener.reset(0, true);
    }

    @Override
    protected void getRecyclerViewAdapter(final Callback_Adapter callback_adapter, final int index_category) {
        loadData(type, 0, new Callback.API() {
            @Override
            public void onSuccess(Object object) {
                adapter = new KKBOXSearchTrackListViewAdapter((ArrayList<Tracks>)object, keyword);
                callback_adapter.onAdapter(adapter);

                setLoadMore();
            }

            @Override
            public void onFailed(String reason) {

            }
        });
    }

    private void setLoadMore(){
        if(rv_card == null || rv_card.getLayoutManager() == null) return;
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener((LinearLayoutManager) rv_card.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {

                final Callback.LoadMore callback_LoadMore = adapter.loadMore();

                loadData(type, current_page, new Callback.API() {
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

    private void loadData(int type, int now_page, final Callback.API callback_Api) {
        String apiType = Api.KKBOXSearchType.TRACK;
        Api.getKKBOXSearch(getActivity(), keyword, apiType, "TW", now_page, 20, (now_page==0 ? ((BaseActivity)getActivity()).getProgressBar() : null),  new Api.Callback() {
            @Override
            public void onSuccess(@Nullable Object obj) {
                callback_Api.onSuccess(((Search)obj).getTracks().getData());
            }
            @Override
            public void onUnSuccess(int stateCode, String reason) {
            }
            @Override
            public void onFailed() {
            }
        });
    }

    @Override
    protected String[] getCategoryText() {
        return new String[]{};
    }

    @Override
    protected int[] getCategoryIndex() {
        return new int[]{};
    }

    @Override
    protected boolean getSwipeRefreshLayoutEnable() {
        return false;
    }

    @Override
    protected boolean refreshOnResume() {
        return false;
    }
}
