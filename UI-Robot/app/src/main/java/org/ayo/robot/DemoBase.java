package org.ayo.robot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.ayo.sample.menu.touch.TouchBoard;


/**
 * Created by Administrator on 2016/12/19.
 */

public abstract class DemoBase extends AyoActivityAttacher {

    TextView tv_title, tv_method, tv_comment, tv_notify, tv_comment_enter, tv_setting_enter;
    FrameLayout shapeViewContainer;
    BaseView baseView;
    TouchBoard touchBoard_left;
    TouchBoard touchBoard_right;

    private boolean isCommentShow = false;
    private boolean isSettingShow = false;

    @Override
    protected int getLayoutId() {
        return R.layout.demo_base;
    }

    @Override
    protected void onCreate2(View view, @Nullable Bundle bundle) {
        tv_title = id(R.id.tv_title);
        tv_method = id(R.id.tv_method);
        tv_comment = id(R.id.tv_comment);
        tv_notify = id(R.id.tv_notify);
        tv_comment_enter = id(R.id.tv_comment_enter);
        tv_setting_enter = id(R.id.tv_setting_enter);

        shapeViewContainer = id(R.id.shapeView_container);
        touchBoard_left = id(R.id.touchBoard_left);
        touchBoard_right = id(R.id.touchBoard_right);

        baseView = createTestView();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        shapeViewContainer.addView(baseView, lp);

        baseView.setOnFingerTouchCallback(new BaseView.OnFingerTouchCallback() {
            @Override
            public void onFingerMove(int x, int y, int dx, int dy) {
                onViewTouchMove(x, y, dx, dy);
            }

            @Override
            public void onFingerDown(int x, int y) {
                onViewTouchDown(x, y);
            }

            @Override
            public void onFingerUp(int x, int y) {
                onViewTouchUp(x, y);
            }

            @Override
            public void onClick(int x, int y) {
                onViewClicked(x, y);
            }
        });

        setTitle(baseView.getTitle());
        setMethod(baseView.getMethod());
        setComment(baseView.getComment());

        touchBoard_left.setCallback(new TouchBoard.Callback() {
            @Override
            public void onDown(int x, int y) {

            }

            @Override
            public void onUp(int x, int y) {

            }

            @Override
            public void onClick() {
                onLeftTouchBoardClicked();
            }

            @Override
            public void onMove(int x, int y, int dx, int dy) {
                onLeftTouchBoardMove(x, y, dx, dy);
            }
        });

        touchBoard_right.setCallback(new TouchBoard.Callback() {
            @Override
            public void onDown(int x, int y) {

            }

            @Override
            public void onUp(int x, int y) {

            }

            @Override
            public void onClick() {
                onRightTouchBoardClicked();
            }

            @Override
            public void onMove(int x, int y, int dx, int dy) {
                onRightTouchBoardMove(x, y, dx, dy);
            }
        });

        ///-----设置
        final View root = findViewById(R.id.body);
        final PaintSettingView paintSettingView = DemoShapeMgmr.attach(getActivity(), root, getTestView());

        ///-----------笔记
        ///tv_comment.setVisibility(View.GONE);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_comment.animate().translationY(-tv_comment.getHeight());
                paintSettingView.animate().translationY(-paintSettingView.getHeight());
            }
        }, 200);
        tv_comment_enter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tv_comment.animate().translationY(0);
                isCommentShow = true;
            }
        });
        tv_setting_enter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                paintSettingView.animate().translationY(0);
                isSettingShow = true;
            }
        });

        root.findViewById(R.id.body).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isCommentShow){
                    tv_comment.animate().translationYBy(-tv_comment.getHeight());
                    isCommentShow = false;
                }
                if(isSettingShow){
                    paintSettingView.animate().translationYBy(-paintSettingView.getHeight());
                    isSettingShow = false;
                }
            }
        });


        setTestViewBackgroundStroke();
    }

    @Override
    protected void onDestroy2() {

    }

    @Override
    protected void onPageVisibleChanged(boolean b, boolean b1, @Nullable Bundle bundle) {

    }


    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy){}
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy){}
    protected void onLeftTouchBoardClicked(){}
    protected void onRightTouchBoardClicked(){}

    protected void onViewTouchDown(int x, int y){}
    protected void onViewTouchUp(int x, int y){}
    protected void onViewTouchMove(int x, int y, int dx, int dy){}
    protected void onViewClicked(int x, int y){}

    protected abstract BaseView createTestView();

    protected void enableLeftTouchBoard(boolean enable){
        touchBoard_left.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    protected void enableRightTouchBoard(boolean enable){
        touchBoard_right.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    protected void setTestViewBackgroundStroke(){
        shapeViewContainer.setBackgroundResource(R.drawable.round_rect);
    }

    protected void setTestViewBackgroundFill(int bgColor){
        shapeViewContainer.setBackgroundColor(bgColor);
    }

    protected <T extends BaseView> T getTestView(){
        return (T) baseView;
    }

    protected void setNotify(String s){
        tv_notify.setText(s);
    }

    private void setTitle(String s){
        tv_title.setText(s);
    }

    private void setMethod(String s){
        tv_method.setText(s);
    }

    private void setComment(String s){
        tv_comment.setText(s);
    }
}
