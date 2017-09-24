package org.ayo.fringe.ui.adapter;

import android.app.Activity;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ayo.core.Lang;
import org.ayo.fringe.R;
import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.list.adapter.AyoViewHolder;
import org.ayo.list.adapter.ItemBean;
import org.ayo.fringe.model.timeline.AyoTimeline;
import org.ayo.fringe.ui.photo.AyoGalleryFragment;
import org.ayo.fringe.widget.NineGridLayout;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class AyoTimeLineAdapter extends AyoItemTemplate {


    public AyoTimeLineAdapter(Activity activity) {
        super(activity, null);
    }

    public static Uri parse(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        return Uri.parse(url);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_timeline;
    }


    @Override
    public boolean isForViewType(ItemBean itemBean, int position) {
        if(itemBean instanceof AyoTimeline){
            AyoTimeline bean = (AyoTimeline) itemBean;
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(ItemBean itemBean, int position, AyoViewHolder holder) {

        final AyoTimeline bean = (AyoTimeline) itemBean;

        TextView tv_content = (TextView) holder.findViewById(R.id.tv_content);
        SimpleDraweeView iv_user_logo = (SimpleDraweeView) holder.findViewById(R.id.iv_user_logo);
        TextView tv_user_name = (TextView) holder.findViewById(R.id.tv_user_name);
        TextView tv_info = (TextView) holder.findViewById(R.id.tv_info);

        tv_content.setText(bean.text + "--" + Lang.count(bean.urls));
        String info = Lang.toDate("yyyy-MM-dd", Lang.toInt(bean.created_at)/1000) + " " + "来自 " + bean.source;
        Spanned infoSpan = Html.fromHtml(info);
        tv_info.setText(infoSpan);

        if(bean.user != null){
            tv_user_name.setText(bean.user.name);
            iv_user_logo.setImageURI(parse(bean.user.avatar_large));
        }

        ///-----------------处理图片，视频----------------------//
        NineGridLayout fl_flowlayout = (NineGridLayout) holder.findViewById(R.id.fl_flowlayout);
        TimeLineImageFlowAdapter flowAdapter = new TimeLineImageFlowAdapter(getActivity());
        fl_flowlayout.setAdapter(flowAdapter);

        if(Lang.isNotEmpty(bean.urls)){
            fl_flowlayout.setVisibility(View.VISIBLE);
            if(bean.type == 1){
                //文+图，最多9张
                flowAdapter.notifyDataSetChanged(bean.urls);

            }else if(bean.type == 2){
                //文+图，只有两张，0是封面图， 1是url地址
                //fl_flowlayout.setAdapter(null);
                fl_flowlayout.setVisibility(View.GONE);
            }
        }else{
            //没有图片，视频
            //fl_flowlayout.setAdapter(null);
            fl_flowlayout.setVisibility(View.GONE);
        }
        ///---------------------图片视频 over----------------------//
        holder.root().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fl_flowlayout.setOnItemClickListener(new NineGridLayout.OnItemClickListener(){

            @Override
            public void onItemClick(View v, int position) {
                if(bean.urls != null){
                    ArrayList<ImageInfo> images = new ArrayList<ImageInfo>();
                    for(String url: bean.urls){
                        ImageInfo ii = new ImageInfo(url);
                        images.add(ii);
                    }
                    AyoGalleryFragment.start(getActivity(), images, position);
                }
            }
        });

        fl_flowlayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            }
        });
    }

    public static class ImageInfo implements AyoGalleryFragment.IImageInfo, Serializable{

        private String url;

        public ImageInfo(String url) {
            this.url = url;
        }

        @Override
        public String getUri() {
            return url;
        }

        @Override
        public String getLocalUri() {
            return "";
        }
    }
}
