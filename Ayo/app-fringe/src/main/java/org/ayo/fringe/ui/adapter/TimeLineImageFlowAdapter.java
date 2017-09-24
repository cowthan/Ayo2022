package org.ayo.fringe.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ayo.fresco.Flesco;
import org.ayo.fringe.R;
import org.ayo.fringe.widget.NineGridLayout;

/**
 * Created by Administrator on 2016/4/23.
 */
public class TimeLineImageFlowAdapter extends NineGridLayout.BaseNineGridAdapter<String> {


    public TimeLineImageFlowAdapter(Activity c) {
        a = c;
    }

    private Activity a;


    @Override
    public View getView(LayoutInflater layoutInflater, int position) {
        View v = layoutInflater.inflate(R.layout.item_timeline_image, null);
        //ImageView iv_image = (ImageView) v.findViewById(R.id.iv_image);
        //Glider.load(a, iv_image, uri);
        String uri = getItem(position);
        SimpleDraweeView iv_image = (SimpleDraweeView) v.findViewById(R.id.iv_image);
        Flesco.setImageUri(iv_image, uri);
        return v;
    }

    @Override
    public int getCloumnNum() {
        return 3;
    }

    @Override
    public int getHorizontalSpacing() {
        return 16;
    }

    @Override
    public int getVerticalSpacing() {
        return 16;
    }

}
