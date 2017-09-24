
package org.ayo.editor;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import org.ayo.editor.controller.EnterControlView;
import org.ayo.editor.editor.RichTextEditorView;
import org.ayo.editor.emoj.engine.EmojiUtils;
import org.ayo.editor.model.VideoModel;
import org.ayo.imagepicker.ThumbModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 发帖
 */
public class CreateThreadActivity extends AbsCreateActivityRichText implements
        OnClickListener {

    private static final String SP_CONTENT = "create_thread_content";

    private static final String SP_TITLE = "create_thread_title";

    private EditText titleEditText;

    private RichTextEditorView mRichEditorView;

    private EnterControlView mEnterToolsView;

    private View mAtView, mPhotoView, mCameraView, mVideoView, mBattleView;

    private ProgressDialog dialog;
    // 标题字数限制
    private final static int COUNT_TITLE_CHAT_LIMIT = 36;

    private InputFilter[] mFilters = {
        new InputFilter.LengthFilter(COUNT_TITLE_CHAT_LIMIT) {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
                int keep = COUNT_TITLE_CHAT_LIMIT - (dest.length() - (dend - dstart));
                // 标题输入字符数超过限制时提醒
                if (keep < end - start) {
                    MyToaster.toastShort(getString(R.string.thread_title_count_limit));
                }
                return super.filter(source, start, end, dest, dstart, dend);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.item_enter_tool_photo){
            if (mRichEditorView.getPicDataSize() < 6) {
                EmojiUtils.hideSoftInput(this, titleEditText);
                startPickActivity();
            } else {
                MyToaster.toastShort(getResources().getString(R.string.atlast_sixpic));
            }
        }else if(id == R.id.item_enter_tool_camera){
            if (mRichEditorView.getPicDataSize() < 6) {
                startPhotoActivity();
            } else {
                MyToaster.toastShort(getResources().getString(R.string.atlast_sixpic));
            }
        }else if(id == R.id.item_enter_tool_at){
            startAtActivity();
        }else if(id == R.id.item_enter_tool_video){
            if (getRichTextEditorView().isOneVideo()) {
                startVideoActivity();
            } else {
                MyToaster.toastShort(getResources().getString(R.string.toast_upload_video_count));
            }
        }
    }

    @Override
    public void setupView() {
        setContentView(R.layout.activity_create_thread);
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setCanceledOnTouchOutside(false);
        }

        mEnterToolsView = (EnterControlView)findViewById(R.id.tools);
        mRichEditorView = (RichTextEditorView)findViewById(R.id.view_rich_editor);
        titleEditText = (EditText)findViewById(R.id.message_title);
        titleEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null) {
                    return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                } else {
                    return false;
                }
            }
        });
        titleEditText.setOnClickListener(this);
        titleEditText.setFilters(mFilters);

        mPhotoView = mEnterToolsView.addPhotoToolType();
        mCameraView = mEnterToolsView.addCameraToolType();
        mVideoView = mEnterToolsView.addVideoToolType();
        mAtView = mEnterToolsView.addAtToolType();
        mBattleView = mEnterToolsView.addBattleToolType();
        mPhotoView.setOnClickListener(this);
        mCameraView.setOnClickListener(this);
        mVideoView.setOnClickListener(this);
        mAtView.setOnClickListener(this);
        mBattleView.setOnClickListener(this);
        titleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEnterToolsView.showExpressionImageView(false);
                    mEnterToolsView.showExpressionSelectView(false);
                    mPhotoView.setVisibility(View.GONE);
                    mCameraView.setVisibility(View.GONE);
                    mAtView.setVisibility(View.GONE);
                    mVideoView.setVisibility(View.GONE);
                    mAtView.setVisibility(View.GONE);
                    mBattleView.setVisibility(View.GONE);
                } else {
                    mEnterToolsView.showExpressionImageView(true);
                    mEnterToolsView.showExpressionSelectView(false);
                    mPhotoView.setVisibility(View.VISIBLE);
                    mCameraView.setVisibility(View.VISIBLE);
                    mAtView.setVisibility(View.VISIBLE);
                    mVideoView.setVisibility(View.VISIBLE);
                    mAtView.setVisibility(View.VISIBLE);
                    mBattleView.setVisibility(View.VISIBLE);
                }
            }
        });

        String content = ""; //AppSharePreferences.getDefault(this).getString(SP_CONTENT, null);
        if (!TextUtils.isEmpty(content)) {
            if (!dialog.isShowing())
                dialog.show();

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadCache();
            }
        }, 200);

        // 表情显示时,点击输入框回调隐藏表情
        getRichTextEditorView()
                .setOnRichTextClickListener(new RichTextEditorView.OnRichTextClickListener() {
                    @Override
                    public void onEditClickListener() {
                        getEnterToolsView().showExpressionSelectView(false);
                    }

                    @Override
                    public void onEditLongClickListener() {
                        getEnterToolsView().showExpressionImageView(true);
                        getEnterToolsView().showExpressionSelectView(false);
                    }
                });

    }

    @Override
    public void finish() {
        EmojiUtils.hideSoftInput(this, titleEditText);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EmojiUtils.hideSoftInput(this, titleEditText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEnterToolsView.showExpressionSelectView(false);
        getRichTextEditorView().getCurrentEditText().requestFocus();

    }

    @Override
    protected void onDestroy() {
        storeCache();
        super.onDestroy();
    }

    private void loadCache() {
        String title = ""; //AppSharePreferences.getDefault(this).getString(SP_TITLE, null);
        if (!TextUtils.isEmpty(title)) {
            titleEditText.setText(title);
        }
        String content = ""; //AppSharePreferences.getDefault(this).getString(SP_CONTENT, null);
        if (!TextUtils.isEmpty(content)) {
            addViewByCache(content);
        } else {
            mRichEditorView.setHint(getActivity().getString(R.string.hint_create_thread));
            dismiss();
        }
    }

    /**
     * 添加的图文混排缓存
     * @param content
     */
    private void addViewByCache (String content){
        List<ThumbModel> models = null;
        if (!TextUtils.isEmpty(content)) {
            try {
                models = MyJsonParser.toList(content, ThumbModel.class);
//                JSON.parseObject(content,
//                        new TypeReference<List<ThumbModel>>() {
//                        }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (models == null || models.size() == 0) {
            return;
        }
        for (int i = models.size() - 1; i >= 0; i--) {
            if (!TextUtils.isEmpty(models.get(i).content)) {
                // edittext
                getRichTextEditorView().insertEditText4Cache(models.get(i).content);
            } else {
                // imageview
                if (models.get(i).path != null) {
                    if (models.get(i).isVideo) {
                        // video 截图
                        Bitmap bitmap = MyUtils.getBitmapByVideo(models.get(i).path);
                        getRichTextEditorView().insertVideoImage4Cache(models.get(i), bitmap);
                    } else {
                        // imageview
                        getRichTextEditorView().insertImage4Cache(models.get(i));
                    }

                }
            }
        }

        // 判断最后一个是不是空edittext,如果是则从layout中remove掉
        getRichTextEditorView().removeViewAt();
        dismiss();
    }

    private void storeCache() {
//        List<ThumbModel> list = getRichTextEditorView().saveDataStrForCache();
//        if (list != null && list.size() > 0) {
//            if(list.size() == 1 && TextUtils.isEmpty(getRichTextEditorView().getCurrentEditText().getText().toString().trim())){
//                AppSharePreferences.getDefault(this).edit().remove(SP_CONTENT).commit();
//            }else{
//                try {
//                    String content = JSON.toJSONString(list);
//                    AppSharePreferences.getDefault(this).edit().putString(SP_CONTENT, content).commit();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }else{
//            AppSharePreferences.getDefault(this).edit().remove(SP_CONTENT).commit();
//        }
//
//        String title = titleEditText.getText().toString();
//        if (!TextUtils.isEmpty(title)) {
//            AppSharePreferences.getDefault(this).edit().putString(SP_TITLE, title).commit();
//        }else{
//            AppSharePreferences.getDefault(this).edit().remove(SP_TITLE).commit();
//        }
    }

    @Override
    public String getUrl() {
        return ""; //Const.Urls.SERVER_PATH + "/groups/create_topic/" + groupId;
    }

    @Override
    public Map<String, String> getRequestParams() {
        String value = titleEditText.getText().toString().trim();
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(value))
            map.put("title", value);
        value = mRichEditorView.getDataStr();
        map.put("content", TextUtils.isEmpty(value) ? "" : value);
        List<VideoModel> modelList = getVideoModel(mRichEditorView.getVideoCache());
        if(modelList != null && modelList.size() > 0){
            map.put("videos", MyJsonParser.toJson(modelList));
        }
        return map;
    }

    private List<VideoModel> getVideoModel(ThumbModel model) {
        List<VideoModel> list = new ArrayList<>();
        VideoModel videoModel = null;
        if (model != null && model.isVideo) {
            videoModel = new VideoModel();
            videoModel.id = "{{v1}}";
            videoModel.width = model.videoWidth;
            videoModel.height = model.videoHeight;
            videoModel.length = model.duration;
            videoModel.size = model.videoSize;
            videoModel.url = model.videoUrl;
            videoModel.mime = model.videoType;
            list.add(videoModel);
        }
        return list;
    }

    @Override
    public List<String> getPicturePaths() {
        List<ThumbModel> list = mRichEditorView.getPicData();
        if (list == null || list.isEmpty())
            return null;
        Iterator<ThumbModel> it = list.iterator();
        ThumbModel model;
        List<String> data = new ArrayList<>();
        while (it.hasNext()) {
            model = it.next();
            if (model != null && !TextUtils.isEmpty(model.path))
                data.add(model.path);
        }
        return data;
    }

    @Override
    public boolean isCreateReady() {
        if (TextUtils.isEmpty(mRichEditorView.getDataStr())
                && TextUtils.isEmpty(titleEditText.getText().toString().trim())
                && mRichEditorView.getPicDataSize() < 1)
            return false;
        else
            return true;
    }


    @Override
    public EditText getInsertAtUserNameEditText() {
        return mRichEditorView.getCurrentEditText();
    }

    @Override
    public EnterControlView getEnterToolsView() {
        return mEnterToolsView;
    }

    @Override
    public void clearViewsContent() {
        titleEditText.setText("");
        mRichEditorView.clearData();
        clearCache();
    }

    private void clearCache(){
//        AppSharePreferences.getDefault(this).edit().remove(SP_TITLE).commit();
//        AppSharePreferences.getDefault(this).edit().remove(SP_CONTENT).commit();
    }

    public String getNotReadyToast() {
        return getResources().getString(R.string.uncomment_nocontent);
    }

    @Override
    public RichTextEditorView getRichTextEditorView() {
        return mRichEditorView;
    }

    private void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

}
