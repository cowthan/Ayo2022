package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import org.ayo.view.Display;


/**
 * 仿ScrollView，类似SlidePanel
 * 一个子View向上滑动超过一定距离，松开手指后，就自动向上继续滑动，显示下一个子View
 *
 * 每一个子View的高度就是FakeScrollView的高度，一屏显示一个子View
 *
 * 用到的技术：
 * 1 measure和layout
 * 2 Scroller实现平滑滑动
 * 3 向上滑动到一定距离松手，则自动滚动到下一页
 */

public class FakeScrollView extends ViewGroup{
    public FakeScrollView(Context context) {
        super(context);
        init();
    }

    public FakeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FakeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FakeScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
        int count = getChildCount();
        lp.height = Display.screenHeight * count;
        setLayoutParams(lp);
        for(int i = 0; i < count; i++){
            View childView = getChildAt(i);
           if(childView.getVisibility() != View.GONE){
               childView.layout(l, i*Display.screenHeight, r, (i+1) * Display.screenHeight);
           }
        }
    }


    private int mLastY = 0;
    private int mStart = 0;
    private int mEnd = 0;
    private Scroller mScroller;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int)event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mLastY = y;
            mStart = getScrollY();
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            ///如果还在滚动，手放上之后，会停止滚动
            if(!mScroller.isFinished()){
                mScroller.abortAnimation();
            }
            int dy = mLastY - y;
            if(getScrollY() < 0){
                dy = 0;
            }
            if(getScrollY() > getHeight() - Display.screenHeight){
                dy = 0;
            }
            scrollBy(0, dy);
            mLastY = y;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            mEnd = getScrollY();
            int dScrollY = mEnd - mStart;
            if(dScrollY > 0){
                if(dScrollY < Display.screenHeight / 3){
                    mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                }else{
                    mScroller.startScroll(0, getScrollY(), 0, Display.screenHeight - dScrollY);
                }
            }else{
                if(-dScrollY < Display.screenHeight / 3){
                    mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                }else{
                    mScroller.startScroll(0, getScrollY(), 0, -Display.screenHeight - dScrollY);
                }
            }
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}
