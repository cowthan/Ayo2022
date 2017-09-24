package org.ayo.ui.sample.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;

/**
 * Created by qiaoliang on 2017/3/24.
 */

public class DemoWebView extends BasePage {
    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_webview;
    }

    WebViewWrapper webViewWrapper;

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        ViewGroup webViewContainer = (ViewGroup) findViewById(R.id.webview_layout);
        FrameLayout customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);

        webViewWrapper = new WebViewWrapper(getActivity());
        webViewWrapper.attach(webViewContainer, customViewContainer);
        webViewWrapper.loadUrl("http://www.baidu.com");
    }




    @Override
    protected void onDestroy2() {
        webViewWrapper.onDestroy();
    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}
