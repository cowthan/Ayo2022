package org.ayo.notify.popup;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import org.ayo.notify.LocalDisplay;
import org.ayo.notify.R;


/**
 */
public class PopupWindowHelper {

    private View popupView;
    private PopupWindow mPopupWindow;
    private Activity mActivity;
    private boolean enableBackUIOverlay = false;

    public static final int TYPE_WRAP_CONTENT = 0, TYPE_MATCH_PARENT = 1;

    public PopupWindowHelper(Activity activity, View view, boolean enableBackUIOverlay) {
        popupView = view;
        mActivity = activity;
        this.enableBackUIOverlay = enableBackUIOverlay;
    }

    public void showAsDropDown(View anchor) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        mPopupWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int type, int xoff, int yoff, int animStyle) {
        initPopupWindow(type);
        if(animStyle > 0) mPopupWindow.setAnimationStyle(animStyle);
        else mPopupWindow.setAnimationStyle(R.style.Ayo_Popup_AnimationUpPopup2);
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        mPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    public void dismiss() {
        if (mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }

    public void showAsPopUp(View anchor) {
        showAsPopUp(anchor, 0, 0);
    }

    public void showAsPopUp(View anchor, int xoff, int yoff) {
        initPopupWindow(TYPE_WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.Ayo_Popup_AnimationUpPopup);
        popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = popupView.getMeasuredHeight();
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, location[0] + xoff, location[1] - height + yoff);
    }

    public void showAsPopUp(View anchor, int gravity, int xoff, int yoff, int type, int animStyle) {
        initPopupWindow(type);
        if(animStyle > 0) mPopupWindow.setAnimationStyle(animStyle);
        else mPopupWindow.setAnimationStyle(R.style.Ayo_Popup_AnimationUpPopup2);
        popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = popupView.getMeasuredHeight();
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        mPopupWindow.showAtLocation(anchor, gravity, -location[0] + xoff , LocalDisplay.SCREEN_HEIGHT_PIXELS - location[1]);
    }

    public void showFromBottom(View anchor) {
        initPopupWindow(TYPE_MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.Ayo_Popup_AnimationFromButtom);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
    }

    public void showFromTop(View anchor) {
        initPopupWindow(TYPE_MATCH_PARENT);
        mPopupWindow.setAnimationStyle(R.style.Ayo_Popup_AnimationFromTop);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, getStatusBarHeight());
    }

    /**
     * touch outside dismiss the popupwindow, default is ture
     * @param isCancelable
     */
    public void setCancelable(boolean isCancelable) {
        if (isCancelable) {
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
        }else {
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setFocusable(false);
        }
    }

    public void initPopupWindow(int type) {
        if (type == TYPE_WRAP_CONTENT) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else if (type == TYPE_MATCH_PARENT) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        setCancelable(true);
        //mPopupWindow.setAnimationStyle(R.style.Popup_AnimationUpPopup2);

        //设置添加屏幕的背景透明度
        if(enableBackUIOverlay){
            final WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
            ValueAnimator anim = ValueAnimator.ofFloat(1.0f, 0.6f).setDuration(300);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.alpha = (Float) valueAnimator.getAnimatedValue();
                    mActivity.getWindow().setAttributes(lp);
                }
            });
            anim.start();

            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    final WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                    ValueAnimator anim = ValueAnimator.ofFloat(0.6f, 1.0f).setDuration(300);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            lp.alpha = (Float) valueAnimator.getAnimatedValue();
                            mActivity.getWindow().setAttributes(lp);
                        }
                    });
                    anim.start();
                }
            });
        }

    }

    private int getStatusBarHeight() {
        return Math.round(25 * Resources.getSystem().getDisplayMetrics().density);
    }
}
