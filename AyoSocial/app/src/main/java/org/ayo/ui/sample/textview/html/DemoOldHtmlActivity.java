package org.ayo.ui.sample.textview.html;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.ayo.sample.R;
import org.ayo.ui.sample.base.AyoActivity;

/**
 * Created by Administrator on 2016/3/29.
 */
public class DemoOldHtmlActivity extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_spannable;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) findViewById(R.id.tv_2);

        String content = "<mxgsa>测试自定义标签</mxgsa>";

        //普通TextView
        tv_1.setText(content);

        //使用自定义标签
        tv_2.setText(Html.fromHtml(content, null, new MxgsaTagHandler(getActivity())));
        tv_2.setClickable(true);
        tv_2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}

