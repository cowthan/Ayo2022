
package org.ayo.editor.emoj;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ayo.editor.MyEmojiLoader;
import org.ayo.editor.R;
import org.ayo.editor.emoj.model.EmojiCategoryModel;
import org.ayo.editor.progress.ProgressImageView;

/**
 * Created by hujinghui on 16/6/21.
 */
public class ExpressionEmptyView extends LinearLayout implements View.OnClickListener {
    private TextView mTextView;

    private Button mButton;

    private ProgressImageView mProgressImageView;

    private EmojiCategoryModel mCategoryModel;

    public ExpressionEmptyView(Context context) {
        super(context);
    }

    public ExpressionEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpressionEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextView = (TextView)findViewById(R.id.empty_text_view);
        mButton = (Button)findViewById(R.id.resume_btn);
        mProgressImageView = (ProgressImageView)findViewById(R.id.empty_progress);
        mButton.setOnClickListener(this);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.resume_btn) {
            if (mCategoryModel != null)
                MyEmojiLoader.startResumeExpressions(getContext(), mCategoryModel.pkgId);
            mButton.setVisibility(GONE);
            mProgressImageView.setVisibility(VISIBLE);
            mTextView.setText("表情下载中，请稍后...");
        }
    }

    public void setData(EmojiCategoryModel model) {
        this.mCategoryModel = model;
    }
}
