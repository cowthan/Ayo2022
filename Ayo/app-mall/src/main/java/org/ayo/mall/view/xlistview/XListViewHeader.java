
package org.ayo.mall.view.xlistview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ayo.mall.R;

/**
 * 重写一个新的ListViewHeader视图，以满足下拉刷新时的布局样式
 * <HR>
 * 作者: 孙博
 * <p/>
 * 时间: 2013年11月7日 上午10:57:07
 */
public class XListViewHeader extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    private final int ROTATE_ANIM_DURATION = 180;
    private int mState = STATE_NORMAL;
    private View mContainer;

    private View view1;

    private ImageView mArrowImageView;

    private ImageView mProgressBar;

    private TextView mHintTextView;

    private Animation mRotateUpAnim;

    private Animation mRotateDownAnim;

    public XListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        view1 = findViewById(R.id.xlistview_header_content);

        mArrowImageView = (ImageView)findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView)findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (ImageView)findViewById(R.id.xlistview_header_progressbar);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING) { // 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            view1.setVisibility(GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else { // 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            view1.setVisibility(VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY)
                    mArrowImageView.startAnimation(mRotateDownAnim);
                if (mState == STATE_REFRESHING)
                    mArrowImageView.clearAnimation();
                mHintTextView.setText(R.string.xlistview_header_hint_normal);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(R.string.xlistview_header_hint_ready);
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.xlistview_header_hint_loading);
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
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }
}
