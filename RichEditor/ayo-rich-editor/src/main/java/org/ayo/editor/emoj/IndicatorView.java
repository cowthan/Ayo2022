package org.ayo.editor.emoj;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.ayo.editor.R;


public class IndicatorView extends LinearLayout {

	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setCount(int n) {
		removeAllViews();
		for (int i = 0; i < n; i++) {
			ImageView imageView = new ImageView(getContext());
			RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
					30, 30);
			imageView.setImageResource(R.drawable.ic_action_attent);
			imageView.setScaleType(ScaleType.CENTER);
			addView(imageView, layoutParams);
		}
	}

	public void setSelect(int index) {
		int count = getChildCount();
		if (index < 0 || index >= count) {
			return;
		}
		ImageView imageView = null;
		for (int i = 0; i < count; i++) {
			imageView = (ImageView) getChildAt(i);
			if (i == index) {
				imageView.setImageResource(R.drawable.indicator_focus);
			} else {
				imageView.setImageResource(R.drawable.indicator_normal);
			}
		}
	}
}
