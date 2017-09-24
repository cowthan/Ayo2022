
package org.ayo.editor;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.ayo.editor.controller.EnterControlView;
import org.ayo.editor.editor.RichTextEditorView;
import org.ayo.editor.emoj.engine.EmojiUtils;
import org.ayo.editor.prompt.CreateThreadDialog;
import org.ayo.editor.prompt.RemindDialog;
import org.ayo.imagepicker.PhotoListActivity;
import org.ayo.imagepicker.ThumbModel;
import org.ayo.imagepicker.VideoListActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * Created by hujinghui on 16/3/29.
 */
public abstract class AbsCreateActivityRichText extends AppCompatActivity implements ICreateActivityUI {
    public static final String TAG = "AbsCreateActivityRichText";

    /**
     * 限制上传图片总数
     */
    private int picTotal = 6;

    /**
     * 创建内容成功时返回传递数据的KEY
     */
    public static final String EXTRA_RESPONSE = "CREATE_RESPONSE";

    /**
     * 创建内容成功时返回传递的CODE
     */
    public static final int RESULT_CODE_CREATE_SUCCESS = 0x0001;

    /**
     * 是否准备好发送请求
     * 
     * @return
     */
    public abstract boolean isCreateReady();


    /**
     * 返回要插入@用户的控件
     * 
     * @return
     */
    public abstract EditText getInsertAtUserNameEditText();

    /**
     * 返回输入控件
     * 
     * @return
     */
    public abstract EnterControlView getEnterToolsView();

    /**
     * Activity初始化 布局
     */
    public abstract void setupView();

    /**
     * 返回要接收输入内容的富文本控件
     * 
     * @return
     */
    public abstract RichTextEditorView getRichTextEditorView();

    /**
     * 清除控件里得内容
     */
    public abstract void clearViewsContent();

    /**
     * 打开相机
     */
    private final int RESULT_CODE_PHOTO = 0x1000;

    /**
     * 选取图片
     */
    private final int RESULT_CODE_PICK_PIC = 0x1001;

    /**
     * 阵容大师
     */
    private final int RESULT_CODE_BATTLE = 0x1002;

    /**
     * @用户
     */
    private final int RESULT_CODE_AT = 0x1003;

    /**
     * 视频
     */
    private final int REQUEST_CODE_VIDEO = 0x1004;

    private Handler mHandler;

    /**
     * 创建内容弹出框
     */
    protected CreateThreadDialog mDialog;

    private String token;

    /**
     * 保存的文件路径
     */
    private String mFilePath;

    private CreateActivityPresenter mPresenter;

    private VideoBroadcastReceiver mReceiver;


    private MeasureInputMethod mMeasureInputMethod;

    private Runnable mDialogShowRunnable;

    protected Activity getActivity(){
        return this;
    }

    private class DialogShowRunnable implements Runnable {
        @Override
        public void run() {
            mDialogShowRunnable = null;
            if (isFinishing())
                return;
            if (mDialog != null && mDialog.isShowing())
                mDialog.cancel();
            mDialog = new CreateThreadDialog(AbsCreateActivityRichText.this, new CreateThreadDialog.DialogListener() {
                @Override
                public void onCancel(View v) {
                    mPresenter.cancelCreateThread();
                }
            });
            mDialog.show();
            mDialog.setContent(getResources().getString(R.string.sending));
            mDialog.showProgress(true);
        }
    }


    public void onSubmit(){
        EmojiUtils.hideSoftInput(getApplicationContext(),
                getRichTextEditorView().getCurrentEditText());
        if (MyUtils.getFileSize(getRichTextEditorView().getPicDataStr())) {
            if (isCreateReady()) {
                createDialog();
                if (getRichTextEditorView().hasVideo()) {
                    getQiNiuToken();
                } else {
                    createUpload();
                }
            } else if (!TextUtils.isEmpty(getNotReadyToast())) {
                MyToaster.toastShort(getNotReadyToast());
            }

        } else {
            MyToaster.toastShort(getResources().getString(R.string.add_pic_over_limit_show));
        }
    }

