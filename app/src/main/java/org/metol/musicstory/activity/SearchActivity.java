package org.metol.musicstory.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.R;
import org.metol.musicstory.fragment.KKBOXSearchListFragment;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.util.SharedPreferencesManager;
import org.metol.musicstory.util.TapTargetManager;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

public class SearchActivity extends BaseActivity {
    @Override
    protected String[] getTabTitle() {
        return new String[]{"依歌名", "依歌手", "依專輯"};
    }

    @Override
    protected ArrayList<Fragment> getTabFragment() {
        ArrayList<Fragment> al_fragment = new ArrayList();
        al_fragment.add(KKBOXSearchListFragment.newInstanceForSearch(Constants.TYPE_SEARCH_TRACK_BY_TRACK,  keyword));
        al_fragment.add(KKBOXSearchListFragment.newInstanceForSearch(Constants.TYPE_SEARCH_TRACK_BY_ARTIST, keyword));
        al_fragment.add(KKBOXSearchListFragment.newInstanceForSearch(Constants.TYPE_SEARCH_TRACK_BY_ALBUM,  keyword));
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

//        mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
//            @Override
//            public void onMenuClick() {
//                finish();
//            }
//        });
        // mSearchView.setShouldClearOnOpen(true);
        super.onCreate(savedInstanceState);
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
        return true;
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
        TapTargetManager.showTutorialForView(SearchActivity.this, new TapTargetSequence.Listener() {
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