
package org.ayo.editor.progress;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.ayo.editor.R;

/**
 * Created by Hujh on 15/8/20.
 */
public class ProgressImageView extends android.support.v7.widget.AppCompatImageView {
    private Animation animation;

    public ProgressImageView(Context context) {
        super(context);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rich_progress);
        if (getVisibility() == VISIBLE)
            setAnimation(animation);
        super.onFinishInflate();
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == VISIBLE) {
            startAnimation(animation);
        } else {
            clearAnimation();
        }
        super.setVisibility(visibility);
    }
}
