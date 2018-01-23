package com.worse.more.breaker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.core.BaseActivity;
import com.app.core.prompt.Toaster;

import org.ayo.component.core.SystemBarTintManager;
import org.ayo.core.Lang;
import org.ayo.log.LogReporter;
import org.ayo.view.widget.TitleBar;


public class LoginActivity extends BaseActivity {

	public static Intent getStartIntent(Activity a){
		Intent intent = new Intent(a, LoginActivity.class);
		return intent;
	}

	ImageView iv_login_avatar;
	RelativeLayout rl_user;
	Button btn_login;
	Button btn_regist;
	TextURLView tv_forget_password;
	EditText et_account;
	EditText et_password;
	TitleBar titlebar;



	@Override
	protected int getLayoutId() {
		return R.layout.acct_act_ac_login;
	}

	@Override
	protected void onCreate2(View view, @Nullable Bundle bundle) {

		iv_login_avatar = fid(R.id.iv_login_avatar);
		rl_user = fid(R.id.rl_user);
		btn_login = fid(R.id.btn_login);
		btn_regist = fid(R.id.btn_regist);
		tv_forget_password = fid(R.id.tv_forget_password);
		et_account = fid(R.id.et_account);
		et_password = fid(R.id.et_password);
		titlebar = fid(R.id.titlebar);

		SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintColor(Lang.rcolor(R.color.theme_color));
		tintManager.setNavigationBarTintColor(Lang.rcolor(R.color.theme_color));

		titlebar.title("登录")
				.bgColor(Lang.rcolor(R.color.theme_color))
				.callback(new TitleBar.Callback() {
					@Override
					public void onLeftButtonClicked(View v) {
						finish();
					}

					@Override
					public void onRightButtonClicked(int index, View v) {

					}
				});


		tv_forget_password.setText("忘记密码?");

		Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.act_anim_login_frame);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		String username = Config.account.getUsername();
		String pwd = Config.account.getPassword();
		et_account.setText(username);
		et_password.setText(pwd);

		btn_login.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				clickLogin();
			}
		});
	}

	@Override
	protected void onDestroy2() {
	}

	@Override
	protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

	}


	public void clickLogin(){
		String username = et_account.getText().toString();
		String pwd = et_password.getText().toString();
		if(TextUtils.isEmpty(username)){
			Toaster.toastLong("请输入您的账号");
			return;
		}
		if(TextUtils.isEmpty(pwd)){
			Toaster.toastLong("请输入您的密码");
			return;
		}

		Config.account.saveLoginInfo(username, pwd);
	}



	public void onLoginOk(){
		Toaster.toastShort("登录成功");

		try {
			String s = null;
			Log.e("dddd", s.length() + "");
		}catch (Exception e){
			e.printStackTrace();
			LogReporter.report("登录出错", e.getMessage(), Lang.readThrowable(e));
		}

		finish();
	}

	public void onLoginFail(String errorReason){
		Toaster.toastShort(errorReason);
	}
}


