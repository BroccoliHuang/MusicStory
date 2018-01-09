package org.metol.musicstory.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;

import org.metol.musicstory.R;
import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.util.GlideManager;
import java.util.ArrayList;
import ren.qinc.edit.PerformEdit;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

public class AddStoryActivity extends BaseActivity {
    private FrameLayout fl_header;
    private ImageView   iv_sheet_cover;
    private ImageView   iv_sheet_background;
    private TextView    tv_sheet_song_name;
    private TextView    tv_sheet_artist;
    private EditText    et_story_content;
    private PerformEdit mPerformEditStoryContent;
    private MenuItem    mi_undo;
    private MenuItem    mi_redo;
    private MenuItem    mi_ok;
    private MusicStory  musicStory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicStory musicStory = (MusicStory)getIntent().getBundleExtra(Constants.ARGUMENTS_MUSICSTORY).get(Constants.ARGUMENTS_MUSICSTORY);

        View inflated = mVS_custom.inflate();

        fl_header = (FrameLayout)inflated.findViewById(R.id.fl_header);
        iv_sheet_cover = (ImageView)inflated.findViewById(R.id.iv_sheet_cover);
        iv_sheet_background = (ImageView)inflated.findViewById(R.id.iv_sheet_background);
        tv_sheet_song_name = (TextView) inflated.findViewById(R.id.tv_sheet_song_name);
        tv_sheet_artist = (TextView)inflated.findViewById(R.id.tv_sheet_artist);
        et_story_content = (EditText)inflated.findViewById(R.id.et_story_content);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(0);

        GlideManager.setSongImage(AddStoryActivity.this, musicStory.getCoverUrl(), iv_sheet_cover);
        GlideManager.setBackgroundImageWithGaussianBlur(AddStoryActivity.this, musicStory.getCoverUrl(), iv_sheet_background);
        View.OnClickListener onClick_TextMarquee = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
            }
        };
        tv_sheet_song_name.setOnClickListener(onClick_TextMarquee);
        tv_sheet_artist.setOnClickListener(onClick_TextMarquee);
        tv_sheet_song_name.setText(musicStory.getSongName());
        tv_sheet_artist.setText(musicStory.getArtistName());

        mPerformEditStoryContent = new PerformEdit(et_story_content);
    }

    @Override
    protected int getCustomView() {
        return R.layout.activity_addstory;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_story, menu);

        mi_undo = menu.findItem(R.id.action_undo);
        mi_redo = menu.findItem(R.id.action_redo);
        mi_ok   = menu.findItem(R.id.action_ok);

        mi_undo.setVisible(true);
        mi_redo.setVisible(true);
        mi_ok.setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == mi_undo.getItemId()) {
            mPerformEditStoryContent.undo();
            return true;
        }else if(item.getItemId() == mi_redo.getItemId()) {
            mPerformEditStoryContent.redo();
            return true;
        }else if(item.getItemId() == mi_ok.getItemId()) {
            //TODO
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected String getStaticCenterTitle() {
        return getResources().getString(R.string.addstory_title);
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
