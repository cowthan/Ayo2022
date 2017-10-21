package org.ayo.ui.sample.dialog.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ayo.component.Master;
import org.ayo.component.MasterFragment;
import org.ayo.ui.sample.base.AyoActivity;

public class SimpleHomeActivity extends AyoActivity {
    private final String[] mItems = {"Dialog"};
    private final Class<?>[] mClazzs = {DialogHomeActivity.class};
    private DisplayMetrics mDisplayMetrics;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected View getLayoutView() {
        ListView lv = new ListView(getActivity());
        lv.setCacheColorHint(Color.TRANSPARENT);
        lv.setBackgroundColor(Color.WHITE);
        lv.setFadingEdgeLength(0);
        lv.setAdapter(new SimpleHomeAdapter());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Master.startPage(getActivity(), (Class<? extends MasterFragment>) mClazzs[position], null);
            }
        });
        return lv;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
//        mContext = getActivity();
        mDisplayMetrics = getResources().getDisplayMetrics();
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }


    class SimpleHomeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int padding = (int) (mDisplayMetrics.density * 10);

            TextView tv = new TextView(getActivity());
            tv.setText(mItems[position]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv.setTextColor(Color.parseColor("#468ED0"));
            // tv.setGravity(Gravity.CENTER);
            tv.setPadding(padding, padding, padding, padding);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            return tv;
        }
    }
}
