package org.ayo.appsist;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.github.moduth.blockcanary.ui.DisplayView;

import org.ayo.hover.wrapper.R;

import java.io.FileInputStream;
import java.io.IOException;

import io.mattcarroll.hover.Navigator;
import io.mattcarroll.hover.NavigatorContent;
import io.mattcarroll.hover.hoverdemo.introduction.HoverMotion;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public class AssistBlockView extends FrameLayout implements NavigatorContent {


    public AssistBlockView(Context context) {
        super(context);
        init();
    }

    public AssistBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AssistBlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AssistBlockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private HoverMotion mHoverMotion;
    View iv_refresh;
    Button btn_test_block_1, btn_test_block_2, btn_test_block_3;
    Button btn_block;
    DisplayView viewBlock;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.ass_view_block, this, true);

        mHoverMotion = new HoverMotion();
        iv_refresh =  findViewById( R.id.iv_refresh );

        btn_test_block_1 = (Button) findViewById(R.id.btn_test_block_1);
        btn_test_block_2 = (Button) findViewById(R.id.btn_test_block_2);
        btn_test_block_3 = (Button) findViewById(R.id.btn_test_block_3);
        btn_block = (Button) findViewById(R.id.btn_block);
        btn_test_block_1.setOnClickListener(onClickListener);
        btn_test_block_2.setOnClickListener(onClickListener);
        btn_test_block_3.setOnClickListener(onClickListener);

        viewBlock = (DisplayView) findViewById(R.id.viewBlock);

        //卡多久算卡顿？
        //block信息存在哪儿了？

        btn_block.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(viewBlock.getVisibility() == View.VISIBLE){
                    viewBlock.backToList();
                }else{
                    viewBlock.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.btn_test_block_1){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("a", "onClick of R.id.btn_test_block_1: ", e);
                }
            }else if(id == R.id.btn_test_block_2){
                for (int i = 0; i < 100; ++i) {
                    readFile();
                }
            }else if(id == R.id.btn_test_block_3){
                double result = compute();
                System.out.println(result);
            }
        }
    };

    private static double compute() {
        double result = 0;
        for (int i = 0; i < 1000000; ++i) {
            result += Math.acos(Math.cos(i));
            result -= Math.asin(Math.sin(i));
        }
        return result;
    }

    private static void readFile() {
        FileInputStream reader = null;
        try {
            reader = new FileInputStream("/proc/stat");
            while (reader.read() != -1) ;
        } catch (IOException e) {
            Log.e("a", "readFile: /proc/stat", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("a", " on close reader ", e);
                }
            }
        }
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onShown(@NonNull Navigator navigator) {
        mHoverMotion.start(iv_refresh);
        viewBlock.backToList();
    }

    @Override
    public void onHidden() {
        mHoverMotion.stop();
    }



}
