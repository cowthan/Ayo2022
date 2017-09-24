package org.ayo.fringe.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ayo.core.Lang;
import org.ayo.fresco.Flesco;
import org.ayo.fringe.R;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.list.adapter.AyoViewHolder;
import org.ayo.list.adapter.ItemBean;
import org.ayo.fringe.model.top.Top;
import org.ayo.fringe.ui.base.AyoWebViewFragment;

/**
 * Created by Administrator on 2016/4/20.
 */
public class TopSingleAdapter extends AyoItemTemplate {


    public TopSingleAdapter(Activity activity) {
        super(activity, null);
    }

    public static Uri parse(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        return Uri.parse(url);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_top_single;
    }


    @Override
    public boolean isForViewType(ItemBean itemBean, int position) {
        if(itemBean instanceof Top){
            Top bean = (Top) itemBean;
            return bean.isSingleImage();
        }
        return false;
    }

    @Override
    public void onBindViewHolder(ItemBean itemBean, int position, AyoViewHolder holder) {

        final Top bean = (Top) itemBean;

        TextView tv_title = (TextView) holder.findViewById(R.id.tv_title);
        SimpleDraweeView iv_author_head = (SimpleDraweeView) holder.findViewById(R.id.iv_author_head);
        TextView tv_author_name = (TextView) holder.findViewById(R.id.tv_author_name);
        TextView tv_time = (TextView) holder.findViewById(R.id.tv_time);
        SimpleDraweeView iv_cover = (SimpleDraweeView) holder.findViewById(R.id.iv_cover);

        tv_title.setText(bean.title);
        Flesco.setImageUri(iv_author_head, bean.authorHeadImg);
//        Glider.load(mActivity, iv_author_head, bean.authorHeadImg);
        tv_author_name.setText(bean.authorName);
        tv_time.setText(Lang.toDate("yyyy-MM-dd", bean.createAt));
        Flesco.setImageUri(iv_cover, bean.getCoverImages(0));
//        Glider.load(mActivity, iv_cover, bean.getCoverImages(0));


        holder.root().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AyoWebViewFragment.start(getActivity(), bean.topUrl);
            }
        });

        iv_author_head.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AyoWebViewFragment.start(getActivity(), bean.authorUrl);
            }
        });
        tv_author_name.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AyoWebViewFragment.start(getActivity(), bean.authorUrl);
            }
        });
    }

}
