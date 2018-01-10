package org.metol.musicstory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.metol.musicstory.R;

import org.metol.musicstory.activity.BaseActivity;
import com.melnykov.fab.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.base.ObservableFragment;
import me.henrytao.smoothappbarlayout.base.Utils;

import static android.graphics.PorterDuff.Mode.MULTIPLY;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public abstract class BaseFragment extends Fragment implements ObservableFragment {
    @BindView(R.id.rv_card) RecyclerView rv_card;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.pb_loading) ProgressBar pb_loading;
    private Unbinder unbinder;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setColorSchemeResources(R.color.app_theme);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.app_theme_background);
        swipeRefreshLayout.setEnabled(getSwipeRefreshLayoutEnable());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        pb_loading.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.app_theme), MULTIPLY);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_card.setLayoutManager(linearLayoutManager);
        rv_card.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(linearLayoutManager.findFirstVisibleItemPosition()==0){
                    if(getActivity() instanceof BaseActivity){
                        ((BaseActivity)getActivity()).getFAB().hide();
                        setFAB(((BaseActivity)getActivity()).getFAB());
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        load(0);

//        category = getCategory();
//        if(category != null && category.length>0){
//            categorybuttons_category.setCategory(category, getCategoryCallback());
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(refreshOnResume()) refresh();
    }

    private void refresh(){
        load(0);
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void load(int index_category){
        //TODO 做成不要讓CategoryButtons消失的loading

        rv_card.setVisibility(View.GONE);
        pb_loading.setVisibility(View.VISIBLE);

//        nsv_categorybuttons_and_rv_card.setVisibility(View.GONE);
//        pb_load_more.setVisibility(View.VISIBLE);

        getRecyclerViewAdapter(new Callback_Adapter(){
            @Override
            public void onAdapter(RecyclerView.Adapter adapter) {
                if(rv_card!=null) rv_card.setAdapter(adapter);
                if(getActivity() instanceof BaseActivity) setFAB(((BaseActivity) getActivity()).getFAB());

                pb_loading.setVisibility(View.GONE);
                rv_card.setVisibility(View.VISIBLE);

//                nsv_categorybuttons_and_rv_card.setVisibility(View.VISIBLE);
//                pb_load_more.setVisibility(View.GONE);
            }
        }, index_category);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setFAB(final FloatingActionButton fab){
        if(rv_card==null) return;

        fab.attachToRecyclerView(rv_card);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayoutManager==null) return;
                if(rv_card==null) {
                    linearLayoutManager.scrollToPosition(0);
                }else{
                    if (linearLayoutManager.findFirstVisibleItemPosition() > 10 && linearLayoutManager.getItemCount() > 10) {
                        linearLayoutManager.scrollToPosition(10);
                    }
                    linearLayoutManager.smoothScrollToPosition(rv_card, null, 0);
                }
            }
        });
    }

    @Override
    public View getScrollTarget() {
        return rv_card;
    }

    @Override
    public boolean onOffsetChanged(SmoothAppBarLayout smoothAppBarLayout, View target, int verticalOffset) {
        return Utils.syncOffset(smoothAppBarLayout, target, verticalOffset, getScrollTarget());
    }

    protected interface Callback_Adapter{
        void onAdapter(RecyclerView.Adapter adapter);
    }

    protected abstract String[] getCategoryText();
    protected abstract int[] getCategoryIndex();
    protected abstract void getRecyclerViewAdapter(Callback_Adapter callback_adapter, int index_category);
    protected abstract boolean getSwipeRefreshLayoutEnable();
    protected abstract boolean refreshOnResume();
}