package org.ayo.notify.dialog.widget.base;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import org.ayo.animate.yoyo.YoYoAnimator;


public abstract class BottomBaseDialog<T extends BottomBaseDialog<T>> extends BottomTopBaseDialog<T> {
    public BottomBaseDialog(Context context, View animateView) {
        super(context);
        mAnimateView = animateView;

        mInnerShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0);

        mInnerDismissAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
    }

    public BottomBaseDialog(Context context) {
        this(context, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLlTop.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        mLlTop.setGravity(Gravity.BOTTOM);
        getWindow().setGravity(Gravity.BOTTOM);
        mLlTop.setPadding(mLeft, mTop, mRight, mBottom);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        showWithAnim();
    }

    @Override
    public void dismiss() {
        dismissWithAnim();
    }

    private YoYoAnimator mWindowInAs;
    private YoYoAnimator mWindowOutAs;

    @Override
    protected YoYoAnimator getWindowInAs() {
        if (mWindowInAs == null) {
            mWindowInAs = new WindowInAs();
        }
        return mWindowInAs;
    }

    @Override
    protected YoYoAnimator getWindowOutAs() {
        if (mWindowOutAs == null) {
            mWindowOutAs = new WindowOutAs();
        }
        return mWindowOutAs;
    }


    private class WindowInAs extends YoYoAnimator {
        @Override
        public void prepare() {
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mTarget, "scaleX", 1f, 0.9f);
            ObjectAnimator oa2 = ObjectAnimator.ofFloat(mTarget, "scaleY", 1f, 0.9f);
            mAnimatorSet.playTogether(oa1, oa2);
        }
    }

    private class WindowOutAs extends YoYoAnimator {
        @Override
        public void prepare() {
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mTarget, "scaleX", 0.9f, 1f);
            ObjectAnimator oa2 = ObjectAnimator.ofFloat(mTarget, "scaleY", 0.9f, 1f);
            mAnimatorSet.playTogether(oa1, oa2);
        }
    }
}
