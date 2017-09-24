package org.ayo.ui.sample.spinner;

import android.view.View;

/**
 * Created by qiaoliang on 2017/7/1.
 */

public interface OnOptionSelectedListener {
    void onSelected(View v, IOptionModel m, int position);
    void onNothingSelected();
}
