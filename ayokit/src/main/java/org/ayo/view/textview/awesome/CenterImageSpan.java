package org.ayo.view.textview.awesome;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Created by qiaoliang on 2017/11/8.
 *
 * ImageSpan默认居下，这里需要居中
 */

public class CenterImageSpan extends ImageSpan {
    public CenterImageSpan(Drawable d) {
        super(d);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int transY = (y + fontMetricsInt.descent + y + fontMetricsInt.ascent) / 2 - drawable.getBounds().bottom/2;
        canvas.save();
        canvas.translate(x,transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
