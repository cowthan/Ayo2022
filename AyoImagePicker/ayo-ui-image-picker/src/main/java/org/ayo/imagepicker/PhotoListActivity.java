package org.ayo.imagepicker;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import org.ayo.imagepicker.album.PhotoShowActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * Created by wl on 2017/06/11.
 * 相册
 */

public class PhotoListActivity extends AppCompatActivity implements AlbumFolderView.OnFolderSelectListener,
        View.OnClickListener {

    public static void startForResult(Activity activity, int limit){
        Intent intent = new Intent(activity, PhotoListActivity.class);
        intent.putExtra(PIC_TOTAL_FLAG, limit);
        // image/png导致三星报错“相册停止运行” 改成image/PNG
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/PNG");
        activity.startActivityForResult(intent, MyImagePicker.RESULT_CODE_PICK_PIC);
    }

    public static List<ThumbModel> tryToHandleResult(Activity activity, int requestCode, int resultCode, Intent data){
        if(data != null && requestCode == MyImagePicker.RESULT_CODE_PICK_PIC){
            ArrayList<ThumbModel> list = (ArrayList<ThumbModel>)(data.hasExtra("data") ? data.getSerializableExtra("data") : null);
            return list;
        }
        return null;
    }


    private GridView mGridView;

    //相册分类文件夹
    private ArrayList<AlbumFolderModel> mFolders;

    private ArrayList<ThumbModel> mAlbumData;

    private List<ThumbModel> mTempAlbumData;

    private ArrayList<ThumbModel> mSelectedAlbumData;

    private ImgGridViewAdapter mGridAdapter;

    private ViewStub mViewStub;

    private RelativeLayout mSelectFolderLayout;

    private AlbumFolderView mAlbumFolderView;

    private int mMaxCount = 6;

    private int mTitleDrawablePadding;

    private boolean mFolderShow;


    /**
     * 保存的文件路径
     */
    private String mFileName;

    public static final int REQUEST_PHOTO = 1;

    /**
     * 限制上传图片总数key
     */
    public final static String PIC_TOTAL_FLAG = "pic_total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_photo_list_layout);
        MyEventBus.getDefault().register(this);
        if(getIntent().hasExtra(PIC_TOTAL_FLAG)){
            mMaxCount = getIntent().getIntExtra(PIC_TOTAL_FLAG, mMaxCount);
        }
        mTitleDrawablePadding = MyUtils.dip2px(12);
        mAlbumData = new ArrayList<>();
        mTempAlbumData = new ArrayList<>();
        mSelectedAlbumData = new ArrayList<>();
        mFolders = new ArrayList<>();
        initTitleView();
        mGridView = (GridView) findViewById(R.id.grid_view);
        mViewStub = (ViewStub) findViewById(R.id.pop_layout);
        mSelectFolderLayout = (RelativeLayout) findViewById(R.id.select_pop_layout);
        mSelectFolderLayout.setOnClickListener(this);
        setupArrow(false);

        setAdapter();
        scanAlums();

//        MyEventBus.getDefault().post(new GalleryDataEvent());

    }
    MyTitleView mTitleView;
    private void initTitleView() {

        mTitleView = (MyTitleView) findViewById(R.id.titlebar_layout);
        mTitleView.setLeftButton(R.drawable.picker_return_btn_style);
        completeViewUpdate();
        mTitleView.setTitle(getString(R.string.picker_all_pic));
        mTitleView.setTitleViewListener(new MyTitleView.BaseTitleViewListener() {
            @Override
            public void onLeftClicked() {
                finish();
            }

            @Override
            public void onTitleClicked() {
                super.onTitleClicked();
                if (mAlbumFolderView == null) {
                    View view = mViewStub.inflate();
                    mAlbumFolderView = (AlbumFolderView) view.findViewById(R.id.pop_view);
                    int height = (int)(MyUtils.getScreenHeight() * 0.50f);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.
                            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                    lp.setMargins(0, 0, 0, MyUtils.dip2px(50));
                    mAlbumFolderView.setLayoutParams(lp);
                    mAlbumFolderView.setupView();
                    mAlbumFolderView.setData(mFolders);
                    mAlbumFolderView.setFolderSelectListener(PhotoListActivity.this);
                    mAlbumFolderView.show();
                    showFolder();
                } else {
                    if (mFolderShow) {
                        mAlbumFolderView.hide();
                        hideFolder();
                    } else {
                        mAlbumFolderView.show();
                        showFolder();
                    }
                }

            }

            @Override
            public void onRightClicked() {
                super.onRightClicked();
                if (mSelectedAlbumData.size() > 0) {
                    Intent data = new Intent();
                    data.putExtra("data", mSelectedAlbumData);
                    setResult(Activity.RESULT_OK, data);
//                    MyEventBus.getDefault().post(new ImagePickedEvent(mSelectedAlbumData));
                }
                finish();
            }
        });
    }

    private void showFolder() {
        mFolderShow = true;
        mSelectFolderLayout.setVisibility(View.VISIBLE);
        setupArrow(true);
    }

    private void hideFolder() {
        mFolderShow = false;
        mSelectFolderLayout.setVisibility(View.GONE);
        setupArrow(false);
        mAlbumFolderView.hide();
    }

    private void setupArrow(boolean up) {
        Drawable draw;
        if (up) {
            draw = getResources().getDrawable(R.drawable.picker_icon_arrow_up_bitmap);
        } else {
            draw = getResources().getDrawable(R.drawable.picker_icon_arrow_down_bitmap);
        }
        draw.setBounds(0, 0, mTitleDrawablePadding, mTitleDrawablePadding);
        mTitleView.getTitle().setCompoundDrawables(null, null, draw, null);
        mTitleView.getTitle().setCompoundDrawablePadding(10);
    }

    public void setAdapter() {
        mGridAdapter = new ImgGridViewAdapter(this, mAlbumData);
        mGridView.setAdapter(mGridAdapter);

        mGridAdapter.setOnImgSelectedListener(new ImgGridViewAdapter.OnImgSelectedListener() {
            @Override
            public void onSelected(ThumbModel imgBean) {
                if (imgBean == null) return;
                int state = MyUtils.canImageUpload(imgBean.path);
                if (state != 0) {
                    String text = state == 4 ? (getString(R.string.picker_not_supportupload)
                            + MyImagePicker.UPLOAD_LIMIT_M + getString(R.string.picker_mb_gifpic))
                            : (state == 5 ? getString(R.string.picker_not_supportupload1280)
                            : getString(R.string.picker_useless_pic));
                    MyToaster.toastLong(text);
                    return;
                }

                if (mSelectedAlbumData.size() < mMaxCount) {
                    imgBean.setSelect(!imgBean.isSelect());
                    if (imgBean.isSelect()) {
                        if (mMaxCount == 1) {
                            for (ThumbModel imb : mSelectedAlbumData) {
                                imb.setSelect(false);
                            }
                            mSelectedAlbumData.clear();
                            mSelectedAlbumData.add(imgBean);
                        } else {
                            if (!hasPhoto(imgBean)) {
                                mSelectedAlbumData.add(imgBean);
                            }
                        }
                    } else {
                        int pos = hasPhoto(imgBean, -1);
                        if (pos != -1) {
                            mSelectedAlbumData.remove(pos);
                            completeViewUpdate();
                        }
                    }
                    mGridAdapter.notifyDataSetChanged();
                } else {
                    if (imgBean.isSelect()) {
                        imgBean.setSelect(false);
                        int pos = hasPhoto(imgBean, -1);
                        if (pos != -1) {
                            mSelectedAlbumData.remove(pos);
                        }
                    } else {
                        MyToaster.toastShort(getString(R.string.picker_max_pic, mMaxCount));
                    }
                    mGridAdapter.notifyDataSetChanged();
                }
                completeViewUpdate();
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ThumbModel model = (ThumbModel) mGridView.getItemAtPosition(i);
                if (model == null) return;
                if (model.getType() == ThumbModel.ThumbType.CAMERA) {
                    if (mSelectedAlbumData.size() >= mMaxCount) {
                        MyToaster.toastShort(getString(R.string.picker_max_pic, mMaxCount));
                    } else {
                        startPhotoActivity();
                    }
                } else {
                    //TODO:浏览本地相册，并且可以选择
                    scanAlbum(i - 1);
                }
            }
        });
    }

    public void completeViewUpdate() {
        if (mSelectedAlbumData.size() == 0) {
            mTitleView.setRightButton(getString(R.string.picker_cancel));
        } else {
            mTitleView.setRightButton(getString(R.string.picker_finish_num,
                    mSelectedAlbumData.size() + "/" + mMaxCount));
        }
    }

    public void scanAlums() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            MyToaster.toastShort(getString(R.string.picker_sd_card_err));
            return;
        }
        new Thread() {
            @Override
            public void run() {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(uri, null, MediaStore.Images.Media.MIME_TYPE +
                                " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ? or " +
                                MediaStore.Images.Media.MIME_TYPE + " = ? or " +
                                MediaStore.Images.Media.MIME_TYPE + " = ? ",
                        new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                if (cursor == null) return;

                Set<String> dirPaths = new HashSet<>();
                int photoTotal = 0;
                int count = cursor.getCount();
                for (int i = count - 1; i >= 0; i--) {
                    cursor.moveToPosition(i);
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    if (i == count - 1) {
                        AlbumFolderModel imgFolder = new AlbumFolderModel();
                        imgFolder.setFirstImgPath(path);
                        imgFolder.setDirName(getString(R.string.picker_all_pic));
                        imgFolder.setType(0);
                        imgFolder.setSelected(true);
                        mFolders.add(imgFolder);
                    }

                    ThumbModel model = new ThumbModel();
                    model.setPath(path);
                    model.setSelect(false);
                    model.setType(ThumbModel.ThumbType.ALBUM);
                    mAlbumData.add(model);
                    mTempAlbumData.add(model);

                    File parentFile = new File(path).getParentFile();

                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    AlbumFolderModel imgFolderBean;

                    if (dirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        dirPaths.add(dirPath);
                        imgFolderBean = new AlbumFolderModel();
                        imgFolderBean.setDir(dirPath);
                        imgFolderBean.setFirstImgPath(path);
                        imgFolderBean.setDirName(parentFile.getName());
                        imgFolderBean.setType(1);
                        imgFolderBean.setSelected(false);
                        mFolders.add(imgFolderBean);
                    }

                    if (parentFile.list() == null)
                        continue;

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            return filter(s);
                        }
                    }).length;
                    photoTotal += picSize;
                    imgFolderBean.setSize(picSize);
                }
                cursor.close();
                if (mFolders.size() > 0)
                    mFolders.get(0).setSize(photoTotal);
                handleAlbumData();
                Message message = Message.obtain();
                message.what = 0x001;
                handler.sendMessage(message);
            }
        }.start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x001) {
                mGridAdapter.setAddPath(false);
                if (mAlbumData.size() == 0) {
                    ThumbModel model = new ThumbModel();
                    model.setType(ThumbModel.ThumbType.CAMERA);
                    mAlbumData.add(0, model);
                } else {
                }
                mGridAdapter.notifyDataSetInvalidated();
            }
        }
    };

    private boolean filter(String s) {
        return s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".png") || s.endsWith("gif");
    }

    private void scanChildDirPaths(String dirPath) {
        File file = new File(dirPath);
        String[] childFilePaths = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return filter(s);
            }
        });

        if (childFilePaths != null) {
            mGridAdapter.setAddPath(false);
            mGridAdapter.setPPath(dirPath);
            mAlbumData.clear();
            for (String childFilePath : childFilePaths) {
                ThumbModel imgBean = new ThumbModel();
                imgBean.setPath(dirPath + "/" + childFilePath);
                mAlbumData.add(imgBean);
            }
            Collections.reverse(mAlbumData);
        } else {
            mAlbumData.clear();
        }

        handleAlbumData();
        mGridAdapter.notifyDataSetInvalidated();

    }

    @Override
    public void onSelect(int position, AlbumFolderModel model) {
        if (model == null) return;
        mTitleView.setTitle(TextUtils.isEmpty(model.getDirName()) ? "" : model.getDirName());

        hideFolder();
        if (model.getType() == 0) {
            mAlbumData.clear();
            mAlbumData.addAll(mTempAlbumData);
            handleAlbumData();
            mGridAdapter.setAddPath(false);
            mGridAdapter.notifyDataSetInvalidated();
        } else {
            scanChildDirPaths(model.getDir());
        }
    }

    @Override
    public void onClick(View view) {
        mAlbumFolderView.hide();
        hideFolder();
    }

    /**
     * 相机位于相册的第一位
     */
    private void handleAlbumData() {
        if (mAlbumData != null) {
            for (ThumbModel m : mAlbumData) {
                m.setSelect(hasPhoto(m));
            }

            ThumbModel model = new ThumbModel();
            model.setType(ThumbModel.ThumbType.CAMERA);
            mAlbumData.add(0, model);
        }
    }

    private boolean hasPhoto(ThumbModel model) {
        return hasPhoto(model, -1) != -1;
    }

    private int hasPhoto(ThumbModel model, int defaultPos) {
        if (model == null) return defaultPos;
        if (mSelectedAlbumData.size() > 0) {
            for (int i = 0, j = mSelectedAlbumData.size(); i < j; i++) {
                ThumbModel m = mSelectedAlbumData.get(i);
                if (m == null || TextUtils.isEmpty(m.getPath())) continue;
                if (m.getPath().equals(model.getPath())) {
                    return i;
                }
            }
        }
        return defaultPos;
    }

    private void scanAlbum(int position) {
        Intent intent = PhotoShowActivity.getIntent(this, mAlbumData,
                position, mMaxCount, mSelectedAlbumData.size());
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    /**
     * 相机拍照
     */
    private void startPhotoActivity() {
        String name = Long.toString(System.currentTimeMillis()) + ".jpg";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + "/dongqiudi/";
        mFileName  = path + name;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        File destination = new File(mFileName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, MyImagePicker.RESULT_CODE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyImagePicker.RESULT_CODE_PHOTO && resultCode == RESULT_OK) {
            if (mFileName != null) {
                ThumbModel model = new ThumbModel();
                model.setPath(mFileName);
                mSelectedAlbumData.add(model);
                updateGallery(mFileName);
            }
            if(mSelectedAlbumData != null && mSelectedAlbumData.size() > 0){
                Intent dataaa = new Intent();
                dataaa.putExtra("data", mSelectedAlbumData);
                setResult(Activity.RESULT_OK, dataaa);
            }
//            MyEventBus.getDefault().post(new ImagePickedEvent(mSelectedAlbumData));
            finish();
        } else if (requestCode == REQUEST_PHOTO && data != null) {
            mAlbumData = data.getParcelableArrayListExtra(PhotoShowActivity.EXTRA_ALBUM_PATHS);
            updateSelectAlbumData();
            if (resultCode == RESULT_CANCELED) {
                if (mAlbumData != null) {
                    ThumbModel model = new ThumbModel();
                    model.setType(ThumbModel.ThumbType.CAMERA);
                    mAlbumData.add(0, model);
                    mGridAdapter.setData(mAlbumData);
                    mGridAdapter.notifyDataSetChanged();
                } else {
                    mAlbumData = new ArrayList<>();
                }
            } else if (resultCode == RESULT_OK) {
                if(mSelectedAlbumData != null && mSelectedAlbumData.size() > 0){
                    Intent dataaa = new Intent();
                    dataaa.putExtra("data", mSelectedAlbumData);
                    setResult(Activity.RESULT_OK, dataaa);
                }
//                MyEventBus.getDefault().post(new ImagePickedEvent(mSelectedAlbumData));
                finish();
            }
        }
    }

    private void updateSelectAlbumData() {
        for (ThumbModel model : mAlbumData) {
            if (model == null) continue;
            int position = hasPhoto(model, -1);
            if (position != -1) {
                if (model.isSelect()) {
                    mSelectedAlbumData.get(position).setSelect(true);
                } else {
                    mSelectedAlbumData.remove(position);
                }
            } else if (model.isSelect()) {
                mSelectedAlbumData.add(model);
            }
        }
        completeViewUpdate();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyEventBus.getDefault().unregister(this);
    }


    private void updateGallery(String filename)//filename是我们的文件全名
    {
        MediaScannerConnection.scanFile(this,
                new String[] { filename }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

}
