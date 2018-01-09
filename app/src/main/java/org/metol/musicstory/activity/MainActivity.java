package org.metol.musicstory.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import org.metol.musicstory.fragment.MusicStoryListFragment;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.util.TapTargetManager;
import org.metol.musicstory.R;
import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.util.SharedPreferencesManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;


/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

public class MainActivity extends BaseActivity {
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ButterKnife.bind(this);

		if(SharedPreferencesManager.getBoolean(SharedPreferencesManager.IS_TAP_TARGET_TOOLBAR_SEARCH_BUTTON_SHOWN, false)){
			requestShowTarget(
					TapTargetManager.getTapTargetForMenuItem(mToolbar, R.id.action_search, "搜尋", "透過搜尋來新增故事吧~")
			);
		}

		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	private int quitCount = 0;
	@Override
	public void onBackPressed() {
		quitCount++;
		if(quitCount==1){
			showSnack("再按一次離開", Snackbar.LENGTH_SHORT);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					quitCount = 0;
				}
			}, 2000);
		}else{
			super.onBackPressed();
		}
	}

	@Override
	protected ArrayList<Fragment> getTabFragment() {
		ArrayList<Fragment> al_fragment = new ArrayList();
		al_fragment.add(MusicStoryListFragment.newInstance(Constants.CATEGORY_ALL));

//		al_fragment.add(KKBOXSearchListFragment.newInstance(Enum.CATEGORY_SONG_RECOMMEND));
//		al_fragment.add(AlbumListFragment.newInstance(Enum.CATEGORY_ALL));
//		al_fragment.add(MusicBoxFragment.newInstance(Enum.CATEGORY_MUSICBOX));
//		al_fragment.add(PromotionFragment.newInstance(Enum.CATEGORY_PROMOTION));
		return al_fragment;
	}

	@Override
	protected void shownTapTarget(TapTarget... tapTargets) {
		TapTargetManager.showTutorialForView(MainActivity.this, new TapTargetSequence.Listener() {
			@Override
			public void onSequenceFinish() {
				SharedPreferencesManager.putBoolean(SharedPreferencesManager.IS_TAP_TARGET_MUSIC_STORY_LIST_BUTTON_LISTEN_SHOWN, true);
			}
			@Override
			public void onSequenceStep(TapTarget tapTarget, boolean b) {
			}
			@Override
			public void onSequenceCanceled(TapTarget tapTarget) {
			}
		}, tapTargets);
	}

	@Override
	protected CardBottomSheetFragment getCardBottomSheetFragment() {
		return new CardBottomSheetFragment();
	}

	@Override
	protected void intentSearchActivity(String keyword) {
		Intent intent = new Intent(MainActivity.this, SearchActivity.class);
		intent.putExtra(Constants.ARGUMENTS_KEYWORD, keyword);
		startActivity(intent, ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
	}

	@Override
	protected void intentInfoActivity() {
		startActivity(new Intent(MainActivity.this, InfoActivity.class), ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
	}

	@Override
	protected String getStaticCenterTitle() {
		return null;
	}

	@Override
	protected boolean isSearchVisible() {
		return true;
	}

	@Override
	protected boolean isInfoVisible() {
		return true;
	}

	@Override
	protected int getCustomView() {
		return 0;
	}

	@Override
	protected String[] getTabTitle() {
		return new String[]{getString(R.string.app_name)};
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
	protected boolean canToolBarBack() {
		return false;
	}
}
