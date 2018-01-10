package org.metol.musicstory.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;

import org.greenrobot.eventbus.EventBus;
import org.metol.musicstory.Common;
import org.metol.musicstory.R;
import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.model.BroadCastEvent;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.util.GlideManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ren.qinc.edit.PerformEdit;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

//TODO 是否不儲存離開
//TODO 送出資料時要progressbar
//TODO hashtag https://github.com/greenfrvr/hashtag-view
public class EditStoryActivity extends BaseActivity {
    public static final int TYPE_ADD    = 0;
    public static final int TYPE_EDIT   = 1;
    private int type = -1;
    private String storyDocumentId;

    private FrameLayout fl_header;
    private ImageView   iv_sheet_cover;
    private ImageView   iv_sheet_background;
    private TextView    tv_sheet_song_name;
    private TextView    tv_sheet_artist;
    private EditText    et_story_title;
    private EditText    et_story_content;
    private PerformEdit mPerformEditStoryContent;
    private MenuItem    mi_undo;
    private MenuItem    mi_redo;
    private MenuItem    mi_ok;
    private MusicStory  musicStory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra(Constants.ARGUMENTS_TYPE, -1);
        storyDocumentId = getIntent().getStringExtra(Constants.ARGUMENTS_STORY_ID);
        musicStory = (MusicStory)getIntent().getBundleExtra(Constants.ARGUMENTS_MUSICSTORY).get(Constants.ARGUMENTS_MUSICSTORY);

        View inflated = mVS_custom.inflate();

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(0);

        fl_header = (FrameLayout)inflated.findViewById(R.id.fl_header);
        iv_sheet_cover = (ImageView)inflated.findViewById(R.id.iv_sheet_cover);
        iv_sheet_background = (ImageView)inflated.findViewById(R.id.iv_sheet_background);
        tv_sheet_song_name = (TextView) inflated.findViewById(R.id.tv_sheet_song_name);
        tv_sheet_artist = (TextView)inflated.findViewById(R.id.tv_sheet_artist);
        et_story_title = (EditText)inflated.findViewById(R.id.et_story_title);
        et_story_content = (EditText)inflated.findViewById(R.id.et_story_content);

        GlideManager.setSongImage(EditStoryActivity.this, musicStory.getCoverUrl(), iv_sheet_cover);
        GlideManager.setBackgroundImageWithGaussianBlur(EditStoryActivity.this, musicStory.getCoverUrl(), iv_sheet_background);
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
        et_story_title.setText(musicStory.getStoryTitle());
        et_story_content.setText(musicStory.getStoryContent());

        mPerformEditStoryContent = new PerformEdit(et_story_content);

        if(type==TYPE_EDIT) {
            Firestore.getMusicStoryByDocumentId(storyDocumentId, new Firestore.Callback() {
                @Override
                public void onSuccess(Object... object) {
                    MusicStory musicStory = (MusicStory)object[0];
                    et_story_title.setText(musicStory.getStoryTitle());
                    et_story_content.setText(musicStory.getStoryContent());
                }

                @Override
                public void onFailed(String reason) {

                }
            });
        }
    }

    @Override
    protected int getCustomView() {
        return R.layout.activity_edit_story;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_story, menu);

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
            if(TextUtils.isEmpty(et_story_title.getText().toString())) {
                showSnack("請輸入標題");
            }else if(TextUtils.isEmpty(et_story_content.getText().toString())){
                showSnack("寫下故事吧");
            }else {
                Common.getMember(false, new Common.CallbackMember() {
                    @Override
                    public void onMember(Member member) {
                        if(member==null){
                            showSnack("故事新增失敗="+"找不到會員");
                            return;
                        }
                        ArrayList<String> alTag = new ArrayList();

                        //TODO 日期、地點、取得座標、hash tag
                        musicStory.setStoryTitle(et_story_title.getText().toString());
                        musicStory.setStoryContent(et_story_content.getText().toString());
                        musicStory.setFbId(member.getFbId());
                        musicStory.setFbName(member.getFbName());
                        musicStory.setNickname(member.getNickname());
                        musicStory.setStoryDate("");
                        musicStory.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                        musicStory.setLocation("");
                        musicStory.setLongitude("");
                        musicStory.setLatitude("");
                        musicStory.setTag(alTag);

                        if(type==TYPE_ADD){
                            Firestore.insertMusicStory(musicStory, new Firestore.Callback() {
                                @Override
                                public void onSuccess(Object... object) {
                                    EventBus.getDefault().post(new BroadCastEvent(BroadCastEvent.BroadCastType.SEARCH_ACTIVITY_SHOW_SNACK_BAR, "故事新增成功"));
                                    finish();
                                }

                                @Override
                                public void onFailed(String reason) {
                                    showSnack("故事新增失敗=" + reason);
                                }
                            });
                        }else if(type==TYPE_EDIT){
                            Firestore.updateMusicStory(storyDocumentId, musicStory, new Firestore.Callback() {
                                @Override
                                public void onSuccess(Object... object) {
                                    EventBus.getDefault().post(new BroadCastEvent(BroadCastEvent.BroadCastType.SEARCH_ACTIVITY_SHOW_SNACK_BAR, "故事更新成功"));
                                    finish();
                                }

                                @Override
                                public void onFailed(String reason) {
                                    showSnack("故事更新失敗=" + reason);
                                }
                            });
                        }
                    }
                });
            }
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
        if(type==TYPE_ADD) {
            return getResources().getString(R.string.addstory_title);
        }else if(type==TYPE_EDIT) {
            return getResources().getString(R.string.editstory_title);
        }else{
            return "";
        }
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
