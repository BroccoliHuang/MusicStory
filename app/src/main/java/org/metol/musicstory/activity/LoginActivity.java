package org.metol.musicstory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.metol.musicstory.Common;
import org.metol.musicstory.R;
import org.metol.musicstory.database.Firestore;
import org.metol.musicstory.model.Member;
import org.metol.musicstory.util.Api;
import org.metol.musicstory.util.BirthdayUtil;

import butterknife.ButterKnife;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

//TODO 強制更新
//TODO 還沒有確定需不需要FB註冊登入之前，先不顯示FB按鈕，用全藍色加中間ICON當背景，需要登入再動畫帶入login按鈕
public class LoginActivity extends AppCompatActivity {
	public static final String IS_INTENT_BY_ACTIVITY = "is_intent_by_activity";
	private Context mContext;
	private static final int LESS_DELAY_TIME_MILLIS_BEFORE_ENTER_MAIN = 1150;
	private Long startTime;
	private int animate_finish_in = -1;
	private int animate_finish_out = -1;
	private Snackbar mSnackbar = null;
	private CallbackManager callbackManager;


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

		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if(accessToken==null) {
			//FB Login
			callbackManager = CallbackManager.Factory.create();
			LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
			//TODO 測試
//			loginButton.setReadPermissions("email");

			loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
				@Override
				public void onSuccess(LoginResult loginResult) {
					Api.getFBAccountData(loginResult.getAccessToken().getUserId(), AccessToken.getCurrentAccessToken().getPermissions(), new Api.CallbackFBAccountData() {
						@Override
						public void onSuccess(String id, String name, String gender, String birthday, String email, String address) {
							Firestore.insertMember(new Member(id, name, "", gender, email, BirthdayUtil.fbBirthday(birthday), "", address, 0, 0), new Firestore.Callback() {
								@Override
								public void onSuccess(Object... object) {
									afterFbLogin(id);
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
			afterFbLogin(accessToken.getUserId());
		}
	}

	private void afterFbLogin(String fbId){
		Firestore.getMember(fbId, new Firestore.Callback() {
			@Override
			public void onSuccess(Object... object) {
				Common.setFbID(fbId);
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