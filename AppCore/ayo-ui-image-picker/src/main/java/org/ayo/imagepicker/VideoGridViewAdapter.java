
package org.ayo.imagepicker;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class VideoGridViewAdapter extends BaseAdapter {

    protected Context context;

    protected LayoutInflater mInflater;

    protected List<ThumbModel> mData = new ArrayList<>();

    private OnImgSelectedListener onImgSelectedListener;

    public VideoGridViewAdapter(Context context, List<ThumbModel> data) {
        this.context = context;
        this.mData = data;
        mInflater = LayoutInflater.from(this.context);
    }

    public void setOnImgSelectedListener(OnImgSelectedListener onImgSelectedListener) {
        this.onImgSelectedListener = onImgSelectedListener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final ThumbModel model = mData.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.picker_item_video_local_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivImg = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
            viewHolder.vGrayBg = convertView.findViewById(R.id.cover_view);
            viewHolder.mDuration = (TextView) convertView.findViewById(R.id.tv_duration);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(Uri.fromFile(new File(model.getPath()))).into(viewHolder.ivImg);

        if (model.isSelect()) {
            viewHolder.ivSelected.setSelected(true);
            viewHolder.vGrayBg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivSelected.setSelected(false);
            viewHolder.vGrayBg.setVisibility(View.GONE);
        }
        try {
            viewHolder.mDuration.setText(model.getDuration() != 0
                    ? TimeUtils.longToString(model.getDuration() * 1000, "mm:ss")
                    : context.getString(R.string.picker_un_know));
        } catch (ParseException e) {
            viewHolder.mDuration.setText(context.getString(R.string.picker_un_know));
            e.printStackTrace();
        }

        viewHolder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onImgSelectedListener != null) {
                    onImgSelectedListener.onSelected(model);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView ivImg;
        ImageView ivSelected;
        View vGrayBg;
        TextView mDuration;

        public ViewHolder() {
        }
    }

    public interface OnImgSelectedListener {
        void onSelected(ThumbModel model);
    }

}
