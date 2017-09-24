package org.ayo.view3.verticalbanner;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public abstract class VerticalBannerAdapter<T> {
    private List<T> mDatas;
    private OnDataChangedListener mOnDataChangedListener;

    public VerticalBannerAdapter(List<T> datas) {
        mDatas = datas;
        if (datas == null || datas.isEmpty()) {
            throw new RuntimeException("nothing to show");
        }
    }

    public VerticalBannerAdapter(T[] datas) {
        mDatas = new ArrayList<>(Arrays.asList(datas));
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
        notifyDataChanged();
    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    void notifyDataChanged() {
        mOnDataChangedListener.onChanged();
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    public abstract View getView(VerticalBannerView parent);

    public abstract void setItem(View view, T data);


    interface OnDataChangedListener {
        void onChanged();
    }
}
