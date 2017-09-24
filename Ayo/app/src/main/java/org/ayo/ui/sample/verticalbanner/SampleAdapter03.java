package org.ayo.ui.sample.verticalbanner;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.ayo.sample.R;
import org.ayo.view3.verticalbanner.VerticalBannerAdapter;
import org.ayo.view3.verticalbanner.VerticalBannerView;

import java.util.List;

/**
 * Description:
 * <p/>
 * Created by rowandjj(chuyi)<br/>
 * Date: 16/1/7<br/>
 * Time: 下午2:41<br/>
 */
public class SampleAdapter03 extends VerticalBannerAdapter<Model01> {
    private List<Model01> mDatas;

    public SampleAdapter03(List<Model01> datas) {
        super(datas);
    }

    @Override
    public View getView(VerticalBannerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.verticalbanner_item_03,null);
    }

    @Override
    public void setItem(final View view, final Model01 data) {
        TextView tv = (TextView) view.findViewById(R.id.title);
        tv.setText(data.title);

        TextView tag = (TextView) view.findViewById(R.id.tag);
        tag.setText(data.url);

    }
}
