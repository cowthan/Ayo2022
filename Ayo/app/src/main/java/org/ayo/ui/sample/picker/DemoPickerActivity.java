package org.ayo.ui.sample.picker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.ayo.JsonUtils;
import org.ayo.core.Lang;
import org.ayo.imagepicker.PhotoListActivity;
import org.ayo.imagepicker.ThumbModel;
import org.ayo.imagepicker.VideoListActivity;
import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;

import java.util.List;

/**
 * Created by Administrator on 2016/1/26.
 */
public class DemoPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_picker);


        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoListActivity.startForResult(DemoPickerActivity.this, 6);
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoListActivity.startForResult(DemoPickerActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView tv_info = (TextView) findViewById(R.id.tv_info);
        List<ThumbModel> list = PhotoListActivity.tryToHandleResult(this, requestCode, resultCode, data);
        if(Lang.isNotEmpty(list)){
            tv_info.setText(JsonUtils.toJsonPretty(list));
            Toaster.toastShort("选了：" + Lang.count(list) + "张");
        }

        List<ThumbModel> list2 = VideoListActivity.tryToHandleResult(this, requestCode, resultCode, data);
        if(Lang.isNotEmpty(list2)){
            tv_info.setText(JsonUtils.toJsonPretty(list2));
            Toaster.toastShort("选了：" + Lang.count(list2) + "个视频");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
