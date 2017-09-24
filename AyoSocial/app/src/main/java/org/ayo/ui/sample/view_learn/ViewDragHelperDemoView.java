package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.ayo.layout.swipeback.ViewDragHelper;
import org.ayo.notify.toaster.Toaster;

/**
 * Created by Administrator on 2016/11/28.
 */

public class ViewDragHelperDemoView extends ViewGroup {
    public ViewDragHelperDemoView(Context context) {
        super(context);
        init();
    }

    public ViewDragHelperDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewDragHelperDemoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewDragHelperDemoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    ViewDragHelper mViewDragHelper;

    private void init(){
        mViewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        //这里是一个常用模板，ViewDragHelper内部也是通过Scroller实现平滑滑动
        if(mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback(){

        /**
         * child是被触摸的View，这里你来告诉helper这个child是否可以开始检测滑动
         *
         * 一般方法是事先定好几个id，根据id判断是否可以滑动，如DrawerLayout
         int headerId= getResources().getIdentifier("sticky_header", "id", getContext().getPackageName());
         int contentId = getResources().getIdentifier("sticky_content", "id", getContext().getPackageName());
         表示header的id必须是R.id.sticky_header， 内容布局的id必须是R.id.sticky_content

         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            ///如果这个ViewGroup有两个子控件，menuView和contentView，则返回child == contentView意思就是只有contentView能拖动
            return true;
        }

        /**
         * 返回值表示The new clamped position for left，返回0表示不发生滑动
         * @param child Child view being dragged
         * @param left  水平方向上child移动的距离
         * @param dx    和前一次相比的增量
         * @return   一般只需要返回left，但如果需要更精确的计算padding等属性，则需要做一些处理
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Toaster.toastShort("手指松开了");
            mViewDragHelper.smoothSlideViewTo(releasedChild, 300, 500);
            ViewCompat.postInvalidateOnAnimation(ViewDragHelperDemoView.this);

        }
    };

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0; i < getChildCount(); i++){
            getChildAt(i).layout(0, 0, 200, 100);
        }
    }


}
