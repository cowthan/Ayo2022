package org.ayo.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AyoViewLib {
	
	public static Context context;
	
	public static void init(Context context){
		AyoViewLib.context = context;
		LocalDisplay.init(context);
		Display.init(context);
	}

	public static void setListViewHeight(ListView listView){
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight() + 30;
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 *
	 * v
	 *  w  -1 = wrap_content， -2 = match_parent，or px
	 * h  -1 = wrap_content， -2 = match_parent，or px
	 */
	public static void setViewSize(View v, int w, int h){
		if(v == null) return;
		int w2 = w;
		if(w == -1)  w2 = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
		if(w == -2)  w2 = ViewGroup.MarginLayoutParams.MATCH_PARENT;
		int h2 = h;
		if(h == -1)  h2 = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
		if(h == -2)  h2 = ViewGroup.MarginLayoutParams.MATCH_PARENT;
		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
		if(lp == null){
			lp = new ViewGroup.MarginLayoutParams(w2, h2);
		}else{
			lp.width = w2;
			lp.height = h2;
		}
		v.setLayoutParams(lp);
	}

	public static void showHtml(TextView tv, String html){

	}

	public static void showView(View v, int animId){

	}

	public static void hideView(View v, int animId){

	}

	/**
	 */
	public static void hideInputMethod(Activity activity, View v){
		if(v == null) v = activity.getCurrentFocus();
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 */
	public static void showInputMethod(Activity activity, View v){
		if(v == null) v = activity.getCurrentFocus();
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
	}

	/**
	 */
	public static void showInputMethod(View v){
		v.performClick();
	}

	/**
	 * @param activity
	 */
	public static void closeInputMethodBoard(Activity activity){
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	/**
	 * @param activity
	 */
	public static void showInputMethodBoard(Activity activity){
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	public static void toggleInputMethod(Activity activity){
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);


		if (imm.isActive()) {
		}
	}

	/**
	 * @param v
	 */
	public static void locationTheLast(EditText v){
		v.setSelection(v.getText().length());
	}
}
