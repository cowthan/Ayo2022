package org.ayo.ui.sample.webview;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.ayo.sample.R;

import java.util.Map;

/**
 * Created by qiaoliang on 2017/3/24.
 */

public class WebViewWrapper {

    private View mCustomView;
    WebView webview;

    FrameLayout customViewContainer;
    ViewGroup webViewContainer;

    private MyWebChromeClient mWebChromeClient;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private Context mContext;

    public WebViewWrapper(Context c){
        this.mContext = c;
    }

    public void attach(ViewGroup webViewContainer, FrameLayout customViewContainer){
        this.webViewContainer = webViewContainer;
        this.customViewContainer= customViewContainer;
        initWebView();
    }

    private Context getContext(){
        return mContext;
    }

    public void loadUrl(String url, Map<String, String> header){
        webview.loadUrl(url, header);
        // 屏蔽某些手机长按事件,复制奔溃(另一种代替方法初始化webview传getApplicationContext()替换成NewsDetailActivity.this)
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Build.VERSION.SDK_INT <= 17) {
                    return true;
                }
                return false;
            }
        });
    }
    public void loadUrl(String url){
        webview.loadUrl(url);
        // 屏蔽某些手机长按事件,复制奔溃(另一种代替方法初始化webview传getApplicationContext()替换成NewsDetailActivity.this)
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (Build.VERSION.SDK_INT <= 17) {
                    return true;
                }
                return false;
            }
        });
    }

    private void initWebView() {

        webview = (WebView) LayoutInflater.from(getContext()).inflate(R.layout.ayo_layout_webview, null);
        webViewContainer.addView(webview);
        mWebChromeClient = new MyWebChromeClient();
        webview.setWebChromeClient(mWebChromeClient);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(false);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("UTF-8");
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        settings.setUserAgentString(
//                "news/" + AppConstant.VERSIONCODE + " " + settings.getUserAgentString()
//                        + " NewsApp/" + AppConstant.VERSIONCODE + " NetType/");
        // 设置加载进来的页面自适应手机屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        ///加这三行，是因为loadUrl会打开系统浏览器！！
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.setWebViewClient(new WebViewClient());
    }

    class MyWebChromeClient extends WebChromeClient {

        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, int requestedOrientation,
                                     CustomViewCallback callback) {
            onShowCustomView(view, callback); // To change body of overridden
            // methods use File | Settings |
            // File Templates.
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            webview.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                mVideoProgressView = inflater.inflate(R.layout.ayo_layout_webview_video_progress, null);
            }
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView(); // To change body of overridden methods
            // use File | Settings | File Templates.
            if (mCustomView == null)
                return;
            webview.setVisibility(View.VISIBLE);
            customViewContainer.setVisibility(View.GONE);

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

        }
    }


    protected void onDestroy() {
        if (webview != null) {
            ViewGroup viewGroup = (ViewGroup) webview.getParent();
            if (null != viewGroup) {
                viewGroup.removeView(webview);
            }
            webview.loadUrl("about:blank");
            webview.removeAllViews();
            webview.destroy();
        }
    }
}
