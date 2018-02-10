
package org.ayo.imagepicker;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class AlbumFolderAdapter extends RecyclerView.Adapter {
	private Context mContext;

	private List<AlbumFolderModel> mData;

	private View.OnClickListener mClickListener;

	public AlbumFolderAdapter(Context context, View.OnClickListener clickListener) {
		this.mContext = context;
		this.mClickListener = clickListener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.picker_item_album_folder_layout, null);
		view.setOnClickListener(mClickListener);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		AlbumFolderModel model = mData.get(position);
		if (model != null) {
			ViewHolder viewHolder = (ViewHolder) holder;
			viewHolder.tvName.setText(model.getDirName());
			viewHolder.tvNumber.setVisibility(View.VISIBLE);
			viewHolder.tvNumber.setText(mContext.getString(R.string.picker_album_num, model.getSize()));

			viewHolder.mBgItem.setPressed(model.isSelected());
			Glide.with(mContext).load(model.getFirstImgPath()).into(viewHolder.ivImg);
		}
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	public AlbumFolderModel getItem(int position) {
		if (position >= mData.size())
			return null;
		return mData.get(position);
	}

	public void setData(List<AlbumFolderModel> data) {
		if (data == null)
			data = new ArrayList<>();
		this.mData = data;
	}

	public class ViewHolder  extends RecyclerView.ViewHolder {
		ImageView ivImg;
		TextView tvName;
		TextView tvNumber;
		RelativeLayout mBgItem;

		public ViewHolder(View itemView) {
			super(itemView);
			ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
			tvName = (TextView) itemView.findViewById(R.id.tv_name);
			tvNumber = (TextView) itemView.findViewById(R.id.tv_num);
			mBgItem = (RelativeLayout) itemView.findViewById(R.id.folder_layout);
		}
	}

}