    private EnterControlView.EnterToolsViewListener mEnterToolsViewListener = new EnterControlView.EnterToolsViewListener() {
        @Override
        public void onSlide(boolean out) {
            //setSlideOutEnable(out);
        }

        @Override
        public EditText getEnterEditTextView() {
            return AbsCreateActivityRichText.this.getRichTextEditorView().getCurrentEditText();
        }

        @Override
        public boolean getExpressiongTotal() {
            return AbsCreateActivityRichText.this.getRichTextEditorView().getAllExpressionTotal();
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        MyEventBus.getDefault().register(this);
        mHandler = new Handler();
        mPresenter = new CreateActivityPresenter(this, this);
        setupView();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initExpression();
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dongqiudi.qupai");
        mReceiver = new VideoBroadcastReceiver();
        registerReceiver(mReceiver, filter);
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
            mMeasureInputMethod = new MeasureInputMethod(this);
            mMeasureInputMethod.addOnGlobalLayoutListener();
        }
    }

    class VideoBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ThumbModel model = new ThumbModel();
            model.setThumb(intent.getStringExtra("thumb"));
            model.setPath(intent.getStringExtra("path"));
            model.setIsVideo(true);
            model.setVideoWidth(intent.getIntExtra("width", 480));
            model.setVideoHeight(intent.getIntExtra("height", 480));
            model.setDuration(intent.getLongExtra("duration", 0));
            model.setVideoSize(intent.getLongExtra("size", 0));
            model.setVideoType(intent.getStringExtra("type"));
            model.setVideoUrl(intent.getStringExtra("url"));
            model.setFileName(intent.getStringExtra("filename"));

