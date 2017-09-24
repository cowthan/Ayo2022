package org.ayo.robot.paint.xfermode;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import org.ayo.robot.BaseView;

/**
 * Created by Administrator on 2016/12/29.
 */

public abstract class BaseProterDuffView extends BaseView {


    public BaseProterDuffView(Context context) {
        super(context);
    }

    public BaseProterDuffView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseProterDuffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseProterDuffView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static final int USE_PORTER_DUFF_COLOR_FILTER = 1;
    public static final int USE_PORTER_DUFF_XFERMODE = 2;
    public int use = USE_PORTER_DUFF_XFERMODE;

    public String changeTool(){
        if(use == USE_PORTER_DUFF_XFERMODE){
            use = USE_PORTER_DUFF_COLOR_FILTER;
            invalidate();
            return "PorterDuffColorFilter";
        }else{
            use = USE_PORTER_DUFF_XFERMODE;
            invalidate();
            return "PorterDuffXfermode";
        }


    }

}
