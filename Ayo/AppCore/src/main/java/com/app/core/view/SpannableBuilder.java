
package com.app.core.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.IconMarginSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/2/16 0016. 处理Spanner，稍微复杂点的文本样式可以直接用这个
 */

public class SpannableBuilder {

    private String src;

    private SpannableBuilder() {

    }

    public static SpannableBuilder from(String src) {
        SpannableBuilder sb = new SpannableBuilder();
        sb.src = src;
        return sb;
    }

    public Spannable build() {
        Spannable spn = new SpannableString(src);

        for (StyleInfo styleInfo : spans) {
            int start = src.indexOf(styleInfo.dest);
            if (start == -1) {

            } else {
                int end = start + styleInfo.dest.length();
                if (styleInfo.style instanceof ViewSpanRenderer) {
                    ImageSpan spanned = createImageSpan(styleInfo.dest,
                            (ViewSpanRenderer)styleInfo.style);
                    spn.setSpan(spanned, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    spn.setSpan(styleInfo.style, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
        }

        return spn;
    }

    private static class StyleInfo {
        String dest;

        Object style;

        StyleInfo(String dest, Object style) {
            this.dest = dest;
            this.style = style;
        }
    }

    private List<StyleInfo> spans = new ArrayList<>();

    public SpannableBuilder on(String dest, Object style) {
        spans.add(new StyleInfo(dest, style));
        return this;
    }

    public SpannableBuilder textSize(String dest, int size, boolean isDip) {
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, isDip);
        return on(dest, ass);
    }

    public SpannableBuilder textAlign(String dest, Layout.Alignment alignment) {
        AlignmentSpan.Standard s = new AlignmentSpan.Standard(alignment);
        return on(dest, s);
    }

    public SpannableBuilder background(String dest, int color) {
        BackgroundColorSpan s = new BackgroundColorSpan(color);
        return on(dest, s);
    }

    public SpannableBuilder textColor(String dest, int color) {
        ForegroundColorSpan s = new ForegroundColorSpan(color);
        return on(dest, s);
    }

    public SpannableBuilder strikethroughSpan(String dest) {
        StrikethroughSpan s = new StrikethroughSpan();
        return on(dest, s);
    }

    public SpannableBuilder strikethroughSpan(String dest, int color) {
        ColorableStrikethroughSpan s = new ColorableStrikethroughSpan();
        s.alpha(color);
        return on(dest, s);
    }

    public SpannableBuilder bullet(String dest, int gapWidth, int color) {
        BulletSpan s = new BulletSpan(gapWidth, color);
        return on(dest, s);
    }

    public SpannableBuilder drawable(String dest, Drawable drawable, int size) {
        DrawableMarginSpan s = new DrawableMarginSpan(drawable, size);
        return on(dest, s);
    }

    public SpannableBuilder icon(String dest, Bitmap drawable, int size) {
        IconMarginSpan s = new IconMarginSpan(drawable, size);
        return on(dest, s);
    }

    public SpannableBuilder leading(String dest, int lenFirstLine, int lenRemainLines) {
        LeadingMarginSpan.Standard s = new LeadingMarginSpan.Standard(lenFirstLine, lenRemainLines);
        return on(dest, s);
    }

    public SpannableBuilder firstLineLeading(String dest, TextView tv, int countOfa) {
        LeadingMarginSpan.Standard s = new LeadingMarginSpan.Standard(
                Math.round(getTextViewLength(tv, getRepeatString("a", countOfa))), 0);
        return on(dest, s);
    }

    public SpannableBuilder superscript(String dest) {
        SuperscriptSpan s = new SuperscriptSpan();
        return on(dest, s);
    }

    public SpannableBuilder alignTop(String dest) {
        AlignTop s = new AlignTop();
        return on(dest, s);
    }

    // 计算出该TextView中文字的长度(像素)
    public static float getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        float textLength = paint.measureText(text);
        return textLength;
    }

    private static String getRepeatString(String s, int repeatCount) {
        if (repeatCount == 0)
            return "";
        if (repeatCount == 1)
            return s;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repeatCount; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 让文字居上，对应的是TextView里的整个Spannable里最高的那部分的高度，就align这个高度
     * 这个类取自SuperscriptSpan，但SuperscriptSpan只是把文字提高一半高度（正常情况下上标需求可满足，但
     * Spannable高度不一致时，看起来肯定就不是上标）
     */
    public static class AlignTop extends MetricAffectingSpan implements ParcelableSpan {
        public AlignTop() {
        }

        public AlignTop(Parcel src) {
        }

        public int getSpanTypeId() {
            return getSpanTypeIdInternal();
        }

        /** @hide */
        public int getSpanTypeIdInternal() {
            return 14;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            writeToParcelInternal(dest, flags);
        }

        /** @hide */
        public void writeToParcelInternal(Parcel dest, int flags) {
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.baselineShift += (int)(tp.ascent());
        }

        @Override
        public void updateMeasureState(TextPaint tp) {
            tp.baselineShift += (int)(tp.ascent() / 2);
        }

        public static final Creator<AlignTop> CREATOR = new Creator<AlignTop>() {
            public AlignTop createFromParcel(Parcel source) {
                return new AlignTop(source);
            }

            public AlignTop[] newArray(int size) {
                return new AlignTop[size];
            }
        };
    }

    public static class ColorableStrikethroughSpan extends StrikethroughSpan
            implements UpdateAppearance, ParcelableSpan {
        public ColorableStrikethroughSpan() {
        }

        public ColorableStrikethroughSpan(Parcel src) {
            super(src);
            this.alpha = src.readInt();
        }

        private int alpha;

        public ColorableStrikethroughSpan alpha(int alpha) {
            this.alpha = alpha;
            return this;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.alpha);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setAlpha(alpha);
            ds.setStrikeThruText(true);
        }

        public static final Creator<ColorableStrikethroughSpan> CREATOR = new Creator<ColorableStrikethroughSpan>() {
            public ColorableStrikethroughSpan createFromParcel(Parcel source) {
                return new ColorableStrikethroughSpan(source);
            }

            public ColorableStrikethroughSpan[] newArray(int size) {
                return new ColorableStrikethroughSpan[size];
            }
        };
    }

    /// ------------------------------------------
    /// 用一个View来设置样式，然后将View转成drawable
    /// 然后将drawable 转为ImageSpan
    /// 解决一些比较复杂的文字效果
    /// 如添加背景span之后，文字左右间距无法设置的问题，太傻比了
    /// ------------------------------------------

    public interface ViewSpanRenderer {
        View getView(final String text);
    }

    public SpannableBuilder applyView(String dest, ViewSpanRenderer renderer) {
        return on(dest, renderer);
    }

    public static ImageSpan createImageSpan(String s, ViewSpanRenderer renderer) {
        View view = renderer.getView(s);
        BitmapDrawable bitmpaDrawable = (BitmapDrawable)convertViewToDrawable(view);
        bitmpaDrawable.setBounds(UPPER_LEFT_X, UPPER_LEFT_Y, bitmpaDrawable.getIntrinsicWidth(),
                bitmpaDrawable.getIntrinsicHeight());
        ImageSpan spn = new ImageSpan(bitmpaDrawable);
        return spn;
    }

    private final static int UPPER_LEFT_X = 0;

    private final static int UPPER_LEFT_Y = 0;

    public static Drawable convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(UPPER_LEFT_X, UPPER_LEFT_Y, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }

}
