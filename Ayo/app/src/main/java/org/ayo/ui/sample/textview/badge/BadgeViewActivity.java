package org.ayo.ui.sample.textview.badge;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import org.ayo.sample.R;
import org.ayo.ui.sample.base.AyoActivity;
import org.ayo.view.textview.BadgeView;

/**
 * Created by Administrator on 2016/3/28.
 */
public class BadgeViewActivity extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_spannable;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) findViewById(R.id.tv_2);

        ///badgeview
        BadgeView badge = new BadgeView(getActivity());
        badge.setTargetView(tv_1);
        badge.setBadgeCount(4);

        BadgeView badge2 = new BadgeView(getActivity());
        badge2.setTargetView(tv_2);
        badge2.setBadgeCount(422);
        ///

        String content = "目前有{numHospital}家医院{numSeller}位咨询师";
        content = content.replace("{numHospital}", "28").replace("{numSeller}", "325");

        //普通TextView
        tv_1.setText(content);

        //spannable处理过的TextView

        Spanned s = Html.fromHtml(content);
        int index0 = content.indexOf("前有")+2;
        int index1 = content.indexOf("家医院") + 3;
        int index2 = content.indexOf("位咨");

        String color = "#79d2be";
        SpannableString msp = new SpannableString(s);
        msp.setSpan(new RelativeSizeSpan(1.25f), index0, index1-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //0.5f表示默认字体大小的一半
        msp.setSpan(new RelativeSizeSpan(1.2f), index1,index2 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //0.5f表示默认字体大小的一半
        msp.setSpan(new ForegroundColorSpan(Color.parseColor(color)), index0, index1-3,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色
        msp.setSpan(new ForegroundColorSpan(Color.parseColor(color)),index1,index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色
        msp.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), index0, index1-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //斜体
        msp.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), index1,index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //斜体
        msp.setSpan(new ScaleXSpan(0.9f), index0, index1-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ScaleXSpan(0.9f), index1,index2 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_2.setText(msp);
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

}
