package org.ayo.list.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 最原始的Adapter，只是基于AyoViewHolder对原生adapter的封装
 */
public class AyoSoloAdapter2<T extends ItemBean> extends BaseAdapter {

    protected List<T> mList;
    protected Activity mActivity;
    private AdapterDelegatesManager<T> delegatesManager;

    public AyoSoloAdapter2(Activity activity, List<AyoItemTemplate<T>> templates) {

        this.mActivity = activity;

        // Delegates
        delegatesManager = new AdapterDelegatesManager<>();
        if(templates != null){
            for(AyoItemTemplate template: templates){
                template.attachToActivity(mActivity);
                delegatesManager.addDelegate(template);
            }
        }
        delegatesManager.addDelegate(new GuardItemTemplate(activity));
    }


    @Override
    public int getItemViewType(int position) {
        int type = delegatesManager.getItemViewType(mList.get(position), position);
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return delegatesManager.delegates.size();
    }

    public AyoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AyoViewHolder h = delegatesManager.onCreateViewHolder(null, viewType);
//        h.root().setTag(h);
        return h;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    public void notifyDataSetChanged(List<T> list){
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AyoViewHolder v = null;
        int itemType = getItemViewType(position);
        if(convertView == null){
            v = onCreateViewHolder(null, itemType);
        }else{
            v = (AyoViewHolder) convertView.getTag();
            if(v == null){
                v = onCreateViewHolder(null, itemType);
            }else{
            }
        }
        T t = mList.get(position);
        delegatesManager.onBindViewHolder(t, position, v, itemType);
        return v.root();
    }

}
