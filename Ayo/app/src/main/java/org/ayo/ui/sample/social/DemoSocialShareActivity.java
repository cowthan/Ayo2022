package org.ayo.ui.sample.social;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.ayo.core.Lang;
import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;
import org.ayo.social.SocialCenter;
import org.ayo.social.callback.BaseShareCallback;
import org.ayo.social.callback.ShareCallback;
import org.ayo.social.model.ShareArticle;

/**
 */
public class DemoSocialShareActivity extends AppCompatActivity {

    private LinearLayout mContainerBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_router_demo);

        mContainerBtns = (LinearLayout) findViewById(R.id.container_btns);

        final ShareArticle a = new ShareArticle();
        a.title = "微信怎么分享呢";
        a.desc = "这是一个测试分享";
        a.imageUrl = "http://images.17173.com/2012/news/2012/03/16/mj0316cm07s.jpg";
        a.redirectUrl = "http://img01.taopic.com/141010/240406-1410100G94458.jpg";
        final String demoImageUrl = "http://img01.taopic.com/141010/240406-1410100G94458.jpg";

        addButton("", "分享微信（文章）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareArticle(SocialCenter.PLATFORM_WX_SESSION, getActivity(), a, callback);
            }
        });

        addButton("", "分享微信（图片）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareImage(SocialCenter.PLATFORM_WX_SESSION, getActivity(), demoImageUrl, callback);
            }
        });

        addButton("", "分享朋友圈（文章）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareArticle(SocialCenter.PLATFORM_WX_TIMELINE, getActivity(), a, callback);

            }
        });

        addButton("", "分享朋友圈（图片）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareImage(SocialCenter.PLATFORM_WX_TIMELINE, getActivity(), demoImageUrl, callback);
            }
        });

        addButton("", "分享QQ（文章）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareArticle(SocialCenter.PLATFORM_QQ, getActivity(), a, callback);

            }
        });

        addButton("", "分享QQ（图片）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareImage(SocialCenter.PLATFORM_QQ, getActivity(), demoImageUrl, callback);
            }
        });

        addButton("", "分享qzong（文章）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareArticle(SocialCenter.PLATFORM_QZONG, getActivity(), a, callback);

            }
        });

        addButton("", "分享qzong（图片）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareImage(SocialCenter.PLATFORM_QZONG, getActivity(), demoImageUrl, callback);
            }
        });

        addButton("", "分享微博（文章）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareArticle(SocialCenter.PLATFORM_WB, getActivity(), a, callback);

            }
        });

        addButton("", "分享微博（图片）", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SocialCenter.getDefault().shareImage(SocialCenter.PLATFORM_WB, getActivity(), demoImageUrl, callback);
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

    private ShareCallback callback = new BaseShareCallback() {
        @Override
        public void onSuccess() {
            Toaster.toastShort("好了");
        }

        @Override
        public void onCancel() {
            Toaster.toastShort("取消了");
        }

        @Override
        public void onFail(@Nullable Exception e, String reason) {
            Toaster.toastShort("失败了：" + reason);

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SocialCenter.getDefault().tryToGetQQCallback(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Activity getActivity(){
        return this;
    }
}
