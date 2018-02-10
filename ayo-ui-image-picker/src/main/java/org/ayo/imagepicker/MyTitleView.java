
package org.ayo.imagepicker;


import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyTitleView extends FrameLayout {

    TextView mTitle;

    TextView mSubTitle;

    ImageView mTitleImage;

    LinearLayout mLeftLayout;

    LinearLayout mRightLayout;

    /**
     * 排兵布阵-阵型
     */
    private LinearLayout mFormationLayout;

    /**
     * 排兵布阵-战术
     */
    private LinearLayout mTacticsLayout;
    private TextView mFormationTv, mTacticsTv;

    View mBottomLine;

    // @InjectView (R.id.view_titlebar_center_layout)
    // LinearLayout mCenterLayout;

    private TitleViewListener mTitleViewListener;

    private LayoutInflater layoutInflater;

    private int mPosition = 0;

    private Button btnNews;

    private Button btnMall;

    private Button btnGame;

    private ImageView ivNews;

    private ImageView ivMall;

    private ImageView ivGame;

    private ImageView ivSearch;

    private OnClickListener titleOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null)
                mTitleViewListener.onTitleClicked();
        }
    };

    private OnClickListener left1OnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null)
                mTitleViewListener.onLeft1Clicked();
        }
    };

    private OnClickListener leftOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null)
                mTitleViewListener.onLeftClicked();
        }
    };

    private OnClickListener rightOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mTitleViewListener != null)
                mTitleViewListener.onRightClicked();
        }
    };

    public MyTitleView(Context context) {
        super(context);
    }

    public MyTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = (TextView)findViewById(R.id.view_titlebar_title);
        mSubTitle = (TextView)findViewById(R.id.view_titlebar_sub_title);
        mTitleImage = (ImageView)findViewById(R.id.view_titlebar_title_img);
        mLeftLayout = (LinearLayout)findViewById(R.id.view_titlebar_left_layout);
        mRightLayout = (LinearLayout)findViewById(R.id.view_titlebar_right_layout);
        mBottomLine = findViewById(R.id.bottom_line);
        layoutInflater = LayoutInflater.from(getContext());
        super.onFinishInflate();
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setOnClickListener(titleOnClickListener);
            mTitleImage.setVisibility(View.INVISIBLE);
        } else {
            mTitle.setVisibility(View.GONE);
            mTitleImage.setVisibility(View.VISIBLE);
        }

        mSubTitle.setVisibility(View.GONE);
    }

    public void setTitle(String title, int textSize) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setOnClickListener(titleOnClickListener);
            mTitleImage.setVisibility(View.INVISIBLE);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP ,textSize);
        } else {
            mTitle.setVisibility(View.GONE);
            mTitleImage.setVisibility(View.VISIBLE);
        }

        mSubTitle.setVisibility(View.GONE);
    }

    public void setTitle(String title, int textSize, int textColor) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setOnClickListener(titleOnClickListener);
            mTitleImage.setVisibility(View.INVISIBLE);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(textSize));
            mTitle.setTextColor(getResources().getColor(textColor));
        } else {
            mTitle.setVisibility(View.GONE);
            mTitleImage.setVisibility(View.VISIBLE);
        }

        mSubTitle.setVisibility(View.GONE);
    }

    public void setTitle(String title, String subTitle) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
            if (!TextUtils.isEmpty(subTitle)) {
                mSubTitle.setText(subTitle);
                mSubTitle.setVisibility(View.VISIBLE);
            } else {
                mSubTitle.setVisibility(View.GONE);
            }
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setOnClickListener(titleOnClickListener);
            mTitleImage.setVisibility(View.INVISIBLE);
        } else {
            mTitle.setVisibility(View.GONE);
            mTitleImage.setVisibility(View.VISIBLE);
        }
    }

    public void setHtmlTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(Html.fromHtml(title));
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setOnClickListener(titleOnClickListener);
            mTitleImage.setVisibility(View.INVISIBLE);
        } else {
            mTitle.setVisibility(View.GONE);
            mTitleImage.setVisibility(View.VISIBLE);
        }
    }

    public void showTitleImage(boolean isShow) {
        mTitleImage.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    public void setLeftButton(int res) {
        mLeftLayout.removeAllViews();
        if (res != 0) {
            ImageView view = (ImageView)layoutInflater.inflate(
                    R.layout.picker_view_titlebar_right_img_btn, null);
            view.setImageResource(res);
            view.setOnClickListener(leftOnClickListener);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mLeftLayout.addView(view, params);
        }
    }


    public TextView getTitle() {
        return mTitle;
    }

    public void setRightButton(String text) {
        mRightLayout.removeAllViews();
        if (!TextUtils.isEmpty(text)) {
            TextView btn = (TextView)layoutInflater
                    .inflate(R.layout.picker_view_titlebar_right_text_btn, null);
            btn.setText(text);
            btn.setOnClickListener(rightOnClickListener);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mRightLayout.addView(btn, params);
        }
    }

    /**
     * 只针对相册
     * @param selected 是否选中
     */
    public void setRightButton(boolean selected) {
        mRightLayout.removeAllViews();
        ImageView btn = (ImageView) layoutInflater
                .inflate(R.layout.picker_view_titlebar_right_btn, null);
        btn.setOnClickListener(rightOnClickListener);
        btn.setSelected(selected);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mRightLayout.addView(btn, params);
    }


    public void setTitleViewListener(TitleViewListener listener) {
        mTitleViewListener = listener;
    }

    public interface TitleViewListener {
        void onLeftClicked();

        void onLeft1Clicked();

        void onRightClicked();

        void onRight1Clicked();

        void onTitleClicked();
    }


    public static class BaseTitleViewListener implements TitleViewListener {

        @Override
        public void onLeftClicked() {

        }

        @Override
        public void onLeft1Clicked() {

        }

        @Override
        public void onRightClicked() {

        }

        @Override
        public void onRight1Clicked() {

        }

        @Override
        public void onTitleClicked() {

        }

    }




}
