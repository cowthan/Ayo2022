
package org.ayo.mall.view.xlistview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.ayo.mall.R;

/**
 * 重写一个新的ListViewHeader视图，以满足下拉刷新时的布局样式
 * <HR>
 * 作者: 孙博
 * <p/>
 * 时间: 2013年11月7日 上午10:57:07
 */
public class XListViewLoadingHeader extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    private final int ROTATE_ANIM_DURATION = 180;
    private int mState = STATE_NORMAL;
    private LinearLayout mContainer;

    private ImageView mProgressBar;

    public XListViewLoadingHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewLoadingHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.xlistview_loading_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mProgressBar = (ImageView)findViewById(R.id.xlistview_header_progressbar);

    }

    public void setState(int state) {
        if (state == mState)
            return;

        // if (state == STATE_REFRESHING) { // 显示进度
        // mProgressBar.setVisibility(View.VISIBLE);
        // } else { // 显示箭头图片
        // mProgressBar.setVisibility(View.INVISIBLE);
        // }

        switch (state) {
            case STATE_NORMAL:

                break;
            case STATE_READY:

                break;
            case STATE_REFRESHING:
                break;
            default:
        }
        mState = state;
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams)mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }
}
