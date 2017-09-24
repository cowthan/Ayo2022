package com.worse.more.breaker.ui.account;

import android.app.Activity;
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

import com.worse.more.breaker.Config;
import com.worse.more.breaker.R;
import com.worse.more.breaker.event.LoginOkEvent;
import com.worse.more.breaker.ui.BasePage;
import com.worse.more.breaker.ui.contact.LoginContact;
import com.worse.more.breaker.ui.widget.TextURLView;

import org.ayo.component.Master;
import org.ayo.component.core.SystemBarTintManager;
import org.ayo.component.mvp.AyoPresenter;
import org.ayo.core.Async;
import org.ayo.core.Lang;
import org.ayo.log.LogReporter;
import org.ayo.notify.loading.LoadingTopDialog;
import org.ayo.notify.toaster.Toaster;
import org.ayo.view.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BasePage implements LoginContact.View{

	public static void start(Activity a){
		Master.startPage(a, LoginActivity.class, null);
	}

	@Bind(R.id.iv_login_avatar)
	ImageView iv_login_avatar;

	@Bind(R.id.rl_user)
	RelativeLayout rl_user;

	@Bind(R.id.btn_login)
	Button btn_login;

	@Bind(R.id.btn_regist)
	Button btn_regist;

	@Bind(R.id.tv_forget_password)
	TextURLView tv_forget_password;

	@Bind(R.id.et_account)
	EditText et_account;

	@Bind(R.id.et_password)
	EditText et_password;

	@Bind(R.id.titlebar)
	TitleBar titlebar;

	private LoadingTopDialog loadDialog;
	
	LoginPresener loginPresener;

	@Override
	protected int getLayoutId() {
		return R.layout.act_ac_login;
	}

	@Override
	protected void onCreate2(View view, @Nullable Bundle bundle) {
		loginPresener = new LoginPresener();
		loginPresener.attachView(this);

		ButterKnife.bind(this, findViewById(R.id.body));

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

		loadDialog = new LoadingTopDialog(getActivity());
		loadDialog.setCancelable(false);
		loadDialog.setTitle("正在登录...");

		tv_forget_password.setText("忘记密码?");

		Animation anim=AnimationUtils.loadAnimation(getActivity(), R.anim.act_anim_login_frame);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		String username = Config.account.getUsername();
		String pwd = Config.account.getPassword();
		et_account.setText(username);
		et_password.setText(pwd);
	}

	@Override
	protected void onDestroy2() {
		loginPresener.detachView();
	}

	@Override
	protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

	}


	@OnClick(R.id.btn_login)
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
		loadDialog.show();
		//启动核心Service
		loginPresener.doLogin();
	}

	@OnClick(R.id.btn_regist)
	public void clickRegist(){
		RegistActivity.start(getActivity());
	}

	@OnClick(R.id.tv_forget_password)
	public void clickForgetPassword(){
		ForgetPasswordActivity.start(getActivity());
	}


	public void onLoginOk(){
		if(loadDialog.isShowing()){
			loadDialog.dismiss();
		}
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
		if(loadDialog.isShowing()){
			loadDialog.dismiss();
		}
		Toaster.toastShort(errorReason);
	}


	@Override
	public void onLoginOk(LoginOkEvent e) {

	}




	public static class LoginPresener extends AyoPresenter<LoginActivity> {


		public void doLogin(){
			Async.post(new Runnable() {
				@Override
				public void run() {
					if(getView() != null) getView().onLoginOk();
				}
			}, 2000);
		}

	}
}


