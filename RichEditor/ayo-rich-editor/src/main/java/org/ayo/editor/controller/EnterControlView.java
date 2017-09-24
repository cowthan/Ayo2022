
package org.ayo.editor.controller;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.ayo.editor.MyToaster;
import org.ayo.editor.R;
import org.ayo.editor.anim.AnimationUtil;
import org.ayo.editor.emoj.engine.EmojiUtils;
import org.ayo.editor.emoj.engine.ExpressionParser;
import org.ayo.editor.emoj.ExpressionSelectView;
import org.ayo.editor.emoj.model.EmojiModel;

public class EnterControlView extends LinearLayout implements View.OnClickListener,
        ExpressionSelectView.OnExpressionClickListener {
    private ExpressionSelectView mExpressionSelectView;

    private LinearLayout mToolTypeView;

    private ImageView mExpressionImageView;

    private EnterToolsViewListener mListener;

    private boolean isExpressionEnable = false;

    public EnterControlView(Context context) {
        this(context, null);
        View.inflate(getContext(), R.layout.view_enter_tools, this);
    }

    public EnterControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        View.inflate(getContext(), R.layout.view_enter_tools, this);
    }

    public EnterControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.view_enter_tools, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setupView();
    }

    private void setupView() {
        mExpressionSelectView = (ExpressionSelectView)findViewById(R.id.expression_view);
        mExpressionSelectView.setOnExpressionClickListener(this);
        mToolTypeView = (LinearLayout)findViewById(R.id.tool_types);
        mExpressionImageView = (ImageView)addExpressionToolType();
    }

    public void hideExpression() {
        if (mExpressionImageView != null) mToolTypeView.removeView(mExpressionImageView);
    }

    public View addToolType(View view) {
        mToolTypeView.addView(view);
        return view;
    }

    private View addExpressionToolType() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_enter_tool_expression,
                null);
        view.setOnClickListener(this);
        return addToolType(view);
    }

    public View addVideoToolType() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_enter_tool_video, null);
        view.setOnClickListener(this);
        return addToolType(view);
    }

    public View addLinkToolType() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_enter_tool_link, null);
        view.setOnClickListener(this);
        return addToolType(view);
    }

    public View addPhotoToolType() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_enter_tool_photo, null);
        view.setOnClickListener(this);
        return addToolType(view);
    }

    public View addCameraToolType() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_enter_tool_camera, null);
        view.setOnClickListener(this);
        return addToolType(view);
    }

    public View addBattleToolType() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_enter_tool_battle, null);
        view.setOnClickListener(this);
        return addToolType(view);
    }

    public View addAtToolType() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_enter_tool_at, null);
        view.setOnClickListener(this);
        return addToolType(view);
    }

    public void showExpressionImageView(boolean show) {
        mExpressionImageView.setVisibility(show ? VISIBLE : GONE);
    }

    public void showExpressionSelectView(boolean show) {
        if (!show) {
            hideExpressionSelect();
        } else {
            showExpressionSelect();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.item_enter_tool_expression){
            if (mListener == null)
                return;
            if (isExpressionEnable) {
                clickExpression();
            } else {
                if (EmojiUtils.isStoreEmojiEnable()) {
                    MyToaster.toastShort(getResources().getString(R.string.expression_download_failed));
                } else {
                    MyToaster.toastShort(getResources().getString(R.string.expression_download_failed_no_engouth_space));
                }
            }
        }
    }

    @Override
    public void OnExpressionClick(EmojiModel model) {
        setEmojiCommentCode(model.alias, 0);
    }

    @Override
    public void OnExpressionClickDelete() {
        EditText contentEditText = mListener.getEnterEditTextView();
        int selectionStart = contentEditText.getSelectionStart();// 获取光标的位置
        if (selectionStart > 0) {
            String body = contentEditText.getText().toString();
            if (!TextUtils.isEmpty(body)) {
                String tempStr = body.substring(0, selectionStart);
                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                int j = tempStr.lastIndexOf("]");// 获取最后一个表情的位置
                if (i != -1) {
                    if (j < tempStr.length() - 1) {
                        contentEditText.getEditableText().delete(tempStr.length() - 1,
                                selectionStart);
                        return;
                    }
                    contentEditText.getEditableText().delete(i, selectionStart);
                    return;
                }
                contentEditText.getEditableText().delete(tempStr.length() - 1, selectionStart);
            }
        }
    }

    public void initExpression() {
        if (mExpressionSelectView.initExpressionPackages()) {
            mExpressionImageView.setVisibility(View.VISIBLE);
            if (mListener != null
                    && !TextUtils.isEmpty(mListener.getEnterEditTextView().getText().toString())) {
                setEmojiCommentCode(mListener.getEnterEditTextView().getText().toString(), 1);
            }
            isExpressionEnable = true;
        } else {
            isExpressionEnable = false;
        }
    }

    private void clickExpression() {
        if (mExpressionSelectView.getVisibility() == View.VISIBLE) {
            hideExpressionSelect();
        } else {
            showExpressionSelect();
        }
    }

    public void hideExpressionSelect() {
        mExpressionImageView.setImageResource(R.drawable.icon_expression);
        AnimationUtil.closeAction(mExpressionSelectView, mExpressionSelectView.getHeight());
        mExpressionSelectView.setVisibility(View.GONE);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                EmojiUtils.showSoftInput(getContext(), mListener.getEnterEditTextView());
            }
        }, 200);
        if (mListener != null)
            mListener.onSlide(true);
    }

    public void showExpressionSelect() {
        mExpressionImageView.setImageResource(R.drawable.icon_keyboard);
        EmojiUtils.hideSoftInput(getContext(), mListener.getEnterEditTextView());
        if (mListener != null)
            mListener.onSlide(false);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationUtil.showAction(mExpressionSelectView, mExpressionSelectView.getHeight());
                mExpressionSelectView.setVisibility(View.VISIBLE);
            }
        }, 200);

    }

    /**
     * @param str
     * @param flag 0为添加表情,
     */
    private void setEmojiCommentCode(String str, int flag) {
        EditText contentEditText = mListener.getEnterEditTextView();
        boolean richExpressionTotal = mListener.getExpressiongTotal();
        String oriContent = contentEditText.getText().toString();
        if ((!richExpressionTotal
                || !ExpressionParser.getInstance().getExpressionTotal(oriContent))
                && flag == 0) {
            MyToaster.toastShort(getContext().getString(R.string.expression_total_max, 60+""));
        } else {
            String insertEmotion = str;
            int index = 0;
            StringBuilder sBuilder;
            if (flag == 0) {
                sBuilder = new StringBuilder(oriContent);
                index = Math.max(contentEditText.getSelectionStart(), 0);
            } else {
                sBuilder = new StringBuilder();
            }

            sBuilder.insert(index, insertEmotion);
            contentEditText.setText(EmojiUtils.replaceEmojiTextView(sBuilder.toString(),
                    (int)contentEditText.getTextSize()));
            contentEditText.setSelection(index + insertEmotion.length());
        }

    }

    public void setEnterToolsViewListener(EnterToolsViewListener listener) {
        mListener = listener;
    }

    public interface EnterToolsViewListener {
        void onSlide(boolean out);

        EditText getEnterEditTextView();

        boolean getExpressiongTotal();
    }
}
