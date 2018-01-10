package org.metol.musicstory.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.getkeepsafe.taptargetview.TapTarget;
import com.melnykov.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.metol.musicstory.Common;
import org.metol.musicstory.R;
import org.metol.musicstory.adapter.MyStoryAdapter;
import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.model.BroadCastEvent;
import org.metol.musicstory.model.MusicStory;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

public class MyStoryActivity extends BaseActivity {
    private RecyclerView rv_my_story;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        View inflated = mVS_custom.inflate();
        
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(0);

        rv_my_story = (RecyclerView)inflated.findViewById(R.id.rv_my_story);

        linearLayoutManager = new LinearLayoutManager(MyStoryActivity.this);
        rv_my_story.setLayoutManager(linearLayoutManager);
        rv_my_story.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(linearLayoutManager.findFirstVisibleItemPosition()==0){
                    getFAB().hide();
                    setFAB(getFAB());
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        Firestore.getMusicStoryByFbId(Common.getFbID(), new Firestore.Callback() {
            @Override
            public void onSuccess(Object... object) {
                rv_my_story.setAdapter(new MyStoryAdapter((ArrayList<MusicStory>)object[0], (ArrayList<String>)object[1]));
            }

            @Override
            public void onFailed(String reason) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BroadCastEvent event) {
        if(event.getEventType() == BroadCastEvent.BroadCastType.SEARCH_ACTIVITY_SHOW_SNACK_BAR) {
            showSnack((String)event.getData());
        }
    }

    public void setFAB(final FloatingActionButton fab){
        if(rv_my_story==null) return;

        fab.attachToRecyclerView(rv_my_story);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearLayoutManager==null) return;
                if(rv_my_story==null) {
                    linearLayoutManager.scrollToPosition(0);
                }else{
                    if (linearLayoutManager.findFirstVisibleItemPosition() > 10 && linearLayoutManager.getItemCount() > 10) {
                        linearLayoutManager.scrollToPosition(10);
                    }
                    linearLayoutManager.smoothScrollToPosition(rv_my_story, null, 0);
                }
            }
        });
    }

    @Override
    protected int getCustomView() {
        return R.layout.activity_my_story;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected String getStaticCenterTitle() {
        return getResources().getString(R.string.my_story_title);
    }

    @Override
    protected String[] getTabTitle() {
        return null;
    }

    @Override
    protected int[] getTabIcon() {
        return null;
    }

    @Override
    protected boolean isShowTab() {
        return false;
    }

    @Override
    protected ArrayList<Fragment> getTabFragment() {
        return null;
    }

    @Override
    protected boolean canToolBarBack() {
        return true;
    }

    @Override
    protected boolean isSearchVisible() {
        return false;
    }

    @Override
    protected boolean isInfoVisible() {
        return false;
    }

    @Override
    protected CardBottomSheetFragment getCardBottomSheetFragment() {
        return new CardBottomSheetFragment();
    }

    @Override
    protected void intentSearchActivity(String keyword) {

    }

    @Override
    protected void intentInfoActivity() {

    }

    @Override
    protected void shownTapTarget(TapTarget... tapTargets) {

    }
}
