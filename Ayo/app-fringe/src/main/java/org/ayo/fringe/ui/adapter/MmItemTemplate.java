package org.ayo.fringe.ui.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ayo.fresco.Flesco;
import org.ayo.fringe.R;
import org.ayo.fringe.model.MmModel;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.list.adapter.AyoViewHolder;

/**
 * Created by Administrator on 2016/4/20.
 */
public class MmItemTemplate extends AyoItemTemplate<MmModel> {

    public MmItemTemplate(Activity activity) {
        super(activity, null);
    }

    @Override
    public boolean isForViewType(MmModel itemBean, int position) {
        if(itemBean instanceof MmModel){
            MmModel bean = (MmModel) itemBean;
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(MmModel m, int position, AyoViewHolder holder) {
        TextView tv_title = (TextView) holder.findViewById(R.id.tv_title);
        SimpleDraweeView iv_photo = (SimpleDraweeView) holder.findViewById(R.id.iv_photo);
        tv_title.setText(m.title);
        Flesco.setImageUri(iv_photo, m.img_url);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_mm;
    }

}
