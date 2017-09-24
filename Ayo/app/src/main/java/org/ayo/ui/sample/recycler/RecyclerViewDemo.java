package org.ayo.ui.sample.recycler;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.ayo.animate.yoyo.Techniques;
import org.ayo.animate.yoyo.YoYo;
import org.ayo.component.MasterFragment;
import org.ayo.core.Async;
import org.ayo.list.adapter.ItemBean;
import org.ayo.notify.toaster.Toaster;
import org.ayo.sample.R;
import org.ayo.ui.sample.SmartRadioGroup;
import org.ayo.ui.sample.recycler.divider.FlexibleDividerDecoration;
import org.ayo.ui.sample.recycler.divider.HorizontalDividerItemDecoration;
import org.ayo.ui.sample.recycler.divider.VerticalDividerItemDecoration;
import org.ayo.ui.sample.template.recycler.Data;
import org.ayo.ui.sample.template.recycler.model.Top;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/22 0022.
 */

public class RecyclerViewDemo extends MasterFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.frag_recycler_list;
    }

    @BindView(R.id.mRecyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.fl_setting) View fl_setting;
    @BindView(R.id.tv_scroll_info) TextView tv_scroll_info;
    @BindView(R.id.tv_setting) View tv_setting;
    @BindView(R.id.rg_layout) RadioGroup rg_layout;
    @BindView(R.id.rg_column) RadioGroup rg_column;
    @BindView(R.id.rg_oritation) RadioGroup rg_oritation;
    @BindView(R.id.rg_reverse) RadioGroup rg_reverse;
    @BindView(R.id.rg_docore)  SmartRadioGroup rg_docore;
    @BindView(R.id.tv_add)  TextView tv_add;
    @BindView(R.id.tv_remove)  TextView tv_remove;

    private boolean settingOpen = false;
    MyAdapter mAdapter;

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        ButterKnife.bind(this, view);

        Async.post(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.SlideOutUp).duration(500).delay(100).playOn(fl_setting);
            }
        }, 1000);

        tv_setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.SlideInDown).duration(500).playOn(fl_setting);
                settingOpen = true;
            }
        });

        fl_setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(settingOpen){
                    settingOpen = false;
                    YoYo.with(Techniques.SlideOutUp).duration(500).delay(1000).playOn(fl_setting);
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(settingOpen){
                    settingOpen = false;
                    YoYo.with(Techniques.SlideOutUp).duration(500).delay(1000).playOn(fl_setting);
                }
            }
        });

        rg_layout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_layout_linear){
                    layout = Config.layout_linear;
                }else if(checkedId == R.id.rb_layout_grid){
                    layout = Config.layout_grid;
                }else if(checkedId == R.id.rb_layout_stagger){
                    layout = Config.layout_stargger;
                }
                refresh();
            }
        });

        rg_column.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_column_2){
                    column = 2;
                }else if(checkedId == R.id.rb_column_3){
                    column = 3;
                }else if(checkedId == R.id.rb_column_5){
                    column = 5;
                }
                refresh();
            }
        });

        rg_oritation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_oritation_h){
                    oritation = Config.horizontal;
                }else if(checkedId == R.id.rb_oritation_v){
                    oritation = Config.vertical;
                }

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mRecyclerView.getLayoutParams();
                if(oritation == Config.horizontal){
                    lp.height = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
                }else{
                    lp.height = ViewGroup.MarginLayoutParams.MATCH_PARENT;
                }
                mRecyclerView.setLayoutParams(lp);

                refresh();
            }
        });

        rg_reverse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_reverse_true){
                    reverse = true;
                }else if(checkedId == R.id.rb_reverse_false){
                    reverse = false;
                }
                refresh();
            }
        });

        rg_docore.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                decore = checkedId;
                refresh();
            }
        });


        tv_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

               Top top = new Top();
                list.add(2, top);
                mRecyclerView.getAdapter().notifyItemInserted(2);
                Toaster.toastShort("添加到postion = 2");

            }
        });

        tv_remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.remove(3);
                mRecyclerView.getAdapter().notifyItemRemoved(3);
                Toaster.toastShort("从postion = 3移除");
            }
        });

    }


    public static class Config{
        public static final int layout_linear = 1;
        public static final int layout_grid = 2;
        public static final int layout_stargger = 3;
        public static final int layout_flow = 4;

        public static final int horizontal = 1;
        public static final int vertical = 2;

    }
    private int layout = Config.layout_linear;
    private int oritation = Config.vertical;
    private boolean reverse = false;
    private int column = 2;
    private int decore = 0;
    private List<ItemBean> list = Data.getTopList();
    private RecyclerView.ItemDecoration currentItemDecor, currentItemDecor2;

    private void refresh(){
        refreshList(getActivity(), mRecyclerView, layout, column, oritation, reverse, decore);
        collectVisibleInfo();
    }

    public void refreshList(Activity a, RecyclerView recyclerView, int layoutManager,
                                   int column, int oritation, boolean reverse, int decore){

        ///LayoutManager
        RecyclerView.LayoutManager lm = null;
        if(layoutManager == Config.layout_linear){
            int realOrientation = oritation == Config.horizontal ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL;
            lm = new LinearLayoutManager(a, realOrientation, reverse);
        }else if(layoutManager == Config.layout_grid){
            int realOrientation = oritation == Config.horizontal ? GridLayoutManager.HORIZONTAL : GridLayoutManager.VERTICAL;
            lm = new GridLayoutManager(a, column, realOrientation, reverse);
        }else if(layoutManager == Config.layout_stargger){
            int realOrientation = oritation == Config.horizontal ? StaggeredGridLayoutManager.HORIZONTAL : StaggeredGridLayoutManager.VERTICAL;
            lm = new StaggeredGridLayoutManager(column, realOrientation);
            ((StaggeredGridLayoutManager)lm).setReverseLayout(reverse);
        }
        recyclerView.setLayoutManager(lm);

        ///decore
        if(currentItemDecor != null) recyclerView.removeItemDecoration(currentItemDecor);
        if(currentItemDecor2 != null) recyclerView.removeItemDecoration(currentItemDecor2);
        if(decore == R.id.rb_decore_none){
            currentItemDecor = null;
            currentItemDecor2 = null;
        }else if(decore == R.id.rb_decore_linear_1){
            currentItemDecor = new LinearDividerItemDecoration(getActivity(),
                    oritation == Config.horizontal ? LinearDividerItemDecoration.HORIZONTAL_LIST : LinearDividerItemDecoration.VERTICAL_LIST,
                    getActivity().getResources().getDrawable(R.drawable.deivider_bg_linear_1));

            currentItemDecor2 = null;
        }else if(decore == R.id.rb_decore_linear_2){
            currentItemDecor = new LinearDividerItemDecoration(getActivity(),
                    oritation == Config.horizontal ? LinearDividerItemDecoration.HORIZONTAL_LIST : LinearDividerItemDecoration.VERTICAL_LIST,
                    getActivity().getResources().getDrawable(R.drawable.deivider_bg_linear_2));

            currentItemDecor2 = null;
        }else if(decore == R.id.rb_decore_grid_1){
            currentItemDecor = new GridDividerItemDecoration(getActivity(),
                    getActivity().getResources().getDrawable(R.drawable.deivider_bg_grid_1));

            currentItemDecor2 = null;
        }else if(decore == R.id.rb_decore_grid_2){
            currentItemDecor = new GridDividerItemDecoration(getActivity(),
                    getActivity().getResources().getDrawable(R.drawable.deivider_bg_grid_2));

            currentItemDecor2 = null;
        }else if(decore == R.id.rb_flex_linear_v){
            currentItemDecor = new HorizontalDividerItemDecoration.Builder(getActivity()).color(Color.YELLOW).size(10).build();

            currentItemDecor2 = null;
        }else if(decore == R.id.rb_flex_linear_h){
            currentItemDecor = new VerticalDividerItemDecoration.Builder(getActivity()).build();

            currentItemDecor2 = null;
        }else if(decore == R.id.rb_flex_grid_v){
            currentItemDecor = new HorizontalDividerItemDecoration.Builder(getActivity()).margin(40, 40).build();
            currentItemDecor2 = new VerticalDividerItemDecoration.Builder(getActivity()).margin(40, 40).build();
        }else if(decore == R.id.rb_flex_grid_h){
            currentItemDecor = new HorizontalDividerItemDecoration.Builder(getActivity()).margin(40, 40).build();
            currentItemDecor2 = new VerticalDividerItemDecoration.Builder(getActivity()).margin(40, 40).build();
        }else if(decore == R.id.rb_flex_stagger_v){
            currentItemDecor = new HorizontalDividerItemDecoration.Builder(getActivity()).margin(40, 40).build();
            currentItemDecor2 = new VerticalDividerItemDecoration.Builder(getActivity()).margin(40, 40).build();
        }else if(decore == R.id.rb_flex_stagger_h){
            currentItemDecor = new HorizontalDividerItemDecoration.Builder(getActivity()).margin(40, 40).build();
            currentItemDecor2 = new VerticalDividerItemDecoration.Builder(getActivity()).margin(40, 40).build();
        }else if(decore == R.id.rb_flex_paint){
            Paint paint = new Paint();
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLUE);
            paint.setAntiAlias(true);
            paint.setPathEffect(new DashPathEffect(new float[]{3.0f, 2f, 5.0f,7f}, 0.1f));
            currentItemDecor = new HorizontalDividerItemDecoration.Builder(getActivity())
                    .paint(paint)
                    .showLastDivider()
                    .build();
            currentItemDecor2 = null;
        }else if(decore == R.id.rb_flex_drawable){
            currentItemDecor = new HorizontalDividerItemDecoration.Builder(getActivity())
                    .drawable(R.drawable.sample_divider)
                    .size(15)
                    .build();
            currentItemDecor2 = null;
        }else if(decore == R.id.rb_flex_complex){
            ComplexDivider dividerAdapter = new ComplexDivider();
            currentItemDecor = new HorizontalDividerItemDecoration.Builder(getActivity())
                    .paintProvider(dividerAdapter)
                    .visibilityProvider(dividerAdapter)
                    .marginProvider(dividerAdapter)
                    .build();
            currentItemDecor2 = null;
        }else if(decore == R.id.rb_final){
            currentItemDecor = new FinalGridItemDecoration(getActivity(), false);
            currentItemDecor2 = null;
        }
        if(currentItemDecor != null) recyclerView.addItemDecoration(currentItemDecor);
        if(currentItemDecor2 != null) recyclerView.addItemDecoration(currentItemDecor2);

        ///adapter
        mAdapter = new MyAdapter(a, list, layoutManager);
        mAdapter.setOrientation(oritation);
        recyclerView.setAdapter(mAdapter);

        ///OnItemClick
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(settingOpen){
                    settingOpen = false;
                    YoYo.with(Techniques.SlideOutUp).duration(500).playOn(fl_setting);
                }
                Toaster.toastShort("click -- " + position);
            }
        });

        ///ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        ///Scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastScrollState = scrollState;
                scrollState = newState;
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    ///手指没在滑，列表页没在动
                }else if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    ///手指在拖动
                }else if(newState == RecyclerView.SCROLL_STATE_SETTLING){
                    ///手指已松开，但列表还在滑
                }
                collectVisibleInfo();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                scrollX += dx;
                scrollY += dy;

                collectVisibleInfo();
            }
        });
    }

    private void collectVisibleInfo(){
        RecyclerView.LayoutManager mLayoutManager = mRecyclerView.getLayoutManager();
        visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        if(mLayoutManager instanceof LinearLayoutManager){
            firstVisiblePosition = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

            if ( (visibleItemCount + firstVisiblePosition) >= totalItemCount) {
                lastItemVisible = true;
            }else{
                lastItemVisible = false;
            }

        }else if(mLayoutManager instanceof GridLayoutManager){
            firstVisiblePosition = ((GridLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

            if ( (visibleItemCount + firstVisiblePosition) >= totalItemCount) {
                lastItemVisible = true;
            }else{
                lastItemVisible = false;
            }
        }else if(mLayoutManager instanceof StaggeredGridLayoutManager){
            int[] spanFirstItemPostions = new int[((StaggeredGridLayoutManager)mLayoutManager).getSpanCount()];
            int[] spanVisibleCount = new int[((StaggeredGridLayoutManager)mLayoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager)mLayoutManager).findFirstVisibleItemPositions(spanFirstItemPostions);
            ((StaggeredGridLayoutManager)mLayoutManager).findLastVisibleItemPositions(spanVisibleCount);
            firstVisiblePosition = spanFirstItemPostions[0];

            int totalVisible = 0;
            for(int i = 0; i < spanFirstItemPostions.length; i++){
                Log.i("ddd", "First Item for span " + i + "：" + spanFirstItemPostions[i] + ",total visible: " + spanVisibleCount[i]);
                totalVisible += (spanVisibleCount[i] - spanFirstItemPostions[i] + 1);
            }
            if(totalVisible >= totalItemCount){
                lastItemVisible = true;
            }else{
                lastItemVisible = false;
            }
        }
        scrollState = mRecyclerView.getScrollState();
        log();
    }

    @Override
    protected void onDestroy2() {

    }

    private int firstVisiblePosition = 0;
    private int visibleItemCount = 0;
    private boolean lastItemVisible = false;

    private int lastScrollState, scrollState;
    private int scrollX, scrollY;

    private void log(){
        tv_scroll_info.setText("First: " + firstVisiblePosition
                + " , Visible Count: " + visibleItemCount
                + "\nLast is Visible：" + lastItemVisible
                + "\ngetScrollX, getScrollY: (" + mRecyclerView.getScrollX() + ", " + mRecyclerView.getScrollY() + ")"
                + "\ncurrent scroll: (" + scrollX + ", " + scrollY + ")"
                + "\nscroll state: (" + getScrollState(scrollState) + ")"
        )
        ;
        /*   http://blog.csdn.net/oaitan/article/details/51388358
        scoll state：
        SCROLL_STATE_IDLE   0
        SCROLL_STATE_DRAGGING   手指头在滑动时：1
        SCROLL_STATE_SETTLING   松手：如果松手时滑动已经停了，则还是1，   如果松手时还在继续滑，则是2
         */
    }

    String getScrollState(int state){
        if(state == RecyclerView.SCROLL_STATE_IDLE){
            return "RecyclerView.SCROLL_STATE_IDLE";
        }else if(state == RecyclerView.SCROLL_STATE_DRAGGING){
            return "RecyclerView.SCROLL_STATE_DRAGGING";
        }else if(state == RecyclerView.SCROLL_STATE_SETTLING){
            return "RecyclerView.SCROLL_STATE_SETTLING";
        }else{
            return "unknown";
        }
    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {
        if(b && b1){
            refresh();
        }
    }


    ///////////////////////////////



    ////////////////////////////////
    public static class LinearSpaceDevider extends RecyclerView.ItemDecoration{
        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }
    }



    ////////////////////////////////
    private static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);

        }

        public TextView tv;

    }

    private static class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        static String[] strs = {
                "1-哈哈哈哈哈-1",
                "1-呵呵呵呵呵-1",
                "1-哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呵呵呵呵呵而后-1"
        };

        private Activity mActivity;
        private List<ItemBean> list;
        private OnItemClickListener mItemClickListener;

        private int orientation = Config.vertical;

        public void setOrientation(int orientation){
            this.orientation = orientation;
        }

        //item的回调接口
        public interface OnItemClickListener{
            void onItemClick(View view,int Position);
        }
        //定义一个设置点击监听器的方法
        public void setOnItemClickListener(OnItemClickListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
        }

        public MyAdapter(Activity a, List<ItemBean> list, int layoutType){
            this.mActivity = a;
            this.list = list;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(orientation == Config.vertical ? R.layout.item_work_timeline : R.layout.item_work_timeline_horizontal, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null) mItemClickListener.onItemClick(holder.itemView, position);
                }
            });


            holder.tv.setText(position + "-----" + strs[position%3]);
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

    }

    private static class MyAdapter2 extends BaseRecyclerAdapter<ItemBean, MyViewHolder>{

        static String[] strs = {
                "1-哈哈哈哈哈-1",
                "1-呵呵呵呵呵-1",
                "1-哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呵呵呵呵呵而后-1"
        };

        private Activity mActivity;
        private List<ItemBean> list;
        private OnItemClickListener mItemClickListener;

        private int orientation = Config.vertical;

        public void setOrientation(int orientation){
            this.orientation = orientation;
        }

        //item的回调接口
        public interface OnItemClickListener{
            void onItemClick(View view,int Position);
        }
        //定义一个设置点击监听器的方法
        public void setOnItemClickListener(OnItemClickListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
        }

        public MyAdapter2(Activity a, List<ItemBean> list, int layoutType){
            this.mActivity = a;
            this.list = list;
            addDatas(list);
        }


        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public MyViewHolder onCreate(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(orientation == Config.vertical ? R.layout.item_work_timeline : R.layout.item_work_timeline_horizontal, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBind(final MyViewHolder holder, final int position, ItemBean data) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null) mItemClickListener.onItemClick(holder.itemView, position);
                }
            });


            holder.tv.setText(position + "-----" + strs[position%3]);
        }

        @Override
        protected MyViewHolder createHolder(View itemView) {
            return new MyViewHolder(itemView);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if(settingOpen){
            settingOpen = false;
            YoYo.with(Techniques.SlideOutUp).duration(500).playOn(fl_setting);
            return true;
        }else{
            return super.onBackPressedSupport();
        }
    }


    //////////////////////////////////////////////
    public static class ComplexDivider implements
            FlexibleDividerDecoration.PaintProvider,
