package org.ayo.component.indicator;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public interface OnTabSelectedListener {
    void onTabSelected(int position, int prePosition);

    void onTabUnselected(int position);

    void onTabReselected(int position);
}