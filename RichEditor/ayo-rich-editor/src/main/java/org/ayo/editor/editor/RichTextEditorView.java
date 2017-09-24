package org.ayo.editor.editor;


import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.ayo.editor.MyToaster;
import org.ayo.editor.MyUtils;
import org.ayo.editor.R;
import org.ayo.editor.emoj.engine.EmojiUtils;
import org.ayo.editor.emoj.engine.ExpressionParser;
import org.ayo.imagepicker.ThumbModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个富文本编辑器，给外部提供insertImage接口，添加的图片跟当前光标所在位置有关
 * Created by lishuai on 16/5/4.
 */
public class RichTextEditorView extends ScrollView {

    public static final String TAG = "RichTextEditorView";
    private static final int EDIT_FIRST_PADDING_TOP = 12; // 第一个EditText的paddingTop值

    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。

    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup

    private LayoutInflater inflater;

    private View.OnKeyListener keyListener; // 所有EditText的软键盘监听器

    private View.OnClickListener btnListener; // 图片右上角红叉按钮监听器

    private View.OnClickListener videoPlayListener; // 播放按钮点击事件

    private View.OnClickListener layoutListener; // 图片四边点击事件

    private View.OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener

    private EditText lastFocusEdit; // 最近被聚焦的EditText

    private LayoutTransition mTransition; // 只在图片View添加或remove时，触发transition动画

    private int disappearingImageIndex = 0;

    private Context mContext;

    private final int DURING = 200;

    /**
     * 插入的video,如果为null代表已被删除
     */
    private ThumbModel videoModel;

    // 存储图片
    private List<ThumbModel> models = new ArrayList<>();

    public RichTextEditorView(Context context) {
        this(context, null);
    }

    public RichTextEditorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflater = LayoutInflater.from(context);

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        allLayout.setBackgroundColor(Color.WHITE);
        setupLayoutTransitions();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        addView(allLayout, layoutParams);

