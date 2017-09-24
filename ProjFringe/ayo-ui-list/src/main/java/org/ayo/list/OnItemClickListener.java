package org.ayo.list;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mengxn on 2017/4/20.
 *
 * 这个是挺好，但不如直接给template设置onItem。。。，就不用在这里判断view type了，position也没什么问题了
 */
public abstract class OnItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private GestureDetector mGestureDetector;

    public OnItemClickListener() {

    }

    @Override
    public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(rv.getContext(), new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    final View childView = rv.findChildViewUnder(e.getX(), e.getY());
                    final int position = rv.getChildAdapterPosition(childView);
                    onItemClick(childView, position);
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    final View childView = rv.findChildViewUnder(e.getX(), e.getY());
                    final int position = rv.getChildAdapterPosition(childView);
                    onItemLongClick(childView, position);
                }
            });
        }
        return mGestureDetector.onTouchEvent(e);
    }

    public abstract void onItemClick(View view, int position);

    public void onItemLongClick(View view, int position) {

    }
}
