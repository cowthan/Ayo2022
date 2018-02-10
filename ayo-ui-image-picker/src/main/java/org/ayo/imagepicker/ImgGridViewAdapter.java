
package org.ayo.imagepicker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class ImgGridViewAdapter extends BaseAdapter {

	protected Context mContext;

	protected LayoutInflater mInflater;

	protected List<ThumbModel> mData = new ArrayList<>();

	private boolean isAddPath;

	private String pPath = "";

	private OnImgSelectedListener onImgSelectedListener;

	public ImgGridViewAdapter(Context context, List<ThumbModel> data) {
		this.mContext = context;
		this.mData = data;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setOnImgSelectedListener(OnImgSelectedListener onImgSelectedListener) {
		this.onImgSelectedListener = onImgSelectedListener;
	}

	public void setPPath(String path) {
		this.pPath = path;
	}

	public void setAddPath(boolean addPath) {
		this.isAddPath = addPath;
	}

	public void setData(List<ThumbModel> data) {
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public ThumbModel getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.picker_item_img_adapter_layout, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ivImg = (ImageView) convertView.findViewById(R.id.iv_img);
			viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
			viewHolder.bgCoverView = convertView.findViewById(R.id.cover_view);
			viewHolder.mCameraLayout = (RelativeLayout) convertView.findViewById(R.id.camera_layout);
			viewHolder.mPhotoLayout = (RelativeLayout) convertView.findViewById(R.id.photo_layout);
			viewHolder.mSelectLayout = (RelativeLayout) convertView.findViewById(R.id.rl_select);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final ThumbModel imgBean = mData.get(position);

		if (ThumbModel.ThumbType.CAMERA == imgBean.getType()) {
			viewHolder.mPhotoLayout.setVisibility(View.GONE);
			viewHolder.mCameraLayout.setVisibility(View.VISIBLE);
			viewHolder.ivImg.setImageResource(0);
			viewHolder.ivImg.setBackgroundColor(0);
		} else {
			viewHolder.mPhotoLayout.setVisibility(View.VISIBLE);
			viewHolder.mCameraLayout.setVisibility(View.GONE);
			if(!isAddPath) {
				if (imgBean.getPath().endsWith(".gif")) {
					Glide.with(mContext).load(imgBean.getPath()).asGif().
							diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.ivImg);
				} else {
					Glide.with(mContext).load(imgBean.getPath()).into(viewHolder.ivImg);
				}
			} else {
				Glide.with(mContext).load(pPath + "/" + imgBean.getPath()).into(viewHolder.ivImg);
			}

			if(imgBean.isSelect()) {
				viewHolder.ivSelected.setSelected(true);
				viewHolder.bgCoverView.setVisibility(View.VISIBLE);
			} else {
				viewHolder.ivSelected.setSelected(false);
				viewHolder.bgCoverView.setVisibility(View.GONE);
			}

			viewHolder.mSelectLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (onImgSelectedListener != null) {
						onImgSelectedListener.onSelected(imgBean);
					}
				}
			});
		}

		return convertView;
	}

	public class ViewHolder {
		ImageView ivImg;
		ImageView ivSelected;
		View bgCoverView;
		RelativeLayout mCameraLayout;
		RelativeLayout mPhotoLayout;
		RelativeLayout mSelectLayout;

		public ViewHolder(){}
	}

	interface OnImgSelectedListener {
		void onSelected(ThumbModel model);
	}

}