            Bitmap bitmap = BitmapFactory.decodeFile(model.getThumb());
            addThumbModel4Video(model, bitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EmojiUtils.hideSoftInput(this, getRichTextEditorView().getCurrentEditText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyEventBus.getDefault().unregister(this);
        dialogDismiss();
        unregisterReceiver(mReceiver);
        if (mMeasureInputMethod != null) {
            mMeasureInputMethod.removeOnGlobalLayoutListener();
        }
    }

    /**
     * 获取七牛上传视频的token
     */
    private void getQiNiuToken() {

        MyUploader.getQiniuToken(new MyUploader.OnQiniuTokenCallback() {
            @Override
            public void onSuccess(String response) {
                token = MyJsonParser.getByKey(response, "token");
                ThumbModel videoModel = getRichTextEditorView().getVideoCache();
                if (videoModel != null && !TextUtils.isEmpty(token)) {
                    try {
                        DesUtil des = new DesUtil();
                        token = des.getDec(token);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                        String date = df.format(new Date());
                        uploadVideo(token, videoModel.path,
                                date + "/" + videoModel.fileName);
                    } catch (Exception e) {
                        dialogDismiss();
                        e.printStackTrace();
                    }
                } else {
                    dialogDismiss();
                    MyToaster.toastShort(getResources().getString(R.string.error_upload_video));
                }
            }

            @Override
            public void onFail(Object failEntity) {
//                ErrorEntity entity = AppUtils.getErrorEntity(error);
//                String errorMessage = null;
//                if (entity != null && !TextUtils.isEmpty(entity.getMessage())) {
//                    errorMessage = entity.getMessage();
//                } else {
//                    errorMessage = context.getString(R.string.error_upload_video);
//                    getEnterToolsView().showExpressionSelectView(false);
//                    getEnterToolsView().showExpressionImageView(true);
//                }
                dialogDismiss();
                MyToaster.toastShort("获取七牛token失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<ThumbModel> list = PhotoListActivity.tryToHandleResult(this, requestCode, resultCode, data);
        if(list != null && list.size() > 0){
            addThumbModels(list);
            return;
        }

        List<ThumbModel> list2 = VideoListActivity.tryToHandleResult(this, requestCode, resultCode, data);
        if(list2 != null && list2.size() > 0){
            MyToaster.toastShort("这个模式不支持视频：" + list2.get(0).getPath());
            return;
        }

        if (requestCode == RESULT_CODE_PHOTO) {
            MediaScannerConnection.scanFile(this, new String[] {
                    mFilePath
            }, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {

                    int id = MyUtils.getThumbIdByFileName(getActivity(), mFilePath);
                    if (id == -1)
                        return;
                    ThumbModel model = new ThumbModel();
                    model.setBucketId(id);
                    model.setPath(mFilePath);
                    model.setFileName(MyUtils.dealFilePathToFileName(mFilePath));
                    addThumbModel(model);
                }
            });

        } else if (requestCode == RESULT_CODE_PICK_PIC) {
            // handler.postDelayed (hide, 300);
            if (data != null) {
                Uri originalUri = MyUtils.getUri(getApplicationContext(), data);
                if (originalUri != null) {

                    String[] imgs = {
                            MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA
                    };
                    Cursor cursor = getContentResolver().query(originalUri, imgs, null, null, null);
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            ThumbModel model = new ThumbModel();
                            model.setBucketId(cursor.getInt(0));
                            model.setPath(cursor.getString(1));
                            model.setFileName(MyUtils.dealFilePathToFileName(cursor.getString(1)));
                            int state = MyUtils.canImageUpload(model.path);
                            if (state != 0) {
                                String text = state == 4
                                        ? (getResources().getString(R.string.not_supportupload)
                                                + MyEditor.UPLOAD_LIMIT_M
                                                + getResources().getString(R.string.mb_gifpic))
                                        : (state == 5
                                                ? getResources()
                                                        .getString(R.string.not_supportupload1280)
                                                : getResources().getString(R.string.useless_pic));
                                RemindDialog dialog = new RemindDialog(getActivity(), text,
                                        getString(R.string.reminder));
                                dialog.show();
                                return;
                            }
                            addThumbModel(model);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                }
            }
        }  else if (requestCode == REQUEST_CODE_VIDEO) {
            // TODO 截取视频第一帧图片
            if (resultCode == RESULT_OK) {
                // EditorResult result = new EditorResult(data);
                // // 得到视频path，和缩略图path的数组，返回十张缩略图,和视频时长
                // Bitmap bitmap =
                // BitmapFactory.decodeFile(result.getThumbnail()[0]);
                // ThumbModel model = new ThumbModel();
                // model.setPath(result.getPath());
                // model.setIsVideo(true);
                // model.setVideoWidth(bitmap.getWidth());
                // model.setVideoHeight(bitmap.getHeight());
                // if(result != null){
                // long time = result.getDuration() / 1000000000;
                // double timer = (double) time;
                // model.setDuration(timer);
                // }
                // model.setVideoSize(FileOperate.getFileSizes(new
                // File(result.getPath())));
                // model.setVideoType("video/mp4");
                // model.setVideoUrl("");
                // model.setFileName(AppUtils.dealFilePathToFileName(result.getPath()));
                // addThumbModel4Video(model, bitmap);
                //
                // // 删除草稿
                // QupaiDraftManager draftManager = new QupaiDraftManager();
                // draftManager.deleteDraft(data);
           }
        }

        // 图片大小超过限制
        if (!MyUtils.getFileSize(getRichTextEditorView().getPicDataStr())) {
            MyToaster.toastShort(getResources().getString(R.string.add_pic_over_limit_show));
        }
    }

    private void initExpression() {
        if (getEnterToolsView() != null) {
            getEnterToolsView().setEnterToolsViewListener(mEnterToolsViewListener);
            getEnterToolsView().initExpression();
        }
    }

    public void uploadVideo(String token, String path, String fileName) {
//        UploadManager uploadManager = new UploadManager();
//        uploadManager.put(path, fileName, token, new UpCompletionHandler() {
//            @Override
//            public void complete(String key, ResponseInfo info, JSONObject response) {
//                try {
//                    if (response != null) {
//                        String videoUrl = "";
//                        if (response.has("key")) {
//                            videoUrl = response.getString("key");
//                        }
//                        getRichTextEditorView().setVideoUrl(videoUrl);
//                    }
//                    createUpload();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, null);

    }

    private void createDialog() {
        mDialogShowRunnable = new DialogShowRunnable();
        // 延迟显示弹出框（优化网络好时弹出框闪烁情况）
        mHandler.postDelayed(mDialogShowRunnable, 1000);
    }

    @Override
    public void finish() {
        super.finish();
        if (mDialogShowRunnable != null && mHandler != null) {
            mHandler.removeCallbacks(mDialogShowRunnable);
            mDialogShowRunnable = null;
        }
    }

    public void createUpload() {
        if (isCreateReady()) {
            mPresenter.createThread();
        } else if (!TextUtils.isEmpty(getNotReadyToast())) {
            MyToaster.toastShort(getNotReadyToast());
        }
    }

    @Override
    public void onUploadProgress(final int progress) {
        // mHandler.post(new Runnable() {
        // @Override
        // public void run() {
        // AppUtils.hideSoftInput(getApplicationContext(),
        // getRichTextEditorView()
        // .getCurrentEditText());
        // if (mDialog != null && mDialog.isShowing()) {
        // if (progress > 0 && progress <= 100) {
        // mDialog.setContent(getResources().getString(R.string.sendnowing) +
        // "\n"
        // + progress + "%");
        // mDialog.showProgress(false);
        // } else if (progress ==
        // PicturesUploader.UploadProgressListener.PROGRESS_INDEFINITE) {
        // mDialog.setContent(getResources().getString(R.string.sending));
        // mDialog.showProgress(true);
        // }
        // }
        // }
        // });
    }

    @Override
    public void onUploadError(int errorCode, final String errorReason) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MyToaster.toastShort(errorReason);
                finish();
            }
        });
    }

