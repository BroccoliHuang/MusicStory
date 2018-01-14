package org.metol.musicstory.activity;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.metol.musicstory.Common;
import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.R;
import org.metol.musicstory.model.BroadCastEvent;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.util.ImageUtils;
import org.metol.musicstory.util.SharedPreferencesManager;
import org.metol.musicstory.util.StatusManager;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

public class ProfileActivity extends BaseActivity {
    private MenuItem            mi_save;
    private ImageView           iv_avatar;
    private TextView            tv_name;
    private RelativeLayout      rl_my_story;
    private ImageView           iv_my_story;
    private TextView            tv_my_story;
    private RadioButton         rb_male;
    private RadioButton         rb_female;
    private MaterialEditText    met_birth;
    private MaterialEditText    met_email;
    private Button              btn_logout;
    private boolean mIsModify = false;
    private Member mMember;
    private String birthDate = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View inflated = mVS_custom.inflate();

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(0);

        rl_my_story = (RelativeLayout)inflated.findViewById(R.id.rl_my_story);
        iv_my_story = (ImageView)inflated.findViewById(R.id.iv_my_story);
        tv_my_story = (TextView)inflated.findViewById(R.id.tv_my_story);

        Common.getMember(true, new Common.CallbackMember() {
            @Override
            public void onMember(Member member) {
                mMember = member;
                iv_avatar = (ImageView)inflated.findViewById(R.id.iv_avatar);
                tv_name = ((TextView)inflated.findViewById(R.id.tv_name));
                rb_male = ((RadioButton)inflated.findViewById(R.id.rb_male));
                rb_female = ((RadioButton)inflated.findViewById(R.id.rb_female));
                met_birth = ((MaterialEditText)inflated.findViewById(R.id.met_birth));
                met_email = ((MaterialEditText)inflated.findViewById(R.id.met_email));
                btn_logout = ((Button)inflated.findViewById(R.id.btn_logout));

                ImageUtils.setAvatarImage(ProfileActivity.this, mMember.getUid(), ImageUtils.FbAvatarType.TYPE_LARGE, iv_avatar);
                tv_name.setText(member.getName());
                if(!TextUtils.isEmpty(member.getGender())) {
                    rb_male.setChecked(member.getGender().equals("male"));
                    rb_female.setChecked(member.getGender().equals("female"));
                }
                birthDate = member.getBirthDate();
                met_birth.setText(member.getBirthDate());
                met_email.setText(member.getEmail());

                View.OnClickListener onClickListenerMyStory = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ProfileActivity.this, MyStoryActivity.class), ActivityOptions.makeCustomAnimation(ProfileActivity.this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
                    }
                };

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        setModify(true);
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                };
                CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setModify(true);
                    }
                };
                rl_my_story.setOnClickListener(onClickListenerMyStory);
                iv_my_story.setOnClickListener(onClickListenerMyStory);
                tv_my_story.setOnClickListener(onClickListenerMyStory);
                rb_male.setOnCheckedChangeListener(onCheckedChangeListener);
                rb_female.setOnCheckedChangeListener(onCheckedChangeListener);
                met_birth.setFocusable(false);
                met_birth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                setModify(true);
                                birthDate = String.format("%02d", year)+String.format("%02d", month+1)+String.format("%02d", dayOfMonth);
                                met_birth.setText(birthDate);
                            }
                        }, mYear, mMonth, mDay).show();
                    }
                });
                met_email.addTextChangedListener(textWatcher);

                btn_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("登出")
                                .setMessage("確定要登出嗎?")
                                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        StatusManager.Logout();
                                        EventBus.getDefault().post(new BroadCastEvent(BroadCastEvent.BroadCastType.MAIN_ACTIVITY_REFRESH_AFTER_LOGIN_OR_LOGOUT, null));
                                        finish();
                                    }
                                })
                                .setNegativeButton("不要", null)
                                .setCancelable(false)
                                .show();
                    }
                });
            }
        });

        if(!SharedPreferencesManager.getBoolean(SharedPreferencesManager.IS_TAP_TARGET_PROFILE_MY_STORY_BUTTON_SHOWN, false)){
            requestShowTarget(
                    Common.getTapTargetManager().getTapTargetForView(rl_my_story, "我的故事", "這裡可以修改寫過的故事~")
            );
        }
    }

    private void setModify(boolean isModify){
        mIsModify = isModify;
        mi_save.setVisible(isModify);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        mi_save = menu.findItem(R.id.action_save);
        mi_save.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == mi_save.getItemId()){
            if(rb_male.isChecked()){
                mMember.setGender("male");
            }else if(rb_female.isChecked()){
                mMember.setGender("female");
            }
            mMember.setBirthDate(birthDate);
            mMember.setEmail(met_email.getText().toString());
            Firestore.updateMember(mMember, getProgressBar(), new Firestore.Callback() {
                @Override
                public void onSuccess(Object... object) {
                    showSnack("已儲存");
                    setModify(false);
                }

                @Override
                public void onFailed(String reason) {
                    showSnack("儲存失敗:(");
                }
            });

            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(mIsModify){
            new AlertDialog.Builder(this)
            .setTitle("儲存")
            .setMessage("還沒儲存就要離開了嗎QQ?")
            .setNegativeButton("沒", null)
            .setPositiveButton("對", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mIsModify = false;
                    onBackPressed();
                }
            }).show();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected int getCustomView() {
        return R.layout.activity_profile;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected String getStaticCenterTitle() {
        return getResources().getString(R.string.profile_title);
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
        Common.getTapTargetManager().showTutorialForView(ProfileActivity.this, new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                SharedPreferencesManager.putBoolean(SharedPreferencesManager.IS_TAP_TARGET_PROFILE_MY_STORY_BUTTON_SHOWN, true);
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
