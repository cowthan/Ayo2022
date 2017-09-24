package org.ayo.imagepicker.album;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.ayo.imagepicker.R;
import org.ayo.imagepicker.ThumbModel;
import org.ayo.imagepicker.photo.PhotoView;

/**
 * Created by wl on 2017/6/17
 * 图片滑动选择
 */
public class PhotoSlideFragment extends Fragment implements View.OnClickListener {
    private ThumbModel mThumbModel;

//    private PhotoViewAttacher mAttach;

    private PhotoView mPhotoView;

    private ImageView mSelectPhotoView;

    public static final String EXTRA_ALBUM_PATH = "extra_album_path";

    public static PhotoSlideFragment newInstance(ThumbModel models) {
        PhotoSlideFragment f = new PhotoSlideFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ALBUM_PATH, models);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mThumbModel = getArguments().getParcelable(EXTRA_ALBUM_PATH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.picker_fr_photo_slide, container, false);
        mPhotoView = (PhotoView) v.findViewById(R.id.iv_main_pic);
        mSelectPhotoView = (ImageView) v.findViewById(R.id.iv_selected);
        mSelectPhotoView.setSelected(mThumbModel.isSelect());
        mSelectPhotoView.setOnClickListener(this);
        if (mThumbModel.getPath().endsWith(".gif")) {
            Glide.with(getContext()).load(mThumbModel.getPath()).asGif().
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mPhotoView);
        } else {
            Glide.with(getContext()).load(mThumbModel.getPath()).into(mPhotoView);
        }
        return v;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_selected){
            mSelectPhotoView.setSelected(!mThumbModel.isSelect());
            ((PhotoShowActivity) getActivity()).setSelected(!mThumbModel.isSelect());
        }
    }
}
