
package org.ayo.editor.emoj;


import org.ayo.editor.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lishuai on 15/11/15.
 */
public class ExpressionTabView extends LinearLayout implements View.OnClickListener {
    private Context mContext;

    private ImageView mIvState, imageTopLine;

    private TextView mTvName;

    private OnClickListener mListener;

    public ExpressionTabView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        setBackgroundResource(R.drawable.expression_selector);
        LayoutInflater.from(mContext).inflate(R.layout.layout_expression_tab, this, true);
        // 方向垂直
        setOrientation(HORIZONTAL);
        mIvState = (ImageView)findViewById(R.id.iv_state);
        imageTopLine = (ImageView)findViewById(R.id.image_top_line);
        mTvName = (TextView)findViewById(R.id.tv_name);
        setOnClickListener(this);
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            LinearLayout ll = (LinearLayout)getParent();
            for (int i = 0; i < ll.getChildCount(); i++) {
                if (ll.getChildAt(i) != this)
                    ll.getChildAt(i).setSelected(false);
            }
        }
        if (null != mListener) {

            if (selected) {
                imageTopLine.setVisibility(View.VISIBLE);
            } else {
                imageTopLine.setVisibility(View.GONE);
            }
        }
        super.setSelected(selected);
    }

    public ImageView getImageView() {
        return mIvState;
    }

    public TextView getTextView() {
        return mTvName;
    }

    @Override
    public void onClick(View v) {
        setSelected(true);
        mListener.onclick();
    }

    interface OnClickListener {
        void onclick();

    }

    public void setOnTabClickListener(OnClickListener listener) {
        this.mListener = listener;
    }

}
