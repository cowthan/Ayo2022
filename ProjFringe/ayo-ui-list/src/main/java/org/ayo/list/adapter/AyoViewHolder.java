package org.ayo.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 */
public class AyoViewHolder extends RecyclerView.ViewHolder {
    public AyoViewHolder(View itemView) {
        super(itemView);
        viewHolder = new SparseArray<>();
		view = itemView;
    }
	private SparseArray<View> viewHolder;
	private View view;

	public View findViewById(int id){
		View holdedView = viewHolder.get(id);
		if (holdedView == null) {
			holdedView = view.findViewById(id);
			viewHolder.put(id, holdedView);
		}
		return holdedView;
	}

	public <T> T id(int id){
		return (T) findViewById(id);
	}


	public View root(){
		return view;
	}

}
