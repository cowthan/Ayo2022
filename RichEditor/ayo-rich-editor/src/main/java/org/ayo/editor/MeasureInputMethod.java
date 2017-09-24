package org.ayo.editor;


import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.lang.ref.SoftReference;

public class MeasureInputMethod {

    // For more information, see
    // https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that
    // already has its content view set.

    private SoftReference<Activity> mRef;

    private View mChildOfContent;

    private int usableHeightPrevious;

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;

    private FrameLayout.LayoutParams frameLayoutParams;

    public MeasureInputMethod(Activity activity) {
        mRef = new SoftReference<>(activity);
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    public void addOnGlobalLayoutListener() {
        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (mRef == null || mRef.get() == null)
                    return;
                possiblyResizeChildOfContent();
            }
        };
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    public void removeOnGlobalLayoutListener() {
        if (mChildOfContent != null && mOnGlobalLayoutListener != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();// 可见高度
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();// 根节点的高度
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;// 输入法高度
            if (heightDifference > (usableHeightSansKeyboard / 3)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    frameLayoutParams.height = usableHeightSansKeyboard;
                else
                    frameLayoutParams.height = usableHeightSansKeyboard
                            - MyUtils.getStatusBarHeight(mRef.get());
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return (r.bottom - r.top + MyUtils.getStatusBarHeight(mRef.get()));
        }
        return (r.bottom - r.top);
    }

}