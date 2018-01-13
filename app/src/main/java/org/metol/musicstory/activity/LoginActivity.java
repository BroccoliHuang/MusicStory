package org.metol.musicstory.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.metol.musicstory.BuildConfig;
import org.metol.musicstory.Common;
import org.metol.musicstory.R;
import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.model.Setting;
import org.metol.musicstory.util.Api;
import org.metol.musicstory.util.SystemManager;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */
//TODO 加入Google Login
public class LoginActivity extends AppCompatActivity {
	public static final String IS_INTENT_BY_ACTIVITY = "is_intent_by_activity";
	private Context mContext;
	private static final int LESS_DELAY_TIME_MILLIS_BEFORE_ENTER_MAIN = 1150;
	private Long startTime;
	private int animate_finish_in = -1;
	private int animate_finish_out = -1;
	private Snackbar mSnackbar = null;
	private CallbackManager callbackManager;
	private Setting setting;
	@BindView(R.id.rl_login_button) RelativeLayout rl_login_button;
	@BindView(R.id.login_button) LoginButton login_button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);
		mContext = this;
		//取得開始時間
		startTime = System.currentTimeMillis();

		if(getIntent().getBooleanExtra(IS_INTENT_BY_ACTIVITY, false)){
			animate_finish_in = R.anim.slide_in_left;
			animate_finish_out = R.anim.slide_out_right;
		}

		Firestore.getSetting(null, new Firestore.Callback() {
			@Override
			public void onSuccess(Object... object) {
				setting = (Setting)object[0];
				if(BuildConfig.BUILD_TYPE.toLowerCase().equals("debug") || SystemManager.getAppVersionName().equals(setting.getVersion())) {
					afterCheckVersion();
				}else{
					if(setting.isForce_update()){
						new AlertDialog.Builder(LoginActivity.this)
								.setTitle("更新")
								.setMessage("檢查到新版本需要更新")
								.setPositiveButton("好", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										SystemManager.gotoGooglePlay(LoginActivity.this, SystemManager.getPackageName());
										finish();
									}
								})
								.setCancelable(false)
								.show();
					}else{
						new AlertDialog.Builder(LoginActivity.this)
								.setTitle("更新")
								.setMessage("檢查到新版本，要更新嗎?")
								.setPositiveButton("好", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										SystemManager.gotoGooglePlay(LoginActivity.this, SystemManager.getPackageName());
										finish();
									}
								})
								.setNegativeButton("不要", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										afterCheckVersion();
									}
								})
								.setCancelable(false)
								.show();
					}
				}
			}

			@Override
			public void onFailed(String reason) {
				Snackbar.make(findViewById(R.id.rl_login), reason, Snackbar.LENGTH_SHORT).show();
			}
		});
	}

	private void afterCheckVersion(){
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if(accessToken==null) {
			rl_login_button.setVisibility(View.VISIBLE);
			//FB Login
			callbackManager = CallbackManager.Factory.create();
			login_button.setReadPermissions(Arrays.asList("public_profile","email"));

			login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
				@Override
				public void onSuccess(LoginResult loginResult) {
					rl_login_button.setVisibility(View.GONE);
					Api.getFBAccountData(loginResult.getAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), new Api.CallbackFBAccountData() {
						@Override
						public void onSuccess(String uid, String name, String gender, String birthday, String email) {
							Firestore.insertMember(new Member(uid, name, gender, email, birthday, 0, 0), null, new Firestore.Callback() {
								@Override
								public void onSuccess(Object... object) {
									afterFbLogin((String)object[0]);
								}

								@Override
								public void onFailed(String reason) {
								}
							});
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
				public void onCancel() {
					if (mSnackbar != null) mSnackbar.dismiss();
					mSnackbar = Snackbar.make(findViewById(R.id.rl_login), "目前只支援FB登入哦", Snackbar.LENGTH_LONG);
					mSnackbar.show();
				}

				@Override
				public void onError(FacebookException exception) {
					if (mSnackbar != null) mSnackbar.dismiss();
					mSnackbar = Snackbar.make(findViewById(R.id.rl_login), "發生錯誤", Snackbar.LENGTH_LONG);
					mSnackbar.show();
				}
			});
		}else{
			afterFbLogin("fb-"+accessToken.getUserId());
		}
	}

	private void afterFbLogin(String uid){
		Firestore.getMember(uid, null, new Firestore.Callback() {
			@Override
			public void onSuccess(Object... object) {
				Common.setUid(uid);
				Common.setMember((Member)object[0]);
				startTutorialOrMain();
			}

			@Override
			public void onFailed(String reason) {

			}
		});
	}

	private void startTutorialOrMain(){
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.putExtra(Constants.ARGUMENTS_ANNOUNCEMENT_TITLE, setting.getAnnouncement_title());
				intent.putExtra(Constants.ARGUMENTS_ANNOUNCEMENT_CONTENT, setting.getAnnouncement_content());
				intent.setClass(mContext, MainActivity.class);
				startActivity(intent);
				animate_finish_in = R.anim.slide_in_right;
				animate_finish_out = R.anim.slide_out_left;
				finish();
			}}, LESS_DELAY_TIME_MILLIS_BEFORE_ENTER_MAIN-(System.currentTimeMillis()-startTime));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(callbackManager!=null) callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void finish() {
		super.finish();

		if(animate_finish_in != -1 && animate_finish_out != -1){
			overridePendingTransition(animate_finish_in, animate_finish_out);
		}
	}
}