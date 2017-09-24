package com.zebdar.tom.chat.input;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zebdar.tom.Const;
import com.zebdar.tom.FaceVPAdapter;
import com.zebdar.tom.R;
import com.zebdar.tom.chat.ChatManager;
import com.zebdar.tom.chat.MessageCenter;
import com.zebdar.tom.chat.MessageHelper;
import com.zebdar.tom.chat.menu.ChatMenuItem;
import com.zebdar.tom.chat.menu.ChatMenuManager;
import com.zebdar.tom.chat.model.IMMsg;
import com.zebdar.tom.emoji.ExpressionUtil;
import com.zebdar.tom.speech.SpeechCallback;
import com.zebdar.tom.speech.SpeechParser;

import org.ayo.core.Lang;
import org.ayo.notify.toaster.Toaster;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class InputView extends LinearLayout implements View.OnClickListener{

    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;
    LinearLayout mSectionMenu;
    private EditText input;
    private TextView send;
    private LinearLayout chat_face_container;
    private ImageView image_face;//表情图标
    private ImageView image_voice;//语音

    //表情图标每页6列4行
    private int columns = 6;
    private int rows = 4;
    //每页显示的表情view
    private List<View> views = new ArrayList<View>();
    //表情列表
    private List<String> staticFacesList;

    private Activity mActivity;


    public InputView(Context context) {
        super(context);
        init();
    }

    public InputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
//        setOrientation(LinearLayout.VERTICAL);
//        LayoutInflater.from(getContext()).inflate(R.layout.layout_input, this, true);
        View v = View.inflate(getContext(), R.layout.layout_input, null);
        this.addView(v);
        mPlaceHolderKeyboard = findViewById(R.id.placeholder_keyboard);
    }


    public void bind(Activity activity){
        mActivity = activity;

        staticFacesList = ExpressionUtil.initStaticFaces(activity);
        image_face = (ImageView) findViewById(R.id.image_face); //表情图标
        image_voice = (ImageView) findViewById(R.id.image_voice);//语音
        chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);//表情布局

        mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        mViewPager.setOnPageChangeListener(new PageChange());
        //表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        int pagesize = ExpressionUtil.getPagerCount(staticFacesList.size(), columns, rows);
        // 获取页数
        for (int i = 0; i < pagesize; i++) {
            views.add(ExpressionUtil.viewPagerItem(activity, i, staticFacesList, columns, rows, input));
            LayoutParams params = new LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mViewPager.setAdapter(mVpAdapter);
        mDotsLayout.getChildAt(0).setSelected(true);

        input = (EditText) findViewById(R.id.input_sms);
        send = (TextView) findViewById(R.id.send_sms);
        input.setOnClickListener(this);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    send.setVisibility(View.VISIBLE);
                    image_voice.setVisibility(View.GONE);
                } else {
                    send.setVisibility(View.GONE);
                    image_voice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        image_face.setOnClickListener(this);//表情按钮
        image_voice.setOnClickListener(this);//语音按钮
        send.setOnClickListener(this); // 发送

        //---------------------
        //menu相关
        //---------------------
        mSectionMenu = (LinearLayout) findViewById(R.id.section_menu);
        mSectionMenu.removeAllViews();

        List<ChatMenuItem> menus = ChatMenuManager.getDefault().getMenus();
        if(Lang.isNotEmpty(menus)){
            for(ChatMenuItem menu: menus){
                menu.bind(mActivity);
                addMenu(menu);
            }
        }

    }

    private void addMenu(final ChatMenuItem menu){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        View v = View.inflate(mActivity, R.layout.layout_menu_item, null);
        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        iv.setImageResource(menu.getIconId());

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //处理getMenuView
                View menuView = menu.getMenuView();
                if(menuView != null){

                }else{
                    //处理点击事件
                    menu.onClick();
                }

            }
        });

        v.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        mSectionMenu.addView(v, lp);
    }

    public void fetchFocus(){
        input.requestFocus();
        if (chat_face_container.getVisibility() == View.VISIBLE) {
            SoftKeyboard.hideSoftInputView(mActivity);
        }
    }

    public void setText(String text){
        input.setText(text);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        IMMsg msg = null;

        if(id == R.id.send_sms){
            String content = input.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return;
            }
            //sendMsgText(content, true);
            msg = MessageHelper.createTextMessage(content, ChatManager.getDefault().getMe().id, ChatManager.getDefault().getCurrentPartner().id, true);
            MessageCenter.getDefault().send(msg);
        }else if(id == R.id.input_sms){
            if (chat_face_container.getVisibility() == View.VISIBLE) {
                chat_face_container.setVisibility(View.GONE);
            }
        }else if(id == R.id.image_face){
            SoftKeyboard.hideSoftInputView(mActivity);//隐藏软键盘
            if (chat_face_container.getVisibility() == View.GONE) {
                chat_face_container.setVisibility(View.VISIBLE);
            } else {
                chat_face_container.setVisibility(View.GONE);
            }
        }else if(id == R.id.image_voice){
            SpeechParser.getDefault().recordAndTranslate(new SpeechCallback() {
                @Override
                public void onRecognizedOk(String textResult, String audioPath, boolean isLast) {
                    IMMsg msg = MessageHelper.createVoiceMessage(audioPath + Const.SPILT + textResult, ChatManager.getDefault().getMe().id, ChatManager.getDefault().getCurrentPartner().id, true);
                    MessageCenter.getDefault().send(msg);

                    msg = MessageHelper.createTextMessage(textResult, ChatManager.getDefault().getMe().id, ChatManager.getDefault().getCurrentPartner().id, true);
                    MessageCenter.getDefault().send(msg);


                }

                @Override
                public void onRecognizedFail(String reason) {
                    Toaster.toastShort(reason);
                }
            });
        }
    }

    private View mPlaceHolderKeyboard;

    public void onKeyboardShown(int height){
        mPlaceHolderKeyboard.setLayoutParams(Lang.newLayoutParams(mPlaceHolderKeyboard, Lang.screenWidth(), height));
    }

    public void onKeyboardHidden(int height){
        mPlaceHolderKeyboard.setLayoutParams(Lang.newLayoutParams(mPlaceHolderKeyboard, Lang.screenWidth(), 0));
    }

    public boolean hideInputSection(Activity activity){
        if (chat_face_container.getVisibility() == View.VISIBLE) {
            chat_face_container.setVisibility(View.GONE);
            return true;
        }
        SoftKeyboard.hideSoftInputView(activity);
        return false;
    }


    /**
     * 表情页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = View.inflate(getContext(), R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    /**
     * 表情页改变时，dots效果也要跟着改变
     */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }
    }

}
