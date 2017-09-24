package com.worse.more.breaker.ui.account;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.worse.more.breaker.R;
import com.worse.more.breaker.ui.BasePage;
import com.worse.more.breaker.ui.contact.RegistContact;

import org.ayo.component.Master;
import org.ayo.component.core.SystemBarTintManager;
import org.ayo.core.Async;
import org.ayo.core.Lang;
import org.ayo.core.Strings;
import org.ayo.notify.loading.LoadingTopDialog;
import org.ayo.notify.toaster.Toaster;
import org.ayo.view.widget.TitleBar;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class RegistActivity extends BasePage implements RegistContact.View{
	
	public static void start(Activity a){
		Master.startPage(a, RegistActivity.class, null);
	}

	@Bind(R.id.et_phone)
	EditText et_phone;

	@Bind(R.id.tv_very_code)
	TextView tv_very_code;

	@Bind(R.id.et_new_pwd)
	EditText et_new_pwd;

	@Bind(R.id.et_code)
	EditText et_code;

	@Bind(R.id.iv_eye)
	ImageView iv_eye;

	@Bind(R.id.cb_protocol)
	CheckBox cb_protocol;

	@Bind(R.id.titlebar)
	TitleBar titlebar;

	@Bind(R.id.tv_submit_new_pwd)
	TextView tv_submit_new_pwd;


	Timer timer;
	LoadingTopDialog loadingDlg;
	private RegistPresenter presenter;

	private Handler handler;
	private boolean passwordHide = true;

	@Override
	protected int getLayoutId() {
		return R.layout.act_ac_regist;
	}

	@Override
	protected void onCreate2(View view, @Nullable Bundle bundle) {
		ButterKnife.bind(this, findViewById(R.id.body));

		presenter = new RegistPresenter();
		presenter.attachView(this);

		SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setStatusBarTintColor(Lang.rcolor(R.color.theme_color));
		tintManager.setNavigationBarTintColor(Lang.rcolor(R.color.theme_color));

		loadingDlg = new LoadingTopDialog(getActivity());
		loadingDlg.setCancelable(false);
		loadingDlg.setTitle("正在注册...");

		titlebar.title("注册")
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

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					if (msg.what > 0) {
						tv_very_code.setText(msg.what+"秒");
					} else {
						tv_very_code.setText("获取验证码");
						tv_very_code.setClickable(true);
						tv_very_code.setBackgroundColor(Lang.rcolor(R.color.theme_color));

						// 结束Timer计时器
						timer.cancel();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
	}

	@Override
	protected void onDestroy2() {
		presenter.detachView();
	}

	@Override
	protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

	}


	@OnClick(R.id.iv_eye)
	public void clickEye(){
		if(passwordHide){
			iv_eye.setSelected(true);
			//如果选中，显示密码
			et_new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			passwordHide = false;
		}else{
			iv_eye.setSelected(false);
			//否则隐藏密码
			et_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
			passwordHide = true;
		}
	}

	@OnCheckedChanged(R.id.cb_protocol)
	public void protocolCheckChanged(CompoundButton arg0, boolean arg1){
		if(arg1){
			tv_submit_new_pwd.setEnabled(true);
		}else{
			tv_submit_new_pwd.setEnabled(false);
		}
	}

	@OnClick(R.id.tv_protocol)
	public void clickProtocol(){
		Toaster.toastShort("协议");
	}

	@OnClick(R.id.tv_submit_new_pwd)
	public void clickSubmit(){
		final String phone = et_phone.getText().toString().trim();
		final String newPwd = et_new_pwd.getText().toString().trim();
		String code = et_code.getText().toString().trim();

		if (isInputAvalid(phone, newPwd, code)) {

			loadingDlg.show();
			presenter.doSubmit();

		}
	}

	@OnClick(R.id.tv_very_code)
	public void clickVeryCode(){

		final String phone = et_phone.getText().toString().trim();

		if (!Strings.isMobile(phone)) {
			Toaster.toastShort("请输入正确的手机号");
			return;
		}
		//SBToast.toastShort(agent.getActivity(), "获取验证码");

		presenter.doGetCode();

		timer = new Timer(true);

		TimerTask timerTask = new TimerTask() {
			int i = 60;

			@Override
			public void run() {
				Message msg = new Message();
				msg.what = i--;
				handler.sendMessage(msg);
			}
		};

		timer.schedule(timerTask, 0, 1000); // 延时0ms后执行，1000ms执行一次
		tv_very_code.setClickable(false);
		tv_very_code.setBackgroundColor(Color.parseColor("#696969"));
	}

	public boolean isInputAvalid(String phone, String newPwd, String code) {

		if(!Strings.isMobile(phone)){
			Toaster.toastShort("请输入11位手机号");
			return false;
		}
		if(!isPwdSuitable(newPwd)){
			Toaster.toastShort("请输入密码，长度6到16位");
			return false;
		}
		if(!isVerifyCodeSuitable(code)){
			Toaster.toastShort("请输入验证码");
			return false;
		}

		return true;
	}


	public boolean isPwdSuitable(String pwd) {
		if (pwd == null || "".equals(pwd)) {
			Toaster.toastShort("密码不能为空");
			return false;
		}

		if (pwd.length() < 6 || pwd.length() > 16) {
			Toaster.toastShort("请输入6-16位数字、字母");
			return false;
		}
		
		if(!Strings.isName(pwd)){
			Toaster.toastShort("只能输入数字和字母");
			return false;
		}

		return true;
	}

	public boolean isVerifyCodeSuitable(String nickname) {

		if (nickname == null || "".equals(nickname)) {
			Toaster.toastShort("请输入验证码");
			return false;
		}
		return true;
	}

	public void onVeryCodeClickedOk(String code){
		Toaster.toastShort("请等待接收短信...");
	}

	public void onVeruyCodeClickedFail(String errorReason){
		Toaster.toastShort(errorReason);
		timer.cancel();
		tv_very_code.setText("验证码");
		tv_very_code.setEnabled(true);
	}

	public void onSubmitOk(){
		loadingDlg.dismiss();
		Toaster.toastLong("注册成功...");
		finish();
	}

	public void onSubmitFail(String errorReason){
		loadingDlg.dismiss();
		Toaster.toastShort(errorReason);
	}



	public static class RegistPresenter extends RegistContact.Presenter{


		public void doGetCode(){
			Async.post(new Runnable() {
				@Override
				public void run() {
					if(getView() != null) getView().onVeryCodeClickedOk("234543");
				}
			}, 2000);
		}

		public void doSubmit(){
			Async.post(new Runnable() {
				@Override
				public void run() {
					if(getView() != null) getView().onSubmitOk();
				}
			}, 2000);
		}
	}

}
