
package org.ayo.editor.prompt;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ayo.editor.R;

/**
 * 提醒弹出框 Created by hujinghui on 14-10-13.
 */
public class RemindDialog extends BaseDialog implements View.OnClickListener{

    TextView mTitle;


    TextView mContent;


    Button mConfirm;

    private String text;

    private String title;

    private String mConfirmText;

    public RemindDialog(Context context, String text) {
        this(context, text, null, null);
    }

    public RemindDialog(Context context, String text, String title) {
        this(context, text, title, null);
    }

    public RemindDialog(Context context, String text, String title, String confirm) {
        super(context);
        this.text = text;
        this.title = title;
        this.mConfirmText = confirm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_match_fav);
        mConfirm = (Button) findViewById(R.id.confirm);
        mConfirm.setOnClickListener(this);
        mContent = (TextView) findViewById(R.id.content);
        mTitle = (TextView) findViewById(R.id.title);

        mContent.setText(text);
        if(!TextUtils.isEmpty(title)){
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(title);
        } else {
            mTitle.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(text)){
            mContent.setVisibility(View.VISIBLE);
            mContent.setText(text);
        } else {
            mContent.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mConfirmText)) {
            mConfirm.setText(mConfirmText);
        }
        resetWidth();
    }

    public void onClick(View v) {
        cancel();
    }


}
