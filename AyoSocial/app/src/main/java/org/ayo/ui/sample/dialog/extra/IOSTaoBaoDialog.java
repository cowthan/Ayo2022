package org.ayo.ui.sample.dialog.extra;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import org.ayo.animate.yoyo.YoYoAnimator;
import org.ayo.notify.dialog.widget.base.BottomBaseDialog;
import org.ayo.sample.R;
import org.ayo.ui.sample.dialog.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IOSTaoBaoDialog extends BottomBaseDialog<IOSTaoBaoDialog> {
    @BindView(R.id.ll_wechat_friend_circle) LinearLayout mLlWechatFriendCircle;
    @BindView(R.id.ll_wechat_friend) LinearLayout mLlWechatFriend;
    @BindView(R.id.ll_qq) LinearLayout mLlQq;
    @BindView(R.id.ll_sms) LinearLayout mLlSms;

    public IOSTaoBaoDialog(Context context, View animateView) {
        super(context, animateView);
    }

    public IOSTaoBaoDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        View inflate = View.inflate(mContext, R.layout.dialog_ios_taobao, null);
        ButterKnife.bind(this, inflate);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mLlWechatFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, "朋友圈");
                dismiss();
            }
        });
        mLlWechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, "微信");
                dismiss();
            }
        });
        mLlQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, "QQ");
                dismiss();
            }
        });
        mLlSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, "短信");
                dismiss();
            }
        });
    }

    private YoYoAnimator mWindowInAs;
    private YoYoAnimator mWindowOutAs;

    @Override
    protected YoYoAnimator getWindowInAs() {
        if (mWindowInAs == null) {
            mWindowInAs = new WindowsInAs();
        }
        return mWindowInAs;
    }

    @Override
    protected YoYoAnimator getWindowOutAs() {
        if (mWindowOutAs == null) {
            mWindowOutAs = new WindowsOutAs();
        }
        return mWindowOutAs;
    }

    class WindowsInAs extends YoYoAnimator {
        @Override
        public void prepare() {
            ObjectAnimator rotationX = ObjectAnimator.ofFloat(mTarget, "rotationX", 10, 0f).setDuration(150);
            rotationX.setStartDelay(200);
            mAnimatorSet.playTogether(
                    ObjectAnimator.ofFloat(mTarget, "scaleX", 1.0f, 0.8f).setDuration(350),
                    ObjectAnimator.ofFloat(mTarget, "scaleY", 1.0f, 0.8f).setDuration(350),
//                    ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f).setDuration(350),
                    ObjectAnimator.ofFloat(mTarget, "rotationX", 0f, 10).setDuration(200),
                    rotationX,
                    ObjectAnimator.ofFloat(mTarget, "translationY", 0, -0.1f * mDisplayMetrics.heightPixels).setDuration(350)
            );
        }
    }

    class WindowsOutAs extends YoYoAnimator {
        @Override
        public void prepare() {
            ObjectAnimator rotationX = ObjectAnimator.ofFloat(mTarget, "rotationX", 10, 0f).setDuration(150);
            rotationX.setStartDelay(200);
            mAnimatorSet.playTogether(
                    ObjectAnimator.ofFloat(mTarget, "scaleX", 0.8f, 1.0f).setDuration(350),
                    ObjectAnimator.ofFloat(mTarget, "scaleY", 0.8f, 1.0f).setDuration(350),
//                    ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f).setDuration(350),
                    ObjectAnimator.ofFloat(mTarget, "rotationX", 0f, 10).setDuration(200),
                    rotationX,
                    ObjectAnimator.ofFloat(mTarget, "translationY", -0.1f * mDisplayMetrics.heightPixels, 0).setDuration(350)
            );
        }
    }
}
