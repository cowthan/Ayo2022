package org.ayo.fringe.ui;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ayo.animate.TweenAnimation;
import org.ayo.core.Async;
import org.ayo.fringe.utils.Initializer;
import org.ayo.fringe.R;
import org.ayo.notify.toaster.Toaster;
import org.ayo.fringe.utils.Permisstioner;
import org.ayo.fringe.App;
import org.ayo.fringe.initialize.StepOfAyoSdk;
import org.ayo.fringe.initialize.StepOfAyoView;
import org.ayo.fringe.initialize.StepOfCrash;
import org.ayo.fringe.initialize.StepOfHttp;
import org.ayo.fringe.initialize.StepOfImageLoader;
import org.ayo.fringe.ui.base.BaseFrgActivity;
import org.ayo.fringe.widget.BlackEmpireView;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * 闪屏页
 * @author Administrator
 *
 */
public class SplashActivity extends BaseFrgActivity {

	public static final int REQUEST_PERMISSION_STORAGE = 0x01;
	public static final int REQUEST_PERMISSION_CAMERA = 0x02;


	@BindView(R.id.iv_logo)
	SimpleDraweeView iv_logo;

	@Override
	protected int getLayoutId() {
		return R.layout.wb_ac_splash;
	}

	@Override
	protected void onCreate2(View contentView, @Nullable Bundle savedInstanceState) {
		UI.systembar(this, true);

		ButterKnife.bind(this);

		final BlackEmpireView bev = (BlackEmpireView) findViewById(R.id.bev);

		String uri = "http://7xicvb.com1.z0.glb.clouddn.com/girl_b_img-05ff9a30822bbd211590a8146903363f.jpg";
		//Flesco.setGalleryImageUri(iv_logo, uri, "file:///android_asset//splash_placeholder.jpg");
	}

	@Override
	protected void onDestroy2() {

	}

	@Override
	protected void onPageVisibleChanged(boolean visible, boolean isFirstTimeVisible, @Nullable Bundle savedInstanceState) {

	}

	private boolean isFirstCome = true;

	@Override
	protected void onStart() {
		super.onStart();
		if(isFirstCome && !App.isInitialed){
			initPermission();
			isFirstCome = false;
		}else{
			onLoadFinish();
		}
	}

	private void initPermission(){

		Permisstioner.newInstance()
				.with(this)
				.forPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.withSpeech("为了能正常运行，强烈建议您打开sd授权")
				.ask(new Permisstioner.Callback() {
					@Override
					public void onGranted(int i, String permission, boolean hasBeenGranted) {
						if(hasBeenGranted){
							initApp();
						}else{
							Permisstioner.openSettingActivity(getActivity(), "如果不授权，将无法继续运行");
						}
					}
				});

//		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//			if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//				initApp();
//			} else {
//				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_STORAGE);
//			}
//		}else{
//			initApp();
//		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_STORAGE) {
			if(grantResults != null && grantResults.length > 0){
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					///权限给了
					initApp();
				} else {
					Toaster.toastShort("权限被禁止，无法继续");
				}
			}


		}
	}



	private void initApp(){

		Initializer.initailizer()
				.addStep(new StepOfCrash())
				.addStep(new StepOfAyoView())
				.addStep(new StepOfAyoSdk())
				.addStep(new StepOfHttp())
				.addStep(new StepOfImageLoader())
				.setStepListener(new Initializer.StepListner() {
					@Override
					public boolean onSuffering(Initializer.Step step, boolean isSuccess, int currentStep, int total) {

						//统一判断
						if (!isSuccess && !step.acceptFail()) {
							//退出，提示错误
							Toaster.toastShort(step.getNotify());
							//finish();
							return false;
						}

						//单步逻辑
						if (step.getName().equals("UI Framework")) {
							//UI库加载完，才可以使用AyoViewLib
//							GifDrawable gif = null;
//							try {
//								gif = new GifDrawable(getResources(), R.drawable.gif_loading_fire);
//								iv_logo.setImageDrawable(gif);
//								gif.start();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
						}


						if(currentStep == total){
							App.isInitialed = true;
							onLoadFinish();
						}

						return true;
					}
				})
				.suffer();
	}

	private void onLoadFinish(){
		Async.post(new Runnable() {
			@Override
			public void run() {
//				GuideActivity.start(getAgent().getActivity());

				MainActivity.start(getActivity());
				TweenAnimation.apply(getActivity(), TweenAnimation.fade_in, TweenAnimation.fade_out);

				finish();
				TweenAnimation.apply(getAgent().getActivity(), TweenAnimation.fade_in, TweenAnimation.scale_out);

				//AyoWebViewFragment.start(getActivity(), "http://720yun.com/t/59e29qOgulg?pano_id=893339");
			}
		}, 3000);
	}
}
