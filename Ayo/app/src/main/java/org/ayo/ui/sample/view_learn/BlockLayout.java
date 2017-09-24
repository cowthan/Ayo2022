package org.ayo.ui.sample.view_learn;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 现在需要一个BlockLayout，其子控件可以是任何控件，但不论其宽度指定成什么，
 每一行都必须只能放3个子控件，而高度不论指定成什么，最后显示出来的都是个正方形
 并且，每一行的中间那个控件，必须距左右各10dp的margin
 行与行之间，也是10dp的margin

 这估计是个最简单的Layout了
 */

public class BlockLayout  extends ViewGroup {
    public BlockLayout(Context context) {
        super(context);
    }

    public BlockLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BlockLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private static final int FIXED_HORIZONTAL_MARIN_PX = 10;
    private static final int FIXED_VERTIVAL_MARIN_PX = 10;
    private int fixedChildWidth = 0;
    private int fixedChildHeight = 0;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int w = 0;
        int h = 0;
        if(widthSpecMode == MeasureSpec.EXACTLY){
            w = widthSpecSize;
        }else{
            w = widthSpecSize;
        }
        if(heightSpecMode == MeasureSpec.EXACTLY){
            h = heightSpecSize;
        }else{
            h = heightSpecSize;
        }
        int childCount = this.getChildCount();
        if(childCount == 0){
            setMeasuredDimension(w, h);
        }else{
            int borderLength = 0;
            int rowCount = childCount / 3 + (childCount%3 == 0 ? 0 : 1);
            int borderLengthHorizontal = (w - 2 * FIXED_HORIZONTAL_MARIN_PX) / 3;
            int borderLengthVertical = (h - (rowCount - 1) * FIXED_VERTIVAL_MARIN_PX) / rowCount;
            //取小的
            borderLength = Math.min(borderLengthHorizontal, borderLengthVertical);

            //重新规划w和h，去掉余数
            w = 3 * borderLength + 2 * FIXED_HORIZONTAL_MARIN_PX;
            h = rowCount * borderLength + (rowCount - 1) * FIXED_VERTIVAL_MARIN_PX;
            setMeasuredDimension(w, h);

            int childSpec = MeasureSpec.makeMeasureSpec(borderLength, MeasureSpec.EXACTLY);
            for(int i = 0; i < childCount; i++){
                View child = this.getChildAt(i);
                child.measure(childSpec, childSpec);
            }
        }
        //如果宽度是EXACTLY，直接setMeasuredDimension
        //如果宽度是AT_MOST，需要根据子控件来得到宽度，但这里这种情况，就取specSize当成EXACTLY来处理
        //如果高度是EXACTLY，

    }

    int currentTop = 0;
    int currentLeft = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = this.getChildCount();
        for(int i = 0; i < childCount; i++){
            final int pos = i;
            View child = this.getChildAt(i);
            if(i % 3 == 0){
                //每行的第一个
                if(i != 0){
                    //不是第一行，需要加个上margin
                    currentTop += FIXED_VERTIVAL_MARIN_PX;
                }
                child.layout(currentLeft, currentTop, currentLeft + child.getMeasuredWidth(), currentTop + child.getMeasuredHeight());
                currentLeft += child.getMeasuredWidth();
            }else if(i % 3 == 1){
                //每行的第二个
                currentLeft += FIXED_HORIZONTAL_MARIN_PX;
                child.layout(currentLeft, currentTop, currentLeft + child.getMeasuredWidth(), currentTop + child.getMeasuredHeight());
                currentLeft += child.getMeasuredWidth();
            }else{
                //每行的第三个
                currentLeft += FIXED_HORIZONTAL_MARIN_PX;
                child.layout(currentLeft, currentTop, currentLeft + child.getMeasuredWidth(), currentTop + child.getMeasuredHeight());
                currentLeft += child.getMeasuredWidth();
            }

            child.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "clicked " + pos, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
