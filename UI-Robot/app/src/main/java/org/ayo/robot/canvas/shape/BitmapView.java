package org.ayo.robot.canvas.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.lang.Bitmaps;
import org.ayo.robot.BaseView;
import org.ayo.robot.R;
import org.ayo.sample.menu.notify.ToasterDebug;

public class BitmapView extends BaseView {
    Bitmap mBitmap;

    public BitmapView(Context context) {
        super(context);
        init();
    }

    public BitmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BitmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BitmapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
    }

    public void changeBitmap(){
        currentPosition++;
        if(currentPosition == ids.length) currentPosition = 0;

        ToasterDebug.toastShort((currentPosition == 0 ? "jpg" : (currentPosition == 1 ? "png 1" : "png 2")));
        invalidate();
    }

    int[] ids = {
            R.raw.test2,
            R.mipmap.ic_launcher,
            R.raw.test3
    };
    int currentPosition = 0;

    @Override
    protected void drawShape(Canvas canvas, int w, int h, Paint paint) {

        int id = ids[currentPosition];
        int[] imageSize = Bitmaps.getImageSize(getContext(), id);
        mBitmap = Bitmaps.getBitmapByResource(getContext(), id, getMeasuredWidth(), getMeasuredHeight());
        Rect src = new Rect(0, 0, imageSize[0], imageSize[1]);
        RectF dest = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawBitmap(mBitmap, src, dest, paint);
        getObservable().notifyDataChanged(this, "图片尺寸(" + imageSize[0] + ", " + imageSize[1] + "), 控件尺寸(" + getMeasuredWidth() + ", " + getMeasuredHeight() + ")");

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getComment() {
        return null;
    }

    public int getCurrentShowingResourceId(){
        return ids[currentPosition];
    }


}