package org.ayo.ui.sample.log;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.ayo.core.Lang;
import org.ayo.http.utils.JsonUtils;
import org.ayo.log.LogReporter;
import org.ayo.sample.R;

/**
 */
public class DemoLogReporterActivity extends AppCompatActivity {

    private LinearLayout mContainerBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_demo);

        mContainerBtns = (LinearLayout) findViewById(R.id.container_btns);

        addButton("", "报告事件--如页面切换，点击事件，后台状态，加载完成等", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LogReporter.report("点击事件--购买", "product_detail_buy_button_click", Lang.newHashMap("productCode", "2123323-aaa-33"));
            }
        });

        addButton("", "报告一般事", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LogReporter.report("购物车数据库查询完毕", "subTitle", JsonUtils.toJson(Lang.newHashMap("productCode", "2123323-aaa-33")), "来自页面CartFragment");
            }
        });

        addButton("", "报告异常--不必非得导致崩溃", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RuntimeException e = new RuntimeException("发生了一般性或者崩溃的异常!!!");
                LogReporter.report(e);
            }
        });

        addButton("", "报告http开始", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LogReporter.reportHttpStart("tag-get-product-detail", "get", "https://www.dddd.com/api/mall/product/2asfsf", Lang.<String, String>newHashMap("productCode", "2123323-aaa-33"));
            }
        });

        addButton("", "报告http结束，没错", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LogReporter.reportHttpEnd("tag-get-product-detail", "get", "https://www.dddd.com/api/mall/product/2asfsf", Lang.<String, String>newHashMap("productCode", "2123323-aaa-33"), "返回的json放这里");
            }
        });

        addButton("", "报告http结束，出错", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RuntimeException e = new RuntimeException("发生了http的异常!!!");
                LogReporter.reportHttpEnd("tag-get-product-detail", "get", "https://www.dddd.com/api/mall/product/2asfsf", Lang.<String, String>newHashMap("productCode", "2123323-aaa-33"), e);
            }
        });

    }

    private void addButton(String info, final String scheme, View.OnClickListener onClickListener){
        Button btn = new Button(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Lang.MATCH, Lang.dip2px(40));
        lp.topMargin = 20;

        btn.setText(scheme);

        if(scheme.startsWith("--")){
            btn.setTextColor(Color.MAGENTA);
            btn.setTextSize(12);
        }else{
            btn.setTextSize(10);
            btn.setOnClickListener(onClickListener);
        }

        mContainerBtns.addView(btn, lp);

    }


    private Activity getActivity(){
        return this;
    }
}
