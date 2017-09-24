package org.ayo.imagepicker;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by wl on 2017/06/11.
 * 本地视频
 */

public class VideoListActivity extends AppCompatActivity {

    public static void startForResult(Activity activity){
        Intent intent = new Intent(activity, VideoListActivity.class);
//        intent.putExtra(PIC_TOTAL_FLAG, limit);
        // image/png导致三星报错“相册停止运行” 改成image/PNG
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/PNG");
        activity.startActivityForResult(intent, MyImagePicker.RESULT_CODE_PICK_VIDEO);
    }

    public static List<ThumbModel> tryToHandleResult(Activity activity, int requestCode, int resultCode, Intent data){
        if(data != null && requestCode == MyImagePicker.RESULT_CODE_PICK_VIDEO){
            ArrayList<ThumbModel> list = (ArrayList<ThumbModel>)(data.hasExtra("data") ? data.getSerializableExtra("data") : null);
            return list;
        }
        return null;
    }

    private GridView mGridView;

    private List<ThumbModel> mVideoList = new ArrayList<>();

    private ArrayList<ThumbModel> mSelectThumbs = new ArrayList<>();

    private VideoGridViewAdapter mAdapter;


    public final static int DURATION_LIMIT = 5 * 60;

    //500MB
    public final static int SIZE_LIMIT = 1024 * 1024 * 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_video_alums_layout);
        initTitleView();

        mGridView = (GridView) findViewById(R.id.grid_layout);
        setAdapter();
        new SearchPhoto().start();

    }
    MyTitleView mTitleView;
    private void initTitleView() {
        mTitleView = (MyTitleView) findViewById(R.id.titlebar_layout);
        mTitleView.setLeftButton(R.drawable.picker_return_btn_style);
        mTitleView.setRightButton(getString(R.string.picker_cancel));
        mTitleView.setTitle(getString(R.string.picker_video_select));
        mTitleView.setTitleViewListener(new MyTitleView.BaseTitleViewListener() {
            @Override
            public void onLeftClicked() {
                finish();
            }

            @Override
            public void onTitleClicked() {
                super.onTitleClicked();
            }

            @Override
            public void onRightClicked() {
                super.onRightClicked();
                if (mSelectThumbs.size() > 0) {
                    Intent dataaa = new Intent();
                    dataaa.putExtra("data", mSelectThumbs);
                    setResult(Activity.RESULT_OK, dataaa);
//                    MyEventBus.getDefault().post(new ImagePickedEvent(mSelectThumbs));
                }
                finish();
            }
        });
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    public void setAdapter() {
        mAdapter = new VideoGridViewAdapter(this, mVideoList);
        mGridView.setAdapter(mAdapter);

        mAdapter.setOnImgSelectedListener(new VideoGridViewAdapter.OnImgSelectedListener() {
            @Override
            public void onSelected(ThumbModel model) {
                if (model == null || mVideoList == null || durationLimit(model)
                        || isFormatLimit(model))
                    return;
                for (ThumbModel m : mVideoList) {
                    m.setSelect(false);
                }

                if (contains(model)) {
                    mSelectThumbs.remove(model);
                    mTitleView.setRightButton(getString(R.string.picker_cancel));
                } else {
                    mSelectThumbs.clear();
                    mSelectThumbs.add(model);
                    model.setSelect(!model.isSelect());
                    mTitleView.setRightButton(getString(R.string.picker_complete));
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ThumbModel model = (ThumbModel) mGridView.getItemAtPosition(i);
                if (model == null) return;
                Uri uri = Uri.parse("file://" + model.getPath());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, model.getVideoType());
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    MyToaster.toastShort(getString(R.string.picker_player_empty));
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean durationLimit(ThumbModel model) {
        if (model.getDuration() > DURATION_LIMIT) {
            MyToaster.toastShort(getResources().getString(R.string.picker_video_limit_length));
            return true;
        }
        if (model.getVideoSize() > SIZE_LIMIT) {
            MyToaster.toastShort(getResources().getString(R.string.picker_video_limit_size));
            return true;
        }
        return false;
    }

    public boolean isFormatLimit(ThumbModel model) {
        if ("video/mp4".equals(model.getVideoType()))
            return false;
        MyToaster.toastShort(getResources().getString(R.string.picker_video_limit_format));
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x001) {
                mAdapter.notifyDataSetInvalidated();
                if (mVideoList.size() == 0) {
                } else {
                }
            }
        }
    };

    class SearchPhoto extends Thread {
        @Override
        public void run() {
            // 如果有sd卡（外部存储卡）
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(originalUri, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
                if (cursor == null) {
                    return;
                }
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    //获取当前Video对应的Id，然后根据该ID获取其缩略图的uri
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String[] selectionArgs = new String[]{id + ""};
                    String[] thumbColumns = new String[]{MediaStore.Video.Thumbnails.DATA,
                            MediaStore.Video.Thumbnails.VIDEO_ID};
                    String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";

                    String uri_thumb = "";
                    Cursor thumbCursor = (VideoListActivity.this.getApplicationContext().getContentResolver()).query(
                            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs,
                            null);

                    if (thumbCursor != null && thumbCursor.moveToFirst()) {
                        uri_thumb = thumbCursor
                                .getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
                    }

                    ThumbModel bitmapEntity = new ThumbModel();
                    bitmapEntity.setThumb(uri_thumb);
                    bitmapEntity.setVideoSize(size);
                    bitmapEntity.setPath(path);
                    bitmapEntity.setId(id);
                    bitmapEntity.setType(ThumbModel.ThumbType.LOCAL_VIDEOS);
                    bitmapEntity.setDuration(duration / 1000);
                    bitmapEntity.setFileName(title);
                    bitmapEntity.setVideoType(type);

                    mVideoList.add(bitmapEntity);

                }
                cursor.close();
                Collections.reverse(mVideoList);
                handler.sendEmptyMessage(0x001);
            }
        }
    }

    public boolean contains(ThumbModel model) {
        if (mSelectThumbs != null && mSelectThumbs.size() > 0) {
            for (ThumbModel thumbModel : mSelectThumbs) {
                if (model.getId() == thumbModel.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
