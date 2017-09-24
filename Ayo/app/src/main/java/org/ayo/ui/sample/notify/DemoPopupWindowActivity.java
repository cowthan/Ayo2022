package org.ayo.ui.sample.notify;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ayo.notify.AyoUI_notify;
import org.ayo.notify.LocalDisplay;
import org.ayo.notify.popup.PopupWindowHelper;
import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;
import org.ayo.ui.sample.base.AyoActivity;
import org.ayo.view.AyoViewLib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class DemoPopupWindowActivity extends AyoActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_demo_popup;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        final Button btn_1 = (Button) findViewById(R.id.btn_1);
        final Button btn_2 = (Button) findViewById(R.id.btn_2);
        final Button btn_3 = (Button) findViewById(R.id.btn_3);

        final List<Tag30> tags = new ArrayList<>();
        tags.add(new Tag30("11", "足球"));
        tags.add(new Tag30("12", "篮球"));
        tags.add(new Tag30("13", "拍皮球"));
        tags.add(new Tag30("14", "打羽毛球"));
        tags.add(new Tag30("15", "打乒乓球"));
        tags.add(new Tag30("16", "头上顶个球"));
        tags.add(new Tag30("17", "桌球"));
        tags.add(new Tag30("18", "网球"));


        btn_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showMenuExtra(getActivity(), btn_1);
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showSubscribeWindow(getActivity(), btn_2, tags);
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showSubscribeWindow(getActivity(), btn_3, tags);
            }
        });
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }

    public static void showMenuExtra(final Activity a, View target){

        int popHeight = LocalDisplay.dp2px(230);

        int[] loc = new int[2];
        target.getLocationOnScreen(loc);

        View contentView1 = View.inflate(AyoUI_notify.app, R.layout.dg_layout_popup_menu_extra, null);
        final PopupWindowHelper pop = new PopupWindowHelper(a, contentView1, true);

        //箭头
        ImageView iv_arrow = (ImageView) contentView1.findViewById(R.id.iv_arrow);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) iv_arrow.getLayoutParams();
        lp.gravity = Gravity.TOP | Gravity.RIGHT;
        lp.rightMargin = LocalDisplay.dp2px(23);
        iv_arrow.setLayoutParams(lp);

        View ll_super = contentView1.findViewById(R.id.ll_super);
        View ll_zxing = contentView1.findViewById(R.id.ll_zxing);

        ll_super.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pop.dismiss();
                Toaster.toastShort("ok");
            }
        });

        ll_zxing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pop.dismiss();
                Toaster.toastShort("ok");
            }
        });


        pop.showAsDropDown(target, PopupWindowHelper.TYPE_WRAP_CONTENT, 30, 0, R.style.Ayo_Popup_AnimationUpPopup3);

    }


    public void showSubscribeWindow(final Activity a, View target, List<Tag30> tags){

        ///模拟弹出框的高度
        int popHeight = LocalDisplay.dp2px(230);

        int[] loc = new int[2];
        target.getLocationOnScreen(loc);

        boolean shouldShowBelow = true;
        if(LocalDisplay.SCREEN_HEIGHT_PIXELS - loc[1] < popHeight){
            //下面没地方了
            shouldShowBelow = false;
        }else{
            shouldShowBelow = true;
        }

        View contentView1 = View.inflate(AyoUI_notify.app, R.layout.dg_layout_popup_recommend, null);
        final PopupWindowHelper pop = new PopupWindowHelper(a, contentView1, true);

        //箭头
        ImageView iv_arrow = (ImageView) contentView1.findViewById(R.id.iv_arrow);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) iv_arrow.getLayoutParams();
        if(shouldShowBelow){
            //箭头在上
            lp.gravity = Gravity.TOP | Gravity.RIGHT;
        }else{
            lp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            lp.topMargin = 0;
            lp.bottomMargin = LocalDisplay.dp2px(3);
            iv_arrow.setImageResource(R.drawable.ic_recommend_arrow_reverse);
        }
        lp.rightMargin = LocalDisplay.dp2px(18);

        iv_arrow.setLayoutParams(lp);

        //
        final TextView tv_confirm = (TextView) contentView1.findViewById(R.id.tv_confirm);
        final TextView tv_label = (TextView) contentView1.findViewById(R.id.tv_label);
        //数据
        final List<Tag30> selected = new ArrayList<>();
        final String textLabel = "可选理由：<font color='#e60012'>num</font>/" + tags.size();
        tv_confirm.setText("不感兴趣");
        tv_label.setText(Html.fromHtml(textLabel.replace("num", 0+"")));


        ////方式1：
        final RecyclerView rl_focusCar = (RecyclerView) contentView1.findViewById(R.id.slider);
        Callback callback1 = new Callback(){

            @Override
            public void onTagClicked(int position, Tag30 tag) {
                if(tag.isSelected){
                    selected.add(tag);
                }else{
                    selected.remove(tag);
                }

                if(selected == null || selected.size() == 0){
                    tv_confirm.setText("不感兴趣");
                    tv_label.setText(Html.fromHtml(textLabel.replace("num", 0+"")));
                }else{
                    tv_confirm.setText("确定");
                    tv_label.setText(Html.fromHtml(textLabel.replace("num", selected.size()+"")));
                }
            }
        };
        TopTag30Adapter focusCarAdapter = new TopTag30Adapter(a, tags, callback1);
        StaggeredGridLayoutManager staggeredGridLayoutManager = null;

        if(canBeShownInOnRow(tags, new Paint())){
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) rl_focusCar.getLayoutParams();
            AyoViewLib.setViewSize(rl_focusCar, lp2.width, LocalDisplay.dp2px(40));
        }else{
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
            ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) rl_focusCar.getLayoutParams();
            AyoViewLib.setViewSize(rl_focusCar, lp2.width, LocalDisplay.dp2px(80));
        }
        rl_focusCar.setLayoutManager(staggeredGridLayoutManager);
        rl_focusCar.setAdapter(focusCarAdapter);