        // 2. 初始化键盘退格监听
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };

        // 3. 图片叉掉处理
        btnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout parentView = (RelativeLayout) v.getParent();
                onImageCloseClick(parentView);
            }
        };

        // 4. 视频播放点击事件
        videoPlayListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout parentView = (RelativeLayout) v.getParent();
                onVideoPlayClick(parentView);
            }
        };

        layoutListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int lastEditIndex = allLayout.indexOfChild(v);
                if (!(allLayout.getChildAt(lastEditIndex + 1) instanceof EditText)) {
                    addEditTextAtIndex(lastEditIndex + 1, "");
                } else {
                    EditText editText = (EditText)allLayout.getChildAt(lastEditIndex + 1);
                    editText.requestFocus();
                }
            }
        };

        focusListener = new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastFocusEdit = (EditText) v;
                }
            }
        };

        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        EditText firstEdit = createEditText("",
                MyUtils.dip2px(EDIT_FIRST_PADDING_TOP));
        allLayout.addView(firstEdit, firstEditParam);
        lastFocusEdit = firstEdit;

    }

    public void setHint(String strHint){
        if(allLayout.getChildCount() == 1){
            EditText editText = (EditText)allLayout.getChildAt(0);
            editText.setHint(strHint);
        }
    }

    public void clearHint(){
        if(allLayout.getChildCount() == 1){
            EditText editText = (EditText)allLayout.getChildAt(0);
            editText.setHint("");
        }
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(final EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    // 合并文本view时，不需要transition动画
                    allLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            allLayout.removeView(editTxt);
                        }
                    }, 100);

                    // 文本合并
                    String str = str1 + str2;
                    setEmojiStr(preEdit, str);
                    preEdit.requestFocus();
                    preEdit.setSelection(str.length(), str.length());
                }
            }
        }
    }

    /**
     * 判断最后一个是不是空edittext,如果是则从layout中remove掉()
     * (此方法只用于加载缓存时,将光标定位到最后一个edittext上)
     */
    public void removeViewAt(){
        // 最后一个view
        final int index = allLayout.getChildCount() - 1;
        if (allLayout.getChildCount() > 1 && allLayout.getChildAt(index) instanceof EditText
                && allLayout.getChildAt(index - 1) instanceof EditText) {
            allLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    allLayout.removeViewAt(index);
                    lastFocusEdit = (EditText)allLayout.getChildAt(index - 1);
                    lastFocusEdit.requestFocus();
                    lastFocusEdit.setSelection(lastFocusEdit.getText().length());
                }
            }, 100);
        }
    }

    /**
     * 获取当前焦点所在的editText
     */
    public EditText getCurrentEditText() {
        return lastFocusEdit;
    }

    /**
     * 获取所有editText的表情总数
     */
    public boolean getAllExpressionTotal(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < allLayout.getChildCount(); i++) {
            if(allLayout.getChildAt(i) instanceof EditText){
                EditText edittext = (EditText)allLayout.getChildAt(i);
                builder.append(edittext.getText());
                if(!ExpressionParser.getInstance()
                        .getExpressionTotal(builder.toString())){
                    // 大于60个表情
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 处理图片叉掉的点击事件
     *
     * @param view 整个image对应的relativeLayout view
     *  删除类型 0代表backspace删除 1代表按红叉按钮删除
     */
    private void onImageCloseClick(final View view) {
        if (!mTransition.isRunning()) {
            disappearingImageIndex = allLayout.indexOfChild(view);
            if (models != null && models.size() > 0) {
                if(view instanceof RelativeLayout){
                    RelativeLayout layout = (RelativeLayout)view;
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        if(layout.getChildAt(i) instanceof RichImageView){
                            RichImageView richView = (RichImageView)layout.getChildAt(i);
                            if(richView != null && richView.getIsVideo()){
                                // 清楚缓存videoModel
                                videoModel = null;
                            }
                        }
                    }

                }
                allLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        allLayout.removeView(view);
                    }
                }, 200);
            }
        }
        //去掉删除图片后editText的倒影
        if(lastFocusEdit!=null){
            lastFocusEdit.clearFocus();
            lastFocusEdit.postDelayed(new Runnable() {
                @Override
                public void run() {

                    lastFocusEdit.requestFocus();
                }
            }, DURING +50);
        }


    }

    /**
     * 编辑框中的图片点击事件
     */
    private void onVideoPlayClick(View view) {
        if (!mTransition.isRunning()) {
            disappearingImageIndex = allLayout.indexOfChild(view);
            if (view instanceof RelativeLayout) {
                RelativeLayout layout = (RelativeLayout) view;
                for (int i = 0; i < layout.getChildCount(); i++) {
                    if (layout.getChildAt(i) instanceof RichImageView) {
                        RichImageView richView = (RichImageView) layout.getChildAt(i);
                        if (richView != null && richView.getIsVideo()) {
                            // TODO 视频播放
                            MyToaster.toastShort("播放视频：" + richView.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 清除数据
     */
    public void clearData() {
        if (!mTransition.isRunning()) {
            allLayout.removeAllViews();
            lastFocusEdit.setText("");
            videoModel = null;
            models.clear();
        }
    }

    public void setVideoUrl(String url){
        if(videoModel != null){
            videoModel.setVideoUrl(url);
        }else{
            videoModel.setVideoUrl("http_error");
        }
    }

    /**
     * 生成文本输入框
     */
    private EditText createEditText(String hint, int paddingTop) {
        EditText editText = (EditText) inflater.inflate(R.layout.view_rich_edit_item, null);
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        editText.setPadding(MyUtils.dip2px(15), MyUtils.dip2px(12), MyUtils.dip2px(15), 0);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRichTextClickListener != null) {
                    mOnRichTextClickListener.onEditClickListener();
                }
            }
        });
        editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnRichTextClickListener.onEditLongClickListener();
                return false;
            }
        });
        return editText;
    }

    /**
     * 插入文本框
     *
     * @param str
     */
    public void insertEditText(String str) {
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        EditText editText;
        if (TextUtils.isEmpty(str)) {
            editText = createEditText(mContext.getString(R.string.write_sth), MyUtils.dip2px(EDIT_FIRST_PADDING_TOP));
        } else {
            editText = createEditText("", MyUtils.dip2px(EDIT_FIRST_PADDING_TOP));
        }
        editText.setText(str);
        allLayout.addView(editText, firstEditParam);
        lastFocusEdit = editText;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout(boolean isVideo) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.view_rich_edit_imageview,
                null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        View videoView = layout.findViewById(R.id.image_video);
        if (isVideo) {
            videoView.setVisibility(View.VISIBLE);
        } else {
            videoView.setVisibility(View.GONE);
        }
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        videoView.setOnClickListener(videoPlayListener);
        layout.setOnClickListener(layoutListener);
        return layout;
    }

    /**
     * 添加单张图片
     */
    public void insertImage(ThumbModel model) {
        clearHint();
        Bitmap bmp = getScaledBitmap(model.path, getWidth());
        //修复三星手机拍照后图片会旋转
        if (MyUtils.isSamsungRom()) {
            int degree = MyUtils.getBitmapDegree(model.path);
            bmp = MyUtils.rotateBitmapByDegree(bmp, degree);
        }

        if (bmp != null) {
            insertImage(bmp, model);
            models.add(model);
        }
    }

    /**
     * 添加视频截图
     */
    public void insertImage4Video(ThumbModel model, Bitmap bmp) {
        clearHint();
        videoModel = model;
        insertImage(bmp, model);
        models.add(model);

    }

    /**
     * 获取刚拍摄的小视频model
     */
    public ThumbModel getVideoCache(){
        return videoModel;
    }

    /**
     * 判断是否有视频
     */
    public boolean hasVideo(){
        List<EditData> dataList = buildEditData();
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).isVideo) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否只有一个视频
     */
    public boolean isOneVideo(){
        int videoCount = 0;
        List<EditData> dataList = buildEditData();
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).isVideo) {
                    videoCount++;
                }
            }
        }
        return videoCount < 1;
    }

    /**
     * 多选图片添加多张图片
     */
    public void insertImages(List<ThumbModel> models) {
        for (int i = models.size() - 1; i >= 0; i--) {
            insertImage(models.get(i));
        }
    }

    /**
     * 插入一张图片
     */
    private void insertImage(Bitmap bitmap, ThumbModel model) {
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex);
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);

        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            addImageViewAtIndex(lastEditIndex, bitmap, model);
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            setEmojiStr(lastFocusEdit, editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex);
            if (allLayout.getChildCount() - 1 == lastEditIndex || editStr2.length() > 0) {
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }

            addImageViewAtIndex(lastEditIndex + 1, bitmap, model);

        }
        lastFocusEdit.requestFocus();
        lastFocusEdit.setSelection(0);
        hideKeyBoard();
    }

    private void setEmojiStr(EditText edittext, String str) {
        StringBuilder sBuilder;
        sBuilder = new StringBuilder(str);
        edittext.setText(EmojiUtils.replaceEmojiTextView(sBuilder.toString(),
                (int) edittext.getTextSize()));
    }

    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    private void addEditTextAtIndex(final int index, String editStr) {
        EditText editText2 = createEditText("", 0);
        setEmojiStr(editText2, editStr);

        // 请注意此处，EditText添加、或删除不触动Transition动画
        editText2.requestFocus();
        allLayout.setLayoutTransition(null);
        allLayout.addView(editText2, index);
        allLayout.setLayoutTransition(mTransition); // remove之后恢复transition动画
    }

    /**
     * 在特定位置添加ImageView
     */
    private void addImageViewAtIndex(final int index, Bitmap bmp,
                                     ThumbModel model) {
        final RelativeLayout imageLayout = createImageLayout(model.isVideo);
        RichImageView imageView = (RichImageView)imageLayout.findViewById(R.id.edit_imageView);
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pic_empty);
        }
        imageView.setImageBitmap(bmp);
        imageView.setAbsolutePath(model.path);
        imageView.setFilename(model.fileName);
        imageView.setIsVideo(model.isVideo);
        if (model.isVideo) {
            imageView.videoWidth = model.videoWidth;
            imageView.videoHeight = model.videoHeight;
            imageView.duration = model.duration;
            imageView.videoSize = model.videoSize;
            imageView.videoType = model.videoType;
            imageView.videoUrl = model.videoUrl;
        }

        // 截图allLayout的touch事件
        imageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        // 调整imageView的高度
        int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);
        lp.setMargins(0, MyUtils.dip2px(12), 0, 0);
        imageView.setLayoutParams(lp);

        // onActivityResult无法触发动画，此处post处理
        allLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                allLayout.addView(imageLayout, index);
            }
        }, 200);
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    private Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / (width + 1) : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransition = new LayoutTransition();
        allLayout.setLayoutTransition(mTransition);
        mTransition.addTransitionListener(new LayoutTransition.TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {
            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
                    mergeEditText();
                }
            }
        });
        mTransition.setDuration(LayoutTransition.CHANGE_DISAPPEARING, DURING);
        // 去掉添加View的动画，避免添加图片是定位问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTransition.disableTransitionType(LayoutTransition.APPEARING);
            mTransition.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
        }else {
            mTransition.setDuration(LayoutTransition.CHANGE_APPEARING, 0);
            mTransition.setDuration(LayoutTransition.APPEARING, 0);
        }
    }

    /**
     * 图片删除的时候，如果上下方都是EditText，则合并处理
     */
    private void mergeEditText() {
        View preView = allLayout.getChildAt(disappearingImageIndex - 1);
        View nextView = allLayout.getChildAt(disappearingImageIndex);
        if (preView != null && preView instanceof EditText && null != nextView
                && nextView instanceof EditText) {
            EditText preEdit = (EditText) preView;
            EditText nextEdit = (EditText) nextView;
            String str1 = preEdit.getText().toString();
            String str2 = nextEdit.getText().toString();
            String mergeText;
            if (str2.length() > 0) {
                mergeText = str1 + "\n" + str2;
            } else {
                mergeText = str1;
            }

            allLayout.setLayoutTransition(null);
            allLayout.removeView(nextEdit);
            setEmojiStr(preEdit, mergeText);
            preEdit.requestFocus();
            preEdit.setSelection(str1.length(), str1.length());
            allLayout.setLayoutTransition(mTransition);
        }
    }

    /**
     * 对外提供的接口, 生成编辑数据上传
     */
    public List<EditData> buildEditData() {
        List<EditData> editList = new ArrayList<>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditData itemData = new EditData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.inputStr = item.getText().toString();
            } else if (itemView instanceof RelativeLayout) {
                RichImageView item = (RichImageView) itemView
                        .findViewById(R.id.edit_imageView);
                itemData.imagePath = item.getAbsolutePath();
                itemData.bitmap = item.getBitmap();
                itemData.isVideo = item.getIsVideo();
                itemData.fileName = item.getFilename();
                if (item.getIsVideo()) {
                    // video
                    itemData.videoUrl = item.getVideoUrl();
                    itemData.duration = item.duration;
                    itemData.videoType = item.videoType;
                    itemData.videoWidth = item.videoWidth;
                    itemData.videoHeight = item.videoHeight;
                    itemData.videoSize = item.videoSize;
                }
            }
            editList.add(itemData);
        }

        return editList;
    }

    /**
     * 对外提供的接口,生成编辑数据带图片占位符
     **/
    public String getDataStr() {
        List<EditData> dataList = buildEditData();
        StringBuilder builder = new StringBuilder();
        int numImg = 0;
        int numVideo = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).inputStr != null) {
                builder.append(dataList.get(i).inputStr);
            } else if (dataList.get(i).isVideo) {
                numVideo++;
                builder.append("{{v").append(numVideo).append("}}");
            } else {
                numImg++;
                builder.append("{{p").append(numImg).append("}}");
            }
        }
        return builder.toString();

    }

    public int getPicDataSize() {
        return getPicData() == null ? 0 : getPicData().size();
    }

    /**
     * 获取全部图片
     **/
    public List<ThumbModel> getPicData() {
        List<ThumbModel> imageList = new ArrayList<>();
        List<EditData> dataList = buildEditData();
        for (int i = 0; i < dataList.size(); i++) {
            if (!TextUtils.isEmpty(dataList.get(i).imagePath) && !dataList.get(i).isVideo) {
                // 重新构成存储图片的list
                ThumbModel model = new ThumbModel();
                model.path = dataList.get(i).imagePath;
                imageList.add(model);
            }
        }
        return imageList;
    }

    /**
     * 获取所有要上传图片的路径
     */
    public List<String> getPicDataStr() {
        List<String> listStr = new ArrayList<>();
        List<ThumbModel> list = getPicData();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if(!list.get(i).isVideo){
                    listStr.add(list.get(i).path);
                }
            }
        } else {
            return listStr;
        }
        return listStr;
    }

    /**
     * 插入video截图只提供给缓存使用
     **/
    public void insertVideoImage4Cache(ThumbModel model, Bitmap bmp) {
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pic_empty);
        }
        models.add(model);
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex);
        setEmojiStr(lastFocusEdit, editStr1);

        final RelativeLayout imageLayout = createImageLayout(model.isVideo);
        RichImageView imageView = (RichImageView)imageLayout.findViewById(R.id.edit_imageView);
        imageView.setImageBitmap(bmp);
        imageView.setAbsolutePath(model.path);
        imageView.setFilename(model.fileName);
        imageView.setIsVideo(model.isVideo);
        if (model.isVideo) {
            imageView.videoWidth = model.videoWidth;
            imageView.videoHeight = model.videoHeight;
            imageView.duration = model.duration;
            imageView.videoSize = model.videoSize;
            imageView.videoType = model.videoType;
            imageView.videoUrl = model.videoUrl;
            videoModel = model;
        }
        // 截图allLayout的touch事件
        imageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        // 调整imageView的高度
        int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);
        lp.setMargins(0, MyUtils.dip2px(12), 0, 0);
        imageView.setLayoutParams(lp);
        allLayout.addView(imageLayout, 0);
        lastFocusEdit.requestFocus();
        lastFocusEdit.setSelection(0);
    }

    /**
     * 插入图片只提供给缓存使用
     **/
    public void insertImage4Cache(ThumbModel model) {
        Bitmap bmp = getScaledBitmap(model.path, getWidth());
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pic_empty);
        }
        models.add(model);
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex);
        setEmojiStr(lastFocusEdit, editStr1);

        final RelativeLayout imageLayout = createImageLayout(model.isVideo);
        RichImageView imageView = (RichImageView) imageLayout
                .findViewById(R.id.edit_imageView);
        imageView.setImageBitmap(bmp);
        imageView.setAbsolutePath(model.path);
        imageView.setFilename(model.fileName);
        imageView.setIsVideo(model.isVideo);
        // 截图allLayout的touch事件
        imageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        // 调整imageView的高度
        int imageHeight = getWidth() * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);
        lp.setMargins(0, MyUtils.dip2px(12), 0, 0);
        imageView.setLayoutParams(lp);
        allLayout.addView(imageLayout, 0);

        lastFocusEdit.requestFocus();
        lastFocusEdit.setSelection(0);
    }

    /**
     * 插入文本框(缓存用)
     **/
    public void insertEditText4Cache(String str) {
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        EditText editText;
        if (TextUtils.isEmpty(str)) {
            editText = createEditText(mContext.getString(R.string.hint_create_thread), MyUtils.dip2px(EDIT_FIRST_PADDING_TOP));
        } else {
            editText = createEditText("", MyUtils.dip2px(EDIT_FIRST_PADDING_TOP));
        }
        setEmojiStr(editText, str);
        allLayout.addView(editText, 0, firstEditParam);
    }

    /**
     * 只用于存储缓存
     */
    public List<ThumbModel> saveDataStrForCache() {
        List<EditData> dataList = buildEditData();
        List<ThumbModel> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            ThumbModel model = new ThumbModel();
            if (dataList.get(i).inputStr != null) {
                model.content = dataList.get(i).inputStr;
            } else if(dataList.get(i).isVideo){
                model.videoUrl = dataList.get(i).videoUrl;
                model.path = dataList.get(i).imagePath;
                model.fileName = dataList.get(i).fileName;
                model.isVideo = dataList.get(i).isVideo;
                model.duration = dataList.get(i).duration;
                model.videoType = dataList.get(i).videoType;
                model.videoWidth = dataList.get(i).videoWidth;
                model.videoHeight = dataList.get(i).videoHeight;
                model.videoSize = dataList.get(i).videoSize;
            } else {
                model.path = dataList.get(i).imagePath;
                model.isVideo = dataList.get(i).isVideo;
                model.fileName = dataList.get(i).fileName;
            }
            list.add(model);
        }
        return list;
    }

    public void addData(List<ThumbModel> list) {
        models.addAll(list);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int viewCount = allLayout.getChildCount();
        View view = allLayout.getChildAt(viewCount-1);
        if(view instanceof EditText){
            view.requestFocus();
        }
        return super.onTouchEvent(ev);
    }

    public class EditData {
        String inputStr;
        String imagePath;
        public Bitmap bitmap;
        boolean isVideo;
        public String fileName;
        /**
         * 视频时长
         */
        public long duration;

        /**
         * 视频类型
         */
        String videoType;

        int videoWidth;

        int videoHeight;

        long videoSize;

        String videoUrl;
    }
    private OnRichTextClickListener mOnRichTextClickListener;
    public void setOnRichTextClickListener(OnRichTextClickListener onRichTextClickListener){
        mOnRichTextClickListener = onRichTextClickListener;
    }
    public interface OnRichTextClickListener{
        void onEditClickListener();
        void onEditLongClickListener();
    }

}
