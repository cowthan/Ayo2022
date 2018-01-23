
package org.ayo.notify.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.ayo.notify.R;

/**
 * Created by hujinghui on 14-10-13.
 */
public class SimpleProgressDialog extends Dialog {


    ProgressImageView mProgressView;

    public SimpleProgressDialog(Context context) {
        super(context, R.style.Ayo_DialogStyle);
    }

    public SimpleProgressDialog(Context context, boolean cancelable) {
        super(context, R.style.Ayo_DialogStyle);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayo_notify_dialog_progress);
        mProgressView = (ProgressImageView) findViewById(R.id.view_list_empty_progress);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mProgressView != null)
            mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mProgressView != null)
            mProgressView.setVisibility(View.GONE);
    }
}
