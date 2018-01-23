
package org.ayo.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by qiaoliang on 2017/4/18.
 */

public abstract class TextWatcherForLimitLength implements TextWatcher {
    private CharSequence temp;

    private int selectionStart;

    private int selectionEnd;

    private EditText tv;

    public TextWatcherForLimitLength(EditText tv) {
        this.tv = tv;
    }

    public abstract int getLimitedLength();

    public abstract void onLengthChanged(int length);

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        temp = s; // s是输入之前的字符串，即刚输入的还没加上
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        selectionStart = tv.getSelectionStart();
        selectionEnd = tv.getSelectionEnd();
        int len = s.length();
        if (temp.length() > getLimitedLength()) {
            CharSequence cs = s.subSequence(0, getLimitedLength());
            len = cs.length();
            int tempSelection = selectionStart;
            tv.setText(cs);
            tv.setSelection(len);// 设置光标在最后
        }
        this.onLengthChanged(len);
    }
}
