package org.metol.musicstory.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;

import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.R;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

public class InfoActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View inflated = mVS_custom.inflate();
    }

    @Override
    protected int getCustomView() {
        return R.layout.activity_info;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected String getStaticCenterTitle() {
        return getResources().getString(R.string.info_title);
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

    }
}
