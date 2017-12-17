package org.ayo.robot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.ayo.robot.config.FormRadioGroup;
import com.ayo.robot.config.FormSeekBar;

import org.ayo.lang.Lists;

import java.util.List;


/**
 * Created by Administrator on 2016/12/16.
 */

public class PaintSettingView extends LinearLayout {

    public static PaintSettingView attach(Activity activity, View root){
        if(root instanceof FrameLayout){
            PaintSettingView v = new PaintSettingView(activity);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            ((FrameLayout) root).addView(v, lp);
            return v;
        }else{
            throw new RuntimeException("root必需是FrameLayout");
        }
    }


    public PaintSettingView(Context context) {
        super(context);
        init();
    }

    public PaintSettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PaintSettingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public FormRadioGroup form_stroke_cap;
    public FormRadioGroup form_stroke_join;
    public FormRadioGroup form_stroke_style;
    public FormSeekBar form_stroke_width;
    public FormSeekBar form_stroke_miter;
    //public FormCheckBox form_stroke_patheffect;

    private void init(){
        LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inf.inflate(R.layout.layout_paint_settings, this, true);
        setBackgroundColor(Color.parseColor("#bb000000"));
        form_stroke_cap = id(R.id.form_stroke_cap);
        form_stroke_join = id(R.id.form_stroke_join);
        form_stroke_width = id(R.id.form_stroke_width);
        form_stroke_miter = id(R.id.form_stroke_miter);
        form_stroke_style = id(R.id.form_stroke_style);
        //form_stroke_patheffect = id(form_stroke_patheffect);

        form_stroke_style.setLabel("设置style");
        form_stroke_cap.setLabel("设置stroke cap");
        form_stroke_join.setLabel("设置stroke join");
        form_stroke_width.setLabel("设置stroke width（0/100)");
        form_stroke_miter.setLabel("设置stroke miter（0/100)");
        //form_stroke_patheffect.setLabel("设置PathEffect");

        form_stroke_style.notifyDataSetChanged(Lists.newArrayList("FILL", "STROKE", "FILL_AND_STROKE"));
        form_stroke_cap.notifyDataSetChanged(Lists.newArrayList("BUTT", "ROUND", "SQUARE"));
        form_stroke_join.notifyDataSetChanged(Lists.newArrayList("BEVEL", "MITER", "ROUND"));
//        form_stroke_patheffect.notifyDataSetChanged(Lists.newArrayList("CornerPathEffect", "DiscretePathEffect", "DashPathEffect"
//            ,"PathDashPathEffect", "ComposePathEffect", "SumPathEffect", "none"));
        form_stroke_width.setSeekRange(100, 0);
        form_stroke_miter.setSeekRange(100, 0);

        form_stroke_width.getInputView().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                form_stroke_width.setLabel("设置stroke width（{progress}/100)".replace("{progress}", progress+""));
                callback.onSelectStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        form_stroke_miter.getInputView().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                form_stroke_miter.setLabel("设置stroke miter（{progress}/100)".replace("{progress}", progress+""));
                callback.onSelectStrokeMiter(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        form_stroke_style.getInputView().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == 1){
                    callback.onSelectStyle(Paint.Style.FILL);
                }else if(checkedId == 2){
                    callback.onSelectStyle(Paint.Style.STROKE);
                }else{
                    callback.onSelectStyle(Paint.Style.FILL_AND_STROKE);
                }
            }
        });

        form_stroke_join.getInputView().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == 1){
                    callback.onSelectStrokeJoin(Paint.Join.BEVEL);
                }else if(checkedId == 2){
                    callback.onSelectStrokeJoin(Paint.Join.MITER);
                }else{
                    callback.onSelectStrokeJoin(Paint.Join.ROUND);
                }
            }
        });

        form_stroke_cap.getInputView().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == 1){
                    callback.onSelectStrokeCap(Paint.Cap.BUTT);
                }else if(checkedId == 2){
                    callback.onSelectStrokeCap(Paint.Cap.ROUND);
                }else{
                    callback.onSelectStrokeCap(Paint.Cap.SQUARE);
                }
            }
        });

//        form_stroke_patheffect.setOnSelectChangedCallback(new FormCheckBox.OnSelectChangedCallback() {
//            @Override
//            public void onSelectChange(List<Object> selected) {
//                callback.onSelectPathEffect(selected);
//            }
//        });
    }

    private <T> T id(int id){
        return (T) findViewById(id);
    }

    public interface Callback{
        void onSelectStyle(Paint.Style style);
        void onSelectStrokeCap(Paint.Cap cap);
        void onSelectStrokeJoin(Paint.Join join);
        void onSelectStrokeWidth(float w);
        void onSelectStrokeMiter(float m);
        void onSelectPathEffect(List<Object> selectedList);
    }

    private Callback callback;

    public void setCallback(Callback callback){
        this.callback = callback;
    }
}