    @Override
    public void onUploadResponse(final String response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                EmojiUtils.hideSoftInput(getApplicationContext(),
                        getRichTextEditorView().getCurrentEditText());
                dialogDismiss();
                if (TextUtils.isEmpty(response)) {
                    MyToaster.toastShort(getString(R.string.publish_comment_failure));
                    getEnterToolsView().showExpressionSelectView(false);
                    getEnterToolsView().showExpressionImageView(true);
                    return;
                }
                Intent data = new Intent();
                Parcelable parcelable = getExtraResponse(response);
                if (parcelable != null)
                    data.putExtra(EXTRA_RESPONSE, parcelable);
                else
                    data.putExtra(EXTRA_RESPONSE, response);
                setResult(RESULT_CODE_CREATE_SUCCESS, data);
                clearViewsContent();
                MyToaster.toastShort(getString(R.string.send_suc));
                finish();
            }
        });
    }

    private void dialogDismiss() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

    public void addThumbModel4Video(final ThumbModel model, final Bitmap bmp) {
        // 添加到富文本控件中
        if (getRichTextEditorView() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getRichTextEditorView().insertImage4Video(model, bmp);
                }
            });
        }
    }

    private void addThumbModel(final ThumbModel model) {
        // 添加到富文本控件中
        if (getRichTextEditorView() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getRichTextEditorView().insertImage(model);
                }
            });
        }
    }

    public void addThumbModels(final List<ThumbModel> models) {
        // 将缓存添加到富文本控件中
        if (getRichTextEditorView() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getRichTextEditorView().insertImages(models);
                }

            });
        }
    }

    /**
     * 插入@用户
     * 
     * @param userName
     */
    public void insertAtUserName(String userName) {
        EditText editText = getInsertAtUserNameEditText();
        if (editText == null)
            return;
        Editable edit = editText.getEditableText();// 获取EditText的文字
        // int index =
        int index = editText.getSelectionStart();
        if (index > 0 && edit.charAt(index - 1) == '@') {
            edit.delete(index - 1, index);
            index = editText.getSelectionStart();
        }
        if (TextUtils.isEmpty(userName))
            return;
        userName = "@" + userName + " ";
        if (index < 0 || index >= edit.length()) {
            edit.append(userName);
        } else {
            edit.insert(index, userName);// 光标所在位置插入文字
        }
    }

    public Parcelable getExtraResponse(String response) {
        return null;
    }


    /**
     * 相机拍照
     */
    public void startPhotoActivity() {
        String name = Long.toString(System.currentTimeMillis()) + ".jpg";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + "/dongqiudi/";
        mFilePath = path + name;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        File destination = new File(mFilePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, RESULT_CODE_PHOTO);
    }

    /**
     * 选取图片
     */
    public void startPickActivity() {
        int total = picTotal - getRichTextEditorView().getPicDataSize();
        if (total > 0) {
            PhotoListActivity.startForResult(this, total);
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.putExtra(GalleryPickerActivity.PIC_TOTAL_FLAG, total);
//            // image/png导致三星报错“相册停止运行” 改成image/PNG
//            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/PNG");
//            startActivityForResult(intent, RESULT_CODE_PICK_PIC);
        }
    }

    /**
     * 添加小视频
     */
    public void startVideoActivity() {

        VideoListActivity.startForResult(this);
    }

