package org.ayo.ui.sample.view_learn;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;
import org.ayo.view.Display;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/26.
 */

public class FakeHorizontalScrollViewDemo extends BasePage {

    private FakeHorizontalScrollView mListContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_fake_horizontal_scroll_view;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        Log.d("aaaa", "onCreate");
        initView();
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

    private void initView(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mListContainer = (FakeHorizontalScrollView) findViewById(R.id.mListContainer);
        final int screenWidth = Display.screenWidth;
        final int screenHeight = Display.screenHeight;
        for (int i = 0; i < 3; i++){
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.content_layout, mListContainer, false);
            layout.getLayoutParams().width = screenWidth;
            TextView textView = (TextView) layout.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            layout.setBackgroundColor(Color.rgb(255/(i+1), 255/(i+1), 0));
            createList(layout);
            mListContainer.addView(layout);
        }
    }

    private void createList(ViewGroup layout){
        ListView listView = (ListView) layout.findViewById(R.id.list);
        ArrayList<String> datas = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            datas.add("name " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.content_list_item, R.id.name, datas);
        listView.setAdapter(adapter);
    }


}
