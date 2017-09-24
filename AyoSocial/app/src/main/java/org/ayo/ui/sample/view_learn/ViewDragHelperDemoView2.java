package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import org.ayo.layout.swipeback.ViewDragHelper;
import org.ayo.notify.toaster.Toaster;

/**
 * Created by Administrator on 2016/11/28.
 */

public class ViewDragHelperDemoView2 extends FrameLayout {
    public ViewDragHelperDemoView2(Context context) {
        super(context);
        init();
    }

    public ViewDragHelperDemoView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewDragHelperDemoView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ViewDragHelperDemoView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            ViewCompat.postInvalidateOnAnimation(ViewDragHelperDemoView2.this);

        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            ///用户触摸到View后回调
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            //在拖曳时回调，状态有idle和dragging，idle就是松手了呗
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            ///控件位置改变了，常用于配合进行一些特效，如缩放
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            Log.i("drag", "onEdgeDragStarted--" + edgeFlags);
        }

        @Override
        public boolean onEdgeLock(int edgeFlags) {
            boolean res = super.onEdgeLock(edgeFlags);
            Log.i("drag", "onEdgeLock--" + res);
            return res;
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            Log.i("drag", "onEdgeTouched--" + edgeFlags);
        }


    };


}