//        FlexibleDividerDecoration.SizeProvider,
//        FlexibleDividerDecoration.ColorProvider,
            FlexibleDividerDecoration.VisibilityProvider,
            HorizontalDividerItemDecoration.MarginProvider{

        @Override
        public Paint dividerPaint(int position, RecyclerView parent) {
            Paint paint = new Paint();
            switch (position % 10) {
                case 0:
                    paint.setColor(Color.RED);
                    paint.setStrokeWidth(30);
                    break;
                case 1:
                    paint.setColor(Color.MAGENTA);
                    paint.setStrokeWidth(10);
                    break;
                default:
                    if (position % 2 == 0) {
                        paint.setColor(Color.BLUE);
                        paint.setAntiAlias(true);
                        paint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
                    } else {
                        paint.setColor(Color.GREEN);

                    }
                    paint.setStrokeWidth(2 + position);
                    break;
            }

            return paint;
        }

        @Override
        public boolean shouldHideDivider(int position, RecyclerView parent) {
            if (position == 14 || position == 15) {
                return true;
            }
            return false;
        }

        @Override
        public int dividerLeftMargin(int position, RecyclerView parent) {
            if (position < 10) {
                return position * 20;
            } else {
                return (20 - position) * 20;
            }
        }

        @Override
        public int dividerRightMargin(int position, RecyclerView parent) {
            if (position < 10) {
                return position * 20 + 20;
            } else {
                return (20 - position) * 20 + 20;
            }
        }
    }


}
