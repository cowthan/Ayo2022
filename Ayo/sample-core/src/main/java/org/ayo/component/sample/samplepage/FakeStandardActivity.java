package org.ayo.component.sample.samplepage;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Administrator on 2017/5/21.
 */

public class FakeStandardActivity extends BaseFakeActivity {

    public static void start(Activity a, boolean newTask){
        Intent intent = new Intent(a, FakeStandardActivity.class);
        intent.putExtra("referer", "referer，表示页面从哪里被打开");
        if(newTask) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.startActivity(intent);
    }
    @Override
    protected String getTopTitle() {
        return "SingleStandard-不透明";
    }
}