//    /**
//     * 打开视频插件
//     *
//     * @param filePath
//     * @param packageName
//     */
//    private void startVideoPlugin(final String filePath, final String packageName) {
//        EmojiUtils.hideSoftInput(getApplicationContext(),
//                getRichTextEditorView().getCurrentEditText());
//        if (AppUtils.isPluginInstall(packageName)) {
//            AppUtils.starPluginPackage(this, packageName);
//        } else {
//            if (TextUtils.isEmpty(filePath)) {
//                return;
//            }
//            File file = new File(filePath);
//            if (!file.exists()) {
//                try {
//                    AppUtils.showToast(this, getString(R.string.intalling_plugin));
//                    AppUtils.copyAssetFileToFiles(this, AppUtils.getPluginPath(this),
//                            Const.AppConstant.FILE_NAME);
//                    AppService.startInstallPlugin(AbsCreateActivityRichText.this, filePath,
//                            packageName, true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return;
//            }
//            AppService.startInstallPlugin(AbsCreateActivityRichText.this, filePath, packageName,
//                    true);
//
//        }
//    }

//    private void requestPlugin() {
//        // TODO: 16/8/25  修改url
//        String url = Urls.SERVER_PATH + "/v2/android/plugins_info?name=test";
//        final GsonRequest<PluginModel> req = new GsonRequest<PluginModel>(url, PluginModel.class,
//                AppUtils.getOAuthMap(getApplicationContext()),
//                new Response.Listener<PluginModel>() {
//                    @Override
//                    public void onResponse(PluginModel response) {
//                        if (response != null) {
//                            if (response.getVersion() > AppSharePreferences
//                                    .getPluginVersion(context, response.getPackagename())
//                                    || !AppUtils.isInstall(response.getPackagename())) {
//                                //如果插件正在下载，则不再下载
//                                List<String> pluginIds = AppSharePreferences.getPluginDownloadIds(context);
//                                if (pluginIds != null) {
//                                    for (int i = 0, j = pluginIds.size(); i < j; i++) {
//                                        final String str = pluginIds.get(i);
//                                        PluginDownloadModel pluginDownloadModel = AppUtils.getPluginDownloadInfo(str);
//                                        if (pluginDownloadModel == null
//                                                || (pluginDownloadModel.getName() != null
//                                                && !pluginDownloadModel.getName().equals(
//                                                response.getPackagename()))) {
//                                            continue;
//                                        }
//                                        //// TODO: 16/8/30
//                                        switch (getDownloadStatus(AbsCreateActivityRichText.this, pluginDownloadModel.getDownloadId())) {
//                                            case DownloadManager.STATUS_FAILED:
//                                                AppSharePreferences.deletePluginDownloadId(context, str);
//                                                return;
//                                            case DownloadManager.STATUS_PAUSED:
//                                            case DownloadManager.STATUS_PENDING:
//                                            case DownloadManager.STATUS_RUNNING:
//                                            case DownloadManager.STATUS_SUCCESSFUL:
//                                                return;
//                                        }
//
//                                    }
//                                } else {
//                                    if (AppSharePreferences.getPluginDownloadStatus(AbsCreateActivityRichText.this, response.getPackagename())) {
//                                        AppUtils.showToast(AbsCreateActivityRichText.this, getString(R.string.downloading_retry));
//                                        return;
//                                    }
//                                }
//
//                                AppUtils.downloadPlugin(AbsCreateActivityRichText.this,
//                                        response.getDownload(),
//                                        getString(R.string.download_plugin_title),
//                                        response.getDescription(),
//                                        response.getPackagename() + SPLIT
//                                                + response.getVersion() + SPLIT
//                                                + System.currentTimeMillis());
//                            } else {
//                                startVideoPlugin(null, response.getPackagename());
//                                AppSharePreferences.savePluginDownloadStatus(context,response.getPackagename(),false);
//                            }
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Trace.e(TAG, "error");
//                    }
//
//                });
//        addRequest(req);
//    }

