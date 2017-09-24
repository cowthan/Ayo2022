package org.ayo.fringe.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import org.ayo.fringe.R;

import java.util.Map;


/**
 * 一个fragment，只有一个webview
 *
 * 如果在外面调loadUrl，可能onCreateView还没执行完
 *
 * 所以要这么用：
 * MyWebFragment f = new MyWebFragment();
 * f.setUrl("http://www.baidu.com/, null);
 * fragmentManager.beginTransaction().replace(id, f).commit();
 *
 * f.loadUrl(); //在这里调用会报空指针，因为onCreateView还没执行，
 * //当然也可以postDelayed里调，但有什么意思呢，你知道应该delay几秒？500毫秒？200毫秒？其实0也行
 *
 */
/**
 * 如果要在js中回调java，需要实现此方法，套路如下
 * 1 调用：webview.addJavascriptInterface(this,"javaObj");
 * 参数1：哪个java对象会作为js对象传入浏览器
 * 参数2：该java对象在js中的对象名
 *
 * 2 定义js方法：
 *
 * @JavascriptInterface
public boolean openUrl(String url) {
if(!url.startsWith("http")){
url = Urls.DOMAIN2 + url;
}
System.out.println("处理超链接--" + url);
return false;
}
（1）在js中调用：javaObj.openUrl(url)
（2）注意注解不能丢
 *
 *
 */
public class AyoWebViewFragment extends BaseFrgFragment {

    public static void start(Activity a, String url){
        Bundle b = new Bundle();
        b.putString("url", url);
        Pages.startWithSwipeback(a, AyoWebViewFragment.class, b);
    }

    private WebView webview;
    private ProgressBar pb_webview;

    private View root = null;


    @Override
    protected int getLayoutId() {
        return R.layout.ayo_tmpl_frag_webview;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        root = view;
        pb_webview = (ProgressBar) root.findViewById(R.id.pb_webview);
        webview = new WebView(getActivity().getApplicationContext());
        FrameLayout mll = (FrameLayout) findViewById(R.id.webview_container);
        mll.addView(webview);

        initWebViewSetting();
        webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        url = getArguments().containsKey("url") ? getArguments().getString("url") : "";

        if(url != null && !url.equals("")){
            loadUrl(url, headers);
        }

    }

    @Override
    protected void onDestroy2() {
        try {
            ((ViewGroup)webview.getParent()).removeView(webview);
            webview.removeAllViews();
            webview.destroy();
            webview = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }


    private String url;
    private Map<String, String> headers;


    public void loadUrl(String url, Map<String, String> headers){
        webview.loadUrl(url, headers);
    }

    public void setOnTouchEvent(View.OnTouchListener onTouchEvent)
    {
        webview.setOnTouchListener(onTouchEvent);
    }




    public void initWebViewSetting() {
        WebSettings settings = webview.getSettings();
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        settings.setDefaultTextEncodingName("UTF -8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setNeedInitialFocus(false);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        settings.setLoadWithOverviewMode(true); // 设置WebView 可以加载更多格式页面
        settings.setUseWideViewPort(true); // 设置WebView使用广泛的视窗
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true); // //设置是否启用了DOM storage API。
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 自动打开窗口

        webview.setDrawingCacheEnabled(false);
        settings.setJavaScriptEnabled(true);//设置可以运行JS脚本
        webview.addJavascriptInterface(this,"javaObj");

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb_webview.setVisibility(View.VISIBLE);
                pb_webview.setProgress(0);

            }

            @SuppressLint("NewApi")
            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                pb_webview.setVisibility(View.GONE);
            }

        });

        webview.setWebChromeClient(myWebChromeClient);

    }

    WebChromeClient myWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            super.onProgressChanged(view, newProgress);
            // 动态在标题栏显示进度条
            pb_webview.setProgress(newProgress);

        }
    };
}
