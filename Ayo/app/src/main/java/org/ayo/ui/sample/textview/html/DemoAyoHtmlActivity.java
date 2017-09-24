package org.ayo.ui.sample.textview.html;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import org.ayo.sample.R;
import org.ayo.ui.sample.base.AyoActivity;
import org.ayo.view.textview.html.HtmlTagHandler;

/**
 * Created by Administrator on 2016/3/29.
 */
public class DemoAyoHtmlActivity extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_spannable;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) findViewById(R.id.tv_2);

        String content = "呵呵呵<span style=\"{color:#e60012}\">哈哈哈</span>嘿嘿嘿";

        //普通TextView
        tv_1.setText(content);

        //使用自定义标签
        content = "<html><body>" + content + "</body></html>";
        Spanned s = HtmlTagHandler.fromHtml(content, null, new SpanTagHandler());
        tv_2.setText(s);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}

