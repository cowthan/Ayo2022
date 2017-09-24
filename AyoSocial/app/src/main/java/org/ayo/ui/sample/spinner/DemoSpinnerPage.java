package org.ayo.ui.sample.spinner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.ayo.core.Lang;
import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;
import org.ayo.ui.sample.BasePage;

import java.util.ArrayList;
import java.util.List;

import widget.spinner.NiceSpinner;

/**
 * Created by qiaoliang on 2017/7/1.
 */

public class DemoSpinnerPage extends BasePage {
    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_spinner;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.spinner);

//        LinkedList<String> data=new LinkedList<>(Arrays.asList("Zhang", "Phil", "@", "CSDN"));
//        niceSpinner.attachDataSource(data);

        List<Option> items = new ArrayList<>();
        items.add(new Option("1", "一二三四五"));
        items.add(new Option("2", "111112222233334444"));
        items.add(new Option("3", "aaaaaaaaaaaaaaaaaa"));
        items.add(new Option("4", "v"));
        items.add(new Option("5", "请选择"));
        SpinnerWrapper.bind(getActivity(), niceSpinner, items, new OnOptionSelectedListener() {
            @Override
            public void onSelected(View v, IOptionModel m, int position) {
                Toaster.toastShort("选中：" + m.getKey());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        niceSpinner.setSelectedIndex(Lang.count(items) - 1);

    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

    public static class Option implements IOptionModel{

        public String k;
        public String v;

        public Option(){}

        public Option(String k, String v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public String getValue() {
            return v;
        }

        @Override
        public String getKey() {
            return k;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }
}