//    /**
//     * 返回系统下载的状态
//     * @param context
//     * @param downloadId 下载的id
//     * @return
//     */
//    private int getDownloadStatus(Context context, long downloadId) {
//        DownloadManager manager = (DownloadManager) context
//                .getSystemService(Context.DOWNLOAD_SERVICE);
//        Cursor c = manager.query(new DownloadManager.Query().setFilterById(downloadId));
//        try {
//            if (c == null || !c.moveToFirst())
//                return DownloadManager.STATUS_FAILED;
//            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
//            int status = c.getInt(columnIndex);
//            int columnReason = c.getColumnIndex(DownloadManager.COLUMN_REASON);
//            int reason = c.getInt(columnReason);
//
//            switch (status) {
//                case DownloadManager.STATUS_FAILED:
//                    switch (reason) {
//                        case DownloadManager.ERROR_CANNOT_RESUME:
//                            //some possibly transient error occurred but we can't resume the download
//                            break;
//                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
//                            //no external storage device was found. Typically, this is because the SD card is not mounted
//                            break;
//                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
//                            //the requested destination file already exists (the download manager will not overwrite an existing file)
//                            break;
//                        case DownloadManager.ERROR_FILE_ERROR:
//                            //a storage issue arises which doesn't fit under any other error code
//                            break;
//                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
//                            //an error receiving or processing data occurred at the HTTP level
//                            break;
//                        case DownloadManager.ERROR_INSUFFICIENT_SPACE://sd卡满了
//                            //here was insufficient storage space. Typically, this is because the SD card is full
//                            break;
//                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
//                            //there were too many redirects
//                            break;
//                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
//                            //an HTTP code was received that download manager can't handle
//                            break;
//                        case DownloadManager.ERROR_UNKNOWN:
//                            //he download has completed with an error that doesn't fit under any other error code
//                            break;
//                    }
//
//
//                    AppUtils.showToast(context, getString(R.string.download_fail_please_retry));
//                    return DownloadManager.STATUS_FAILED;
//                case DownloadManager.STATUS_PAUSED:
//
//                    switch (reason) {
//                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
//                            //the download exceeds a size limit for downloads over the mobile network and the download manager is waiting for a Wi-Fi connection to proceed
//
//                            break;
//                        case DownloadManager.PAUSED_UNKNOWN:
//                            //the download is paused for some other reason
//                            break;
//                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
//                            //the download is waiting for network connectivity to proceed
//                            break;
//                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
//                            //the download is paused because some network error occurred and the download manager is waiting before retrying the request
//                            break;
//                    }
//                    AppUtils.showToast(context, getString(R.string.download_pause));
//                    return DownloadManager.STATUS_PAUSED;
//                case DownloadManager.STATUS_PENDING:
//                    //the download is waiting to start
//                    AppUtils.showToast(AbsCreateActivityRichText.this, getString(R.string.downloading_retry));
//                    return DownloadManager.STATUS_PENDING;
//                case DownloadManager.STATUS_RUNNING:
//                    //the download is currently running
//                    AppUtils.showToast(AbsCreateActivityRichText.this, getString(R.string.downloading_retry));
//                    return DownloadManager.STATUS_RUNNING;
//                case DownloadManager.STATUS_SUCCESSFUL:
//                    return DownloadManager.STATUS_SUCCESSFUL;
//            }
//        } finally {
//            if (c != null)
//                c.close();
//        }
//        return DownloadManager.STATUS_FAILED;
//    }

    /**
     * @用户
     */
    public void startAtActivity() {
        MyToaster.toastShort("选个人");
    }


    public String getNotReadyToast() {
        return getResources().getString(R.string.uncomment_nocontent);
    }


}
