package org.metol.musicstory.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.metol.musicstory.Common;
import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.R;
import org.metol.musicstory.fragment.KKBOXSearchListFragment;
import org.metol.musicstory.fragment.NotYetFragment;
import org.metol.musicstory.model.BroadCastEvent;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.util.SharedPreferencesManager;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

public class SearchActivity extends BaseActivity {
    @Override
    protected String[] getTabTitle() {
        return new String[]{""};
    }

    @Override
    protected ArrayList<Fragment> getTabFragment() {
        ArrayList<Fragment> al_fragment = new ArrayList();
        al_fragment.add(KKBOXSearchListFragment.newInstanceForSearch(keyword));
        return al_fragment;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    protected String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        keyword = getIntent().getStringExtra(Constants.ARGUMENTS_KEYWORD);
        super.onCreate(savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BroadCastEvent event) {
        if(event.getEventType() == BroadCastEvent.BroadCastType.SEARCH_ACTIVITY_SHOW_SNACK_BAR) {
            showSnack((String)event.getData());
        }
    }

    @Override
    protected String getStaticCenterTitle() {
        return "搜尋結果";
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
    protected int getCustomView() {
        return 0;
    }

    @Override
    protected boolean canToolBarBack(){
        return true;
    }

    @Override
    protected int[] getTabIcon() {
        return new int[]{};
    }

    @Override
    protected boolean isShowTab() {
        return false;
    }

    @Override
    protected CardBottomSheetFragment getCardBottomSheetFragment() {
        return null;
    }

    @Override
    protected void intentSearchActivity(String keyword) {
    }

    @Override
    protected void intentInfoActivity() {
    }

    @Override
    protected void shownTapTarget(TapTarget... tapTargets) {
        Common.getTapTargetManager().showTutorialForView(SearchActivity.this, new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                SharedPreferencesManager.putBoolean(SharedPreferencesManager.IS_TAP_TARGET_SEARCH_BUTTON_LISTEN_SHOWN, true);
                SharedPreferencesManager.putBoolean(SharedPreferencesManager.IS_TAP_TARGET_SEARCH_BUTTON_ADD_STORY_SHOWN, true);
            }
            @Override
            public void onSequenceStep(TapTarget tapTarget, boolean b) {
            }
            @Override
            public void onSequenceCanceled(TapTarget tapTarget) {
            }
        }, tapTargets);
    }
}