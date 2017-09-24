package org.ayo.ui.sample.dialog.extra;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.ayo.animate.yoyo.Techniques;
import org.ayo.notify.dialog.utils.CornerUtils;
import org.ayo.notify.dialog.widget.base.BaseDialog;
import org.ayo.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomBaseDialog extends BaseDialog<CustomBaseDialog> {
    @BindView(R.id.tv_cancel) TextView mTvCancel;
    @BindView(R.id.tv_exit) TextView mTvExit;

    public CustomBaseDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(Techniques.Swing.getAnimator());

        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.dialog_custom_base, null);
        ButterKnife.bind(this, inflate);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
