package org.ayo.ui.sample.scrollview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qiaoliang on 2017/3/24.
 */

public class DemoScrollView2 extends BasePage {
    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_scrollview;
    }

    @BindView(R.id.ll_container)
    LinearLayout ll_container;

    @BindView(R.id.section_images)
    View section_images;

    @BindView(R.id.section_info)
    View section_info;

    @BindView(R.id.section_comment)
    View section_comment;

    @BindView(R.id.section_recommend)
    View section_recommend;

    @BindView(R.id.section_size)
    View section_size;

    @BindView(R.id.section_more)
    ImageView section_more;

    View[] sections = null;
    int[] sectionStartPoint = null;

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {

        ButterKnife.bind(this, view);
        sections = new View[]{
                section_images, section_info, section_comment, section_recommend, section_size
        };

        final NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);

        PullLoadMoreNestedScrollViewHelper pullHelper = new PullLoadMoreNestedScrollViewHelper();
        pullHelper.attach(scrollView);
        pullHelper.setOnPullListener(new OnPullListener() {
            @Override
            public void onPullRefresh() {

            }

            @Override
            public void onPullLoadMore() {
                onLoadMore();
            }
        });

        pullHelper.addOnScrollChangeListener(new PullLoadMoreHelper.OnMyScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                String s = String.format("scrollX = %d, scrollY = %d\noldScrollX = %d, oldScrollY = %d\n", scrollX, scrollY, oldScrollX, oldScrollY);

                int scrollViewHeight = v.getHeight();
                s += "scroll view 高度：" + scrollViewHeight + "\n";

                int childHeight = ll_container.getHeight();
                s += "scroll view 子控件的高度：" + childHeight + "\n";


                if(scrollViewHeight + scrollY == childHeight){
                    s += "拉到底了!\n";
                }

                for (int i = 0; i < sectionStartPoint.length; i++) {
                    s += sectionStartPoint[i] + "    ";
                }

                info(s);
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                sectionStartPoint = new int[]{
                        (int) section_images.getY(),
                        (int) section_info.getY(),
                        (int) section_comment.getY(),
                        (int) section_recommend.getY(),
                        (int) section_size.getY(),
                };
            }
        });
    }

    private void onLoadMore(){
        Toaster.toastShort("到底了，加载更多...");
        if(section_more.getVisibility() != View.VISIBLE){
            section_more.setVisibility(View.VISIBLE);
            section_more.setImageResource(R.drawable.a2);
        }
    }

    private void info(String m){
        TextView tv_info = id(R.id.tv_info);
        tv_info.setText(m);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }
}
