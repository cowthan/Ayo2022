package org.ayo.ui.sample.verticalbanner;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
public class SampleAdapter02 extends VerticalBannerAdapter<Model01> {
    private List<Model01> mDatas;

    public SampleAdapter02(List<Model01> datas) {
        super(datas);
    }

    @Override
    public View getView(VerticalBannerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.verticalbanner_item_02,null);
    }

    @Override
    public void setItem(final View view, final Model01 data) {
        TextView tv = (TextView) view.findViewById(R.id.tv_02);
        tv.setText(data.title);
        //你可以增加点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如打开url
                Toast.makeText(view.getContext(),data.url,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
