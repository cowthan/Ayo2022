package com.app.core.anim;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import org.ayo.core.Lang;

/**
 * Created by qiaoliang on 2017/9/19.
 *
 * 控制控件显示和隐藏的动画，以及状态管理
 */

public class VisibilityAnimatorWrapper {

    private ValueAnimator mAnimationIn;

    private ValueAnimator mAnimationOut;

    private boolean mIsMovingToShow = false;

    private boolean mIsMovingToHide = false;

    private boolean mIsShown = true;

    private View mView;

    private Activity mActivity;

    private ObjectAnimator createAlphaAnimator(boolean isIn) {
        if (isIn) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mView, "alpha", 0, 1);
            animator.setDuration(400);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsMovingToShow = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsMovingToShow = false;
                    mIsShown = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mIsMovingToShow = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            return animator;
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mView, "alpha", 1, 0);
            animator.setDuration(400);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsMovingToHide = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsMovingToHide = false;
                    mIsShown = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mIsMovingToHide = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            return animator;
        }
    }

    /**
     * 商城首页搜索框上下滑动时的效果，但还是会占有高度
     * @param isIn
     * @return
     */
    private ObjectAnimator createTranslationYAnimator(boolean isIn) {
        int viewHeight = Lang.dip2px(44);
        if (isIn) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mView, "translationY", -viewHeight,
                    0);
            animator.setDuration(400);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsMovingToShow = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsMovingToShow = false;
                    mIsShown = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mIsMovingToShow = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            return animator;
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mView, "translationY", 0,
                    -viewHeight);
            animator.setDuration(400);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsMovingToHide = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsMovingToHide = false;
                    mIsShown = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mIsMovingToHide = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            return animator;
        }
    }

    private ValueAnimator createTranslationYAnimator2(boolean isIn) {
        int viewHeight = Lang.dip2px(44);
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
        if (isIn) {
            ValueAnimator animator = ValueAnimator.ofInt(-viewHeight, 0);
            animator.setDuration(400);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int marginTop = (int) valueAnimator.getAnimatedValue();
                    lp.topMargin = marginTop;
                    mView.setLayoutParams(lp);
                    mView.requestLayout();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsMovingToShow = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsMovingToShow = false;
                    mIsShown = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mIsMovingToShow = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            return animator;
        } else {
            ValueAnimator animator = ValueAnimator.ofInt(0, -viewHeight);
            animator.setDuration(400);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int marginTop = (int) valueAnimator.getAnimatedValue();
                    lp.topMargin = marginTop;
                    mView.setLayoutParams(lp);
                    mView.requestLayout();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsMovingToHide = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsMovingToHide = false;
                    mIsShown = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mIsMovingToHide = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            return animator;
        }
    }

    public VisibilityAnimatorWrapper(Activity activity, View target) {
        mActivity = activity;
        mView = target;
        mAnimationIn = createAlphaAnimator(true);
        mAnimationOut = createAlphaAnimator(false);
    }

    public void show() {
        if (mIsMovingToShow)
            return;
        if (mIsMovingToHide) {
            mAnimationOut.cancel();
        }
        if (mIsShown)
            return;
        mAnimationIn.start();
    }

    public void hide() {
        if (mIsMovingToHide)
            return;
        if (mIsMovingToShow) {
            mAnimationIn.cancel();
        }
        if (!mIsShown)
            return;
        mAnimationOut.start();
    }

}
