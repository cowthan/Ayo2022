package org.ayo.fringe.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.ayo.view.AyoViewLib;

import java.util.List;

/**
 * Created by Administrator on 2017/1/15.
 */

public class NineGridLayout extends FrameLayout {
    public NineGridLayout(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NineGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private static final int INDEX_TAG = 0x04 << 24;

    private BaseNineGridAdapter<?> mBlockListAdapter;

    private LayoutInflater mLayoutInflater;

    private OnItemClickListener mOnItemClickListener;

    private int sepHorizontal = 16; //间隔，包括左右，上下，网格之间
    private int sepVertical = 16;
    int gridWidth = 0;
    int gridHeight = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }



    public void setAdapter(final BaseNineGridAdapter<?> adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("adapter should not be null");
        }
        mBlockListAdapter = adapter;
        adapter.registerView(this);


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mBlockListAdapter) {
            mBlockListAdapter.registerView(null);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null != mBlockListAdapter) {
            mBlockListAdapter.registerView(this);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int index = (Integer) v.getTag(INDEX_TAG);
            if (null != mOnItemClickListener) {
                mOnItemClickListener.onItemClick(v, index);
            }
        }
    };

    public void onDataListChange(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                onDataListChange11();
            }
        }, 10);
    }

    private void onDataListChange11() {

        removeAllViews();
        if(mBlockListAdapter.getCount() == 0) return;

        sepHorizontal = mBlockListAdapter.getHorizontalSpacing();
        sepVertical = mBlockListAdapter.getVerticalSpacing();
        calculateCellSize();
        AyoViewLib.setViewSize(this, tw, th);

        int len = mBlockListAdapter.getCount();
        int w = gridWidth; //mBlockListAdapter.getBlockWidth();
        int h = gridHeight; //mBlockListAdapter.getBlockHeight();
        int columnNum = mBlockListAdapter.getCloumnNum();

        int horizontalSpacing = mBlockListAdapter.getHorizontalSpacing();
        int verticalSpacing = mBlockListAdapter.getVerticalSpacing();

        boolean blockDescendant = getDescendantFocusability() == ViewGroup.FOCUS_BLOCK_DESCENDANTS;

        for (int i = 0; i < len; i++) {

            FrameLayout.LayoutParams lyp = new FrameLayout.LayoutParams(w, h);
            int row = i / columnNum;
            int clo = i % columnNum;
            int left = 0;
            int top = 0;

            if (clo > 0) {
                left = (horizontalSpacing + w) * clo;
            }
            if (row > 0) {
                top = (verticalSpacing + h) * row;
            }
            lyp.setMargins(left, top, 0, 0);
            View view = mBlockListAdapter.getView(mLayoutInflater, i);
            if (!blockDescendant) {
                view.setOnClickListener(mOnClickListener);
            }
            view.setTag(INDEX_TAG, i);
            addView(view, lyp);
        }
        requestLayout();
    }

    int tw = 0;
    int th = 0;

    private void calculateCellSize(){
        int count = mBlockListAdapter.getCount();
        if(count == 0) return;
        tw = getWidth();
        th = 0;

        int w = 0;
        int h = 0;
        ///w根据本身宽度和图片数决定
        ///图片始终是宽=高
        ///所以这个布局的高度只知道wrap_content，其他方式指定的height都无效
        if(count == 1){
            w = tw - 2 * sepHorizontal;
            h = w;
            th = h + 2 * sepVertical;
        }else if(count == 2){
            w = (tw - 3 * sepHorizontal) / 2;
            h = w;
            th = h + 2 * sepVertical;
        }else if(count == 3){
            w = (tw - 4 * sepHorizontal) / 3;
            h = w;
            th = h + 2 * sepVertical;
        }else if(count <= 6){
            w = (tw - 4 * sepHorizontal) / 3;
            h = w;
            th = 2*h + 3 * sepVertical;
        }else if(count <= 9){
            w = (tw - 4 * sepHorizontal) / 3;
            h = w;
            th = 3*h + 4 * sepVertical;
        }

        gridWidth = w;
        gridHeight = h;
//        AyoViewLib.setViewSize(this, tw, th);
        Log.e("ddddeeddff", "------------" + count + "--" + tw + ", " + th + ", " + w + ", " + h);

//        int l = sepHorizontal;
//        int t = sepVertical;
//        int r = l + gridWidth;
//        int b = t + gridHeight;
//        for(int i = 0; i < count; i++){
//            View childView = getChildAt(i); //adapter.getView(i, null, this);
//            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) childView.getLayoutParams(); //new FrameLayout.LayoutParams(w, h);
//            lp.leftMargin = l;
//            lp.topMargin = t;
//            lp.width = w;
//            lp.height = h;
//            childView.setLayoutParams(lp);
//            Log.e("ddddeeddff", l + ", " + r + ", " + t + ", " + b);
//            //this.addView(childView, lp);
//            if(i == 2 || i == 5 || i == 8){
//                //换行
//                l = sepHorizontal;
//                r = l + gridWidth;
//                t = b + sepVertical;
//                b = t + gridHeight;
//            }else{
//                //换列
//                l = r + sepHorizontal;
//                r = l + gridWidth;
//            }
//
//        }

    }

    public static abstract class BaseNineGridAdapter<T> {

        private List<T> mItemList;
        private NineGridLayout mView;

        // default size is wrap_content
        private int mBlockWidth = -2;
        private int mBlockHeight = -2;

        private int mWidthSpace = 0;
        private int mHeightSpace = 0;

        private int mColumnNum = 0;

        public BaseNineGridAdapter() {
        }

        public T getItem(int position) {
            return mItemList.get(position);
        }

        public void registerView(NineGridLayout observer) {
            mView = observer;
        }

        public void notifyDataSetChanged(List<T> itemList) {
            if (null == itemList) {
                return;
            }
            mItemList = itemList;

            if (null == mView) {
                throw new IllegalArgumentException("Adapter has not been attached to any BlockListView");
            }
            mView.onDataListChange();
        }

        public abstract View getView(LayoutInflater layoutInflater, int position);

        public int getCount() {
            return mItemList == null ? 0 : mItemList.size();
        }

        public void setSpace(int w, int h) {
            mWidthSpace = w;
            mHeightSpace = h;
        }

        public int getHorizontalSpacing() {
            return mWidthSpace;
        }

        public int getVerticalSpacing() {
            return mHeightSpace;
        }

        public void setBlockSize(int w, int h) {
            mBlockWidth = w;
            mBlockHeight = h;
        }

        public int getBlockWidth() {
            return mBlockWidth;
        }

        public int getBlockHeight() {
            return mBlockHeight;
        }

        public void setColumnNum(int num) {
            mColumnNum = num;
        }

        public int getCloumnNum() {
            return mColumnNum;
        }
    }
}
