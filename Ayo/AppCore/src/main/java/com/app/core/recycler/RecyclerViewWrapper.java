package com.app.core.recycler;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.core.recycler.divider.HorizontalDividerItemDecoration;
import com.app.core.recycler.divider.VerticalDividerItemDecoration;

import org.ayo.list.adapter.AyoItemTemplate;
import org.ayo.list.adapter.AyoSoloAdapter;
import org.ayo.list.adapter.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoliang on 2017/10/5.
 *
 * RecyclerView模板代码封装
 */

public class RecyclerViewWrapper<T extends ItemBean> {

    private RecyclerView recyclerView;
    private AyoSoloAdapter<T> adapter;
    private List<T> data;
    private Activity activity;

    private RecyclerViewWrapper(){}

    public static RecyclerViewWrapper from(Activity activity, RecyclerView recyclerView) {
        RecyclerViewWrapper r = new RecyclerViewWrapper();
        r.recyclerView = recyclerView;
        r.activity = activity;
        LinearLayoutManager lm = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(lm);
        return r;
    }

    public RecyclerViewWrapper layout(RecyclerView.LayoutManager layoutManager){
        recyclerView.setLayoutManager(layoutManager);
        return this;
    }

    public RecyclerViewWrapper adapter(List<AyoItemTemplate<T>> templates){
        adapter = new AyoSoloAdapter<>(activity, templates);
        recyclerView.setAdapter(adapter);
        return this;
    }

    public RecyclerViewWrapper adapter(AyoItemTemplate<T>...templates){
        List<AyoItemTemplate<T>> list = new ArrayList<>();
        for(AyoItemTemplate<T> t: templates){
            list.add(t);
        }
        adapter = new AyoSoloAdapter<>(activity, list);
        recyclerView.setAdapter(adapter);
        return this;
    }

    private HorizontalDividerItemDecoration mDividerHorizontal;
    private VerticalDividerItemDecoration mDividerVertical;
    public RecyclerViewWrapper dividerHorizontal(int horizontalPx){
        if(mDividerHorizontal != null){
            recyclerView.removeItemDecoration(mDividerHorizontal);
        }

        if(horizontalPx == -1){
            return this;
        }
        mDividerHorizontal = new HorizontalDividerItemDecoration.Builder(
                activity).drawable(new ColorDrawable(Color.TRANSPARENT))
                .size(horizontalPx).build();
        recyclerView.addItemDecoration(mDividerHorizontal);
        return this;
    }

    public RecyclerViewWrapper dividerVertical(int horizontalPx){
        if(mDividerVertical != null){
            recyclerView.removeItemDecoration(mDividerVertical);
        }

        if(horizontalPx == -1){
            return this;
        }
        mDividerVertical = new VerticalDividerItemDecoration.Builder(
                activity).drawable(new ColorDrawable(Color.TRANSPARENT))
                .size(horizontalPx).build();
        recyclerView.addItemDecoration(mDividerVertical);
        return this;
    }

    public RecyclerViewWrapper notifyDataSetChanged(List<T> list){
        this.data = list;
        adapter.notifyDataSetChanged(this.data);
        return this;
    }

    public static LinearLayoutManager newLinearHorizontal(Activity activity){
        LinearLayoutManager m = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        return m;
    }

    public static LinearLayoutManager newLinearVertical(Activity activity){
        LinearLayoutManager m = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        return m;
    }

    public static GridLayoutManager newGridVertical(Activity activity, int column){
        GridLayoutManager m = new GridLayoutManager(activity, column, GridLayoutManager.VERTICAL, false);
        return m;
    }

    public static GridLayoutManager newGridHorizontal(Activity activity, int column){
        GridLayoutManager m = new GridLayoutManager(activity, column, GridLayoutManager.HORIZONTAL, false);
        return m;
    }
}
