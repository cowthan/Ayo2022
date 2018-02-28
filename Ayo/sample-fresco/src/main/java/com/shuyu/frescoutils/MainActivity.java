package com.shuyu.frescoutils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    Button viewPager;
    @BindView(R.id.list)
    Button list;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.viewPager, R.id.list, R.id.btn_processor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewPager: {
                Intent intent = new Intent(MainActivity.this, ImageViewPagerActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.list: {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_processor: {
                Intent intent = new Intent(MainActivity.this, DemoProcessorActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
