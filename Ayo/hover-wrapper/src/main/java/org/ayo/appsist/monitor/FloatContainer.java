package org.ayo.appsist.monitor;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Description: <悬浮窗>
 * Author: hui.zhao
 * Date: 2016/9/26
 * Copyright: Ctrip
 */
public class FloatContainer implements IFloatView {
    private static final String TAG = "FloatContainer";
    private WindowManager mWm;
    private WindowManager.LayoutParams mLp;
    private View mContentView;

    public FloatContainer(Context context) {
        mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLp = new WindowManager.LayoutParams();
    }

    @Override
    public void release() {
        if (mContentView != null && mContentView.getParent() != null) {
            mWm.removeView(mContentView);
        }
    }

    @Override
    public void attachToWindow(View view, int gravity, int x, int y, int width, int height) {
        if (view.getParent() != null) {
            return;
        }
        mLp.type = WindowManager.LayoutParams.TYPE_TOAST;
        mLp.format = PixelFormat.TRANSLUCENT;
        mLp.flags = WindowManager.LayoutParams
                .FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams
                .FLAG_NOT_TOUCHABLE;
        mLp.gravity = gravity;
        mLp.width = width == 0 ? WindowManager.LayoutParams.WRAP_CONTENT : width;
        mLp.height = height == 0 ? WindowManager.LayoutParams.WRAP_CONTENT : height;
        mLp.x = x;
        mLp.y = y;
        try {
            mContentView = view;
            mWm.addView(mContentView, mLp);
        } catch (Exception e) {
            Log.d(TAG, "悬浮窗添加失败:" + e.getLocalizedMessage());
        }
    }
}
