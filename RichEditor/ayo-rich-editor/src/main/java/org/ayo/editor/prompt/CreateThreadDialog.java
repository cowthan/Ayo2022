
package org.ayo.editor.prompt;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ayo.editor.R;
import org.ayo.editor.progress.ProgressImageView;

/**
 * Created by hujinghui on 14-10-13.
 */
public class CreateThreadDialog extends BaseDialog implements View.OnClickListener{


    TextView mContent;


    Button mConfirm;


    ProgressImageView mProgressImageView;

    private DialogListener mListener;

    public CreateThreadDialog(Context context, DialogListener listener) {
        super(context);
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_create_thread);
        mProgressImageView = (ProgressImageView) findViewById(R.id.progress_bar);
        mConfirm = (Button) findViewById(R.id.confirm);
        mContent = (TextView) findViewById(R.id.content);
        mConfirm.setOnClickListener(this);
        resetWidth();
    }

    public CreateThreadDialog setContent(String content) {
        mContent.setText(content);
        return this;
    }

    public CreateThreadDialog setConfirm(String text) {
        mConfirm.setText(text);
        return this;
    }

    public void showProgress(boolean show) {
        mProgressImageView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.confirm){
            if (mListener != null)
                mListener.onCancel(v);
        }
        cancel();
    }

    public interface DialogListener {
        void onCancel(View v);
    }
}