//        ViewGroup.LayoutParams mParams = rl_focusCar.getLayoutParams();
//        mParams.height = Display.dip2px(50*2);
//        rl_focusCar.setLayoutParams(mParams);


        ////方式2：
//        final FixedRowSlider slider = (FixedRowSlider) contentView1.findViewById(R.id.slider);
//        slider.notifyDataSetChanged(tags, Display.screenWidth - Display.dip2px(20), new FixedRowSlider.Callback(){
//
//            @Override
//            public void onTagClicked(int position, Tag30 tag) {
//                if(tag.isSelected){
//                    selected.add(tag);
//                }else{
//                    selected.remove(tag);
//                }
//
//                if(Lang.isEmpty(selected)){
//                    tv_confirm.setText("不感兴趣");
//                    tv_label.setText(Html.fromHtml(textLabel.replace("num", 0+"")));
//                }else{
//                    tv_confirm.setText("确定");
//                    tv_label.setText(Html.fromHtml(textLabel.replace("num", Lang.count(selected)+"")));
//                }
//            }
//        });

        tv_confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toaster.toastShort("unlike " + selected.size());
                pop.dismiss();
            }
        });

        if(shouldShowBelow){
            pop.showAsDropDown(target, PopupWindowHelper.TYPE_MATCH_PARENT, 0, 0, R.style.Ayo_Popup_AnimationUpPopup3);
        }else{
            pop.showAsPopUp(target, Gravity.RIGHT|Gravity.BOTTOM, 0, 0, PopupWindowHelper.TYPE_MATCH_PARENT, R.style.Ayo_Popup_AnimationUpPopup4);
        }


    }



    public boolean canBeShownInOnRow(List<Tag30> tags, Paint paint){
        //每个tag有40dp的横向margin
        int sum = 0;
        for(Tag30 tag: tags){
            sum += textWidth(tag.tag_name, paint) + LocalDisplay.dp2px(40);
        }
        if(sum > LocalDisplay.SCREEN_WIDTH_DP - LocalDisplay.dp2px(20)){
            return false;
        }else{
            return true;
        }

    }

    public int textWidth(String text, Paint paint){
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(LocalDisplay.sp2px(getActivity(), 13));
        return rect.width();
    }

    public static class TopTag30Adapter extends RecyclerView.Adapter<ViewHolder>{

        Callback callback;
        List<Tag30> list;

        public TopTag30Adapter(Activity a, List<Tag30> list, Callback callback) {
            this.callback = callback;
            this.callback = callback;
            this.list = list;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_recommend, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder h, final int position) {
            final TextView tv_tag = (TextView) h.get(R.id.tv_tag);
            final LinearLayout ll_root = (LinearLayout) h.get(R.id.ll_root);

            final Tag30 t = list.get(position);

            tv_tag.setText(t.tag_name);
            // 还必须设置个param，否则每个控件占一行，奇怪
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) tv_tag.getLayoutParams();
            lp.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
            lp.height = LocalDisplay.dp2px(30);
            lp.rightMargin = LocalDisplay.dp2px(10);
            tv_tag.setLayoutParams(lp);

            tv_tag.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    t.isSelected = !t.isSelected;
                    if(t.isSelected){
                        ll_root.setBackgroundResource(R.drawable.bg_tag_recommend_selected);
                    }else{
                        ll_root.setBackgroundResource(R.drawable.bg_tag_recommend);
                    }
                    callback.onTagClicked(position, t);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }



    public static class Tag30{

        public String tag_id;
        public String tag_name;
        public boolean isSelected;

        @Override
        public boolean equals(Object o) {
            if(o == null) return false;
            if(!(o instanceof Tag30)) return false;
            if(this == o) return true;

            Tag30 o2 = (Tag30) o;
            if(this.tag_id.equals(o2.tag_id) && this.tag_name.equals(o2.tag_name)) return true;
            return false;
        }

        public Tag30(String tag_id, String tag_name) {
            this.tag_id = tag_id;
            this.tag_name = tag_name;
        }

        public Tag30() {
        }

        //不要改
        @Override
        public String toString() {
            return tag_id;
        }
    }

    public interface Callback{
        void onTagClicked(int position, Tag30 tag);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private SparseArray<View> viewHolder;
        private View view;

        public static ViewHolder getViewHolder(View view){
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }
            return viewHolder;
        }
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        public <T extends View> T get(int id) {
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }

        public View getConvertView() {
            return view;
        }

        public TextView getTextView(int id) {

            return get(id);
        }
        public Button getButton(int id) {

            return get(id);
        }

        public ImageView getImageView(int id) {
            return get(id);
        }

        public void setTextView(int  id,CharSequence charSequence){
            getTextView(id).setText(charSequence);
        }

    }
}
