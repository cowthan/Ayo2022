package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 支持横向滑动的ScrollView，用到的技术：
 * 1 解决滑动冲突
 * 2 Scroller实现平滑滑动
 * 3 滑动惯性
 *
 * 简化：
 * 1 假设所有子元素的宽/高都相等
 *
 */

public class FakeHorizontalScrollView extends ViewGroup{

    private static final String TAG = "HorizontalScrollView";

    public FakeHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public FakeHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FakeHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FakeHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;
    //上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    //上次滑动的坐标（onInterceptTouchEvent）
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private void init(){
        if(mScroller == null){
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = 0;
        int measureHeight = 0;
        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        ///这里简化了，不规范，如果子控件数为0，则宽高设为0，其实应该设为LayoutParam指定的值
        if(childCount == 0){
            setMeasuredDimension(0, 0);
        }else if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            final View childView = getChildAt(0);
            ///这里简化了，假设所有子元素宽度都相等，其实应该取宽度相加 + 所有margin + 自己的padding
            measureHeight = childView.getMeasuredHeight();
            ///这里简化了，假设所有子元素高度都相等，其实应该取（高度 + 上下margin + 自己的padding）中的最大
            measureWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measureWidth, measureHeight);
        }else if(heightSpecMode == MeasureSpec.AT_MOST){
            final View childView = getChildAt(0);
            measureHeight = childView.getMeasuredHeight();    ///简化了高度的计算
            setMeasuredDimension(widthSize, measureHeight);
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            final View childView = getChildAt(0);
            measureWidth = childView.getMeasuredWidth() * childCount;    ///简化了宽度的计算
            setMeasuredDimension(measureWidth, heightSize);
        }
    }

    /**
     * 所有子控件，横向排列
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        mChildrenSize = childCount;

        //简化了，既没考虑自己的padding，也没考虑子控件的margin
        for(int i = 0; i < childCount; i++){
            final View childView = getChildAt(i);
            if(childView.getVisibility() != View.GONE){
                final int childWidth = childView.getMeasuredWidth();
                mChildWidth = childWidth;
                childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int)ev.getX();
        int y = (int)ev.getY();

        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            intercepted = false;
            if(!mScroller.isFinished()){
                mScroller.abortAnimation();
                intercepted = true;
            }
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
            int dx = x - mLastXIntercept;
            int dy = y - mLastYIntercept;
            if(Math.abs(dx) > Math.abs(dy)){
                ///纵向滑动
                intercepted = true;
            }else{
                intercepted = false;
            }
        }else if(ev.getAction() == MotionEvent.ACTION_UP){
           intercepted = false;
        }
        Log.d(TAG, "intercepted = " + intercepted);
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int)event.getX();
        int y = (int)event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(!mScroller.isFinished()){
                mScroller.abortAnimation();
            }
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            int dx = x - mLastX;
            int dy = y - mLastY;
            scrollBy(-dx, 0);
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            int scrollX = getScrollX();
            int scrollToChildIndex = scrollX / mChildWidth;
            mVelocityTracker.computeCurrentVelocity(1000);
            float xVelocity = mVelocityTracker.getXVelocity();
            if(Math.abs(xVelocity) >= 50){
                ///滑动速度大于50时，如果是往右滑，速度大于0，往前翻； 如果是往左滑，速度小于0， 往后翻
                mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
            }else{
                ///滑动速度小于50时，根据当前scroll的距离，判断是翻回去，还是往后翻一页，总之最后肯定是停在一页上
                mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
            }
            //要停在的那一页，不能小于0，不能大于子控件数
            mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));

            //计算出从手抬起的位置滑到指定页的距离，然后平滑的划过去
            int dx = mChildIndex * mChildWidth - scrollX;
            smoothScollBy(dx, 0);
            mVelocityTracker.clear();

        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScollBy(int dx, int dy){
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
