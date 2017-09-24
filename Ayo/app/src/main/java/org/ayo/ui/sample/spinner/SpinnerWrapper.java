package org.ayo.ui.sample.spinner;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ayo.sample.R;

import java.util.List;

import widget.spinner.NiceSpinner;

/**
 * Created by qiaoliang on 2017/7/1.
 *
 * 下拉框
 */

public class SpinnerWrapper {

    public static void bind(Activity a, NiceSpinner spinner, final List<? extends IOptionModel> items, final OnOptionSelectedListener callback){
        MyAdapter adapter = new MyAdapter(a, items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(callback != null){
                    callback.onSelected(view, items.get(pos), pos);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        spinner.showDropDown();
    }


    private static class MyAdapter extends BaseAdapter {
        private List<? extends IOptionModel> mList;
        private Context mContext;

        public MyAdapter(Context pContext, List<? extends IOptionModel> pList) {
            this.mContext = pContext;
            this.mList = pList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        /**
         * 下面是重要代码
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater _LayoutInflater= LayoutInflater.from(mContext);
            convertView=_LayoutInflater.inflate(R.layout.item_common_spinner, null);
            if(convertView!=null) {
//                ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
//                imageView.setImageResource(R.drawable.ic_launcher);
//                TextView _TextView1=(TextView)convertView.findViewById(R.id.textView1);
                TextView txtvwSpinner = (TextView)convertView.findViewById(R.id.txtvwSpinner);
                txtvwSpinner.setText(mList.get(position).getValue());
            }
            return convertView;
        }
    }
}