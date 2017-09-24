package org.ayo.view.progress;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.ayo.view.R;


public class ProgressView extends ImageView{

	public ProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProgressView(Context context) {
		super(context);
		init();
	}
	private void init() {
		this.setImageResource(R.drawable.progress_animation_list);
		AnimationDrawable ad = (AnimationDrawable) this.getDrawable();
		ad.start();
	}
}
