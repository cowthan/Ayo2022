package com.zebdar.tom.chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zebdar.tom.Const;
import com.zebdar.tom.DropdownListView;
import com.zebdar.tom.PreferencesUtils;
import com.zebdar.tom.R;
import com.zebdar.tom.SysUtils;
import com.zebdar.tom.chat.adapter.ItemTemplateManager;
import com.zebdar.tom.chat.background.BackgroundManager;
import com.zebdar.tom.chat.background.BaseChatBgProvider;
import com.zebdar.tom.chat.input.InputView;
import com.zebdar.tom.chat.input.SoftKeyBoardListener;
import com.zebdar.tom.chat.model.IMMsg;
import com.zebdar.tom.chat.model.IMessageDao;
import com.zebdar.tom.chat.model.OnMessageChangedListener;
import com.zebdar.tom.chat.model.memdb.MessageCache;
import com.zebdar.tom.speech.SpeechParser;
import com.zebdar.tom.speech.SpeechReader;

import org.ayo.core.Lang;
import org.ayo.list.adapter.AyoSoloAdapter2;
import org.ayo.statusbar.StatusBarCompat;

import java.util.List;


/**
 * 聊天界面
 */
public class ChatActivity extends AppCompatActivity implements DropdownListView.OnRefreshListenerHeader, View.OnClickListener{

    /** 这些是固定的，很长一段时间内都会留在这个类里 */
    private DropdownListView mListView;
    private AyoSoloAdapter2<IMMsg> mLvAdapter;
    private IMessageDao msgDao;
    private List<IMMsg> listMsg;
    private int offset;
    private BaseChatBgProvider mBackgroundProvider;

    String voice_type;
    InputView mInputView;

    //titlebar
    public TextView tv_left, tv_title, tv_right;
    public ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_chat);

        ChatManager.getDefault().setCurrentPartner(ChatManager.getFakeFouther());

        initTitleBar("消息", "智能机器人", "", this);
        msgDao = new MessageCache();

        //设置背景和状态栏
        mBackgroundProvider = BackgroundManager.getDefault().nextBackgroundProvider();
        mBackgroundProvider.onAttached(this);
        View bgView = mBackgroundProvider.getBackground(this);
        if(bgView != null && bgView.getParent() != null){
            ((ViewGroup)bgView.getParent()).removeAllViews();
        }
        FrameLayout bgContainer = (FrameLayout) findViewById(R.id.background_container);
        bgContainer.removeAllViews();
        bgContainer.addView(bgView);

        int statusBarColor = mBackgroundProvider.getStatusBarColor();
        View title_barrrrr = findViewById(R.id.title_barrrrr);
        ViewGroup.MarginLayoutParams lpTitlebar = (ViewGroup.MarginLayoutParams) title_barrrrr.getLayoutParams();
        if(statusBarColor == 0){
            //状态栏透明，侵入状态栏，是全屏模式，如果有标题栏，标题栏需要让出一定高度
            StatusBarCompat.translucentStatusBar(this);
            lpTitlebar.topMargin = Lang.statusBarHeight();
        }else{
            //状态栏着色，不侵入状态栏，是着色模式
            StatusBarCompat.setStatusBarColor(this, statusBarColor);
            lpTitlebar.topMargin = 0;
        }
        title_barrrrr.setLayoutParams(lpTitlebar);
        /// ~~~ 设置背景over

        //设置小键盘监听
        mInputView = (InputView) findViewById(R.id.input_view);
        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(this);
        softKeyBoardListener.register(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mInputView.onKeyboardShown(height);
            }

            @Override
            public void keyBoardHide(int height) {
                mInputView.onKeyboardHidden(height);
            }
        });
        mInputView.bind(this);
        // ~~~ 小键盘监听 over

        voice_type = PreferencesUtils.getSharePreStr(this, Const.IM_VOICE_TPPE);
        //初始化控件
        mListView = (DropdownListView) findViewById(R.id.message_chat_listview);
        SysUtils.setOverScrollMode(mListView);

        mListView.setOnRefreshListenerHead(this);
        mListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    mInputView.hideInputSection(ChatActivity.this);
                }
                return false;
            }
        });
        //初始化数据
        offset = 0;
        listMsg = msgDao.queryMsg(ChatManager.getDefault().getMe().id, ChatManager.getDefault().getCurrentPartner().id, offset);
        offset = listMsg.size();
        mLvAdapter = new AyoSoloAdapter2<>(this, ItemTemplateManager.getDefault().getTemplates());
        mListView.setAdapter(mLvAdapter);
        mLvAdapter.notifyDataSetChanged(listMsg);
        mListView.setSelection(listMsg.size());

        //初始化语音听写及合成部分
//        initSpeech();

        MessageCenter.getDefault().addOnMessageRemoteListener(new OnMessageChangedListener() {
            @Override
            public void onAdd(IMMsg msg) {
                listMsg.add(msg);
                offset = listMsg.size();
                mLvAdapter.notifyDataSetChanged();
                mInputView.setText("");
            }

            @Override
            public void onDelete(IMMsg m) {
                listMsg.remove(m);
                offset = listMsg.size();
                mLvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUpdate(IMMsg m) {

            }

            @Override
            public void onLoading(IMMsg m, boolean isFinish, int progress) {

            }
        });

        SpeechParser.getDefault().init(this);
    }

    /**
     * 下拉加载更多
     */
    @Override
    public void onRefresh() {
        List<IMMsg> list = msgDao.queryMsg(ChatManager.getDefault().getMe().id, ChatManager.getDefault().getCurrentPartner().id, offset);
        if (list.size() <= 0) {
            mListView.setSelection(0);
            mListView.onRefreshCompleteHeader();
            return;
        }
        listMsg.addAll(0, list);
        offset = listMsg.size();
        mListView.onRefreshCompleteHeader();
        mLvAdapter.notifyDataSetChanged();
        mListView.setSelection(list.size());
    }


    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //让输入框获取焦点
                mInputView.fetchFocus();
            }
        }, 100);

    }

    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mInputView.hideInputSection(this)){

            }else{
                SpeechReader.getDefault().stopSpeech();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化titlebar，该方法只有在标题栏布局符合此规则时才能调用
     * @param left titlebar左按钮
     * @param title titlebar标题
     * @param right titlebar 右按钮
     * @param onClickListener 左右按钮点击事件
     */
    public void initTitleBar(String left,String title,String right,View.OnClickListener onClickListener){
        tv_left=(TextView) findViewById(R.id.tv_left);//返回按钮
        tv_title=(TextView) findViewById(R.id.tv_title);//标题
        tv_right=(TextView) findViewById(R.id.tv_right);//更多(右侧)按钮
        pb=(ProgressBar) findViewById(R.id.pb);// 标题栏数据加载ProgressBar

        if(!TextUtils.isEmpty(left)){
            tv_left.setText(left);
            tv_left.setVisibility(View.VISIBLE);
            tv_left.setOnClickListener(onClickListener);
        }

        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }

        if(!TextUtils.isEmpty(right)){
            tv_right.setText(right);
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);
    }

    /**
     * 如果子类支持点击左上角返回按钮返回，则在子类的onClick方法中需添加super.onClick(View view);
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_left){
            finish();
        }
    }
}
