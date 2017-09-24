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
import org.ayo.fringe.model.timeline.Timeline;
import org.ayo.fringe.ui.base.AyoWebViewFragment;
import org.ayo.fringe.ui.photo.AyoGalleryFragment;
import org.ayo.fringe.utils.Utils;
import org.ayo.fringe.widget.NineGridLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class TimeLineWeiboAdapter extends AyoItemTemplate {


    public TimeLineWeiboAdapter(Activity activity) {
        super(activity, null);
    }

    public static Uri parse(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        return Uri.parse(url);
    }

    @Override
    public boolean isForViewType(ItemBean itemBean, int position) {
        if(itemBean instanceof Timeline){
            Timeline bean = (Timeline) itemBean;
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(ItemBean itemBean, int position, AyoViewHolder holder) {

        final Timeline bean = (Timeline) itemBean;

        TextView tv_content = (TextView) holder.findViewById(R.id.tv_content);
        SimpleDraweeView iv_user_logo = (SimpleDraweeView) holder.findViewById(R.id.iv_user_logo);
        TextView tv_user_name = (TextView) holder.findViewById(R.id.tv_user_name);
        TextView tv_info = (TextView) holder.findViewById(R.id.tv_info);

        tv_content.setText(bean.text);
        String info = Lang.tryToDate(bean.created_at, "yyyy-MM-dd") + " " + "来自 " + bean.source;
        Spanned infoSpan = Html.fromHtml(info);
        tv_info.setText(infoSpan);

        if(bean.user != null){
            tv_user_name.setText(bean.user.name);
            iv_user_logo.setImageURI(parse(bean.user.avatar_large));
        }

        ///-----------------处理图片，视频----------------------//
        ///对于微博来说，如果original_pic有值，说明至少有一个图
        //original_pic的值是：http://ww2.sinaimg.cn/thumbnail/006A6daFgw1fbskz8jmlij30hs0mq74r.jpg
        //pic_urls里就是所有图的缩略图（不一定有哦），缩略图的值是：http://ww2.sinaimg.cn/large/006A6daFgw1fbskz8jmlij30hs0mq74r.jpg
        //thumbnail_pic可以作为封面图，如果不够大，bmiddle_pic是中图
        //bmiddle_pic的值是：http://ww2.sinaimg.cn/bmiddle/006A6daFgw1fbskz8jmlij30hs0mq74r.jpg

        ///首先确认有几个图，并取出所有的thumbnail，或者bmiddle
        NineGridLayout fl_flowlayout = (NineGridLayout) holder.findViewById(R.id.fl_flowlayout);
        TimeLineImageFlowAdapter flowAdapter = new TimeLineImageFlowAdapter(getActivity());
        fl_flowlayout.setAdapter(flowAdapter);

        String thubmnail = "thumbnail";
        String bmiddle = "bmiddle";
        String large = "large";

        final List<String> imgs = new ArrayList<>();

        if(Lang.isNotEmpty(bean.original_pic)){
            ///有图
            fl_flowlayout.setVisibility(View.VISIBLE);


            if(Lang.isNotEmpty(bean.pic_urls)){
                for(Timeline.WeiboCoverImage img: bean.pic_urls){
                    imgs.add(img.getMiddle());
                }
            }else{
                imgs.add(bean.original_pic.replace(large, bmiddle));
            }

            flowAdapter.notifyDataSetChanged(imgs);
        }else{
            fl_flowlayout.setVisibility(View.GONE);
        }

        ///---------------------图片视频 over----------------------//

        holder.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://api.weibo.com/2/statuses/go?access_token={token}&uid={uid}&id={id}&source={source}";
                url = url.replace("{token}", Utils.getWeiboToken());
                url = url.replace("{uid}", Utils.getCurrentWeiboUserUid());
                url = url.replace("{id}", bean.id);
                AyoWebViewFragment.start(getActivity(), url);
//                log.e(url);
            }
        });

        fl_flowlayout.setOnItemClickListener(new NineGridLayout.OnItemClickListener(){

            @Override
            public void onItemClick(View v, int position) {
                AyoGalleryFragment.start(getActivity(), (ArrayList<? extends AyoGalleryFragment.IImageInfo>) bean.pic_urls, position);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_timeline_weibo;
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
