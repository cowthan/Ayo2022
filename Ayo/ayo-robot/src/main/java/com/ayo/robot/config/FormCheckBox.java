package com.ayo.robot.config;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class FormCheckBox extends FormBase{

    public FormCheckBox(Context context) {
        super(context);
    }

    public FormCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    HorizontalScrollView radioGroup;
    LinearLayout linearLayout;

    @Override
    protected View createFormView() {
        radioGroup = new HorizontalScrollView(getContext()); // (RadioGroup) View.inflate(getContext(), R.layout.layout_form_radio, null);

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        radioGroup.addView(linearLayout);

        return radioGroup;
    }


    List<?> list = null;
    List<Object> selected = new ArrayList<>();
    public void notifyDataSetChanged(final List<?> list){
        this.list = list;
        linearLayout.removeAllViews();
        selected.clear();
        if(list == null || list.size() == 0) return;
        for(int i = 0; i < list.size(); i++){
            CheckBox btn = new CheckBox(getContext());
            btn.setTextColor(Color.parseColor("#00E5EE"));
            btn.setText(list.get(i).toString());
            btn.setId(i+1);
            linearLayout.addView(btn);
//            if(i == 0){
//                btn.setChecked(true);
//                selected.add(list.get(i));
//            }else{
//                btn.setChecked(false);
//            }
            final int p = i;
            btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!isChecked){
                        selected.remove(list.get(p));
                    }else{
                        selected.add(list.get(p));
                    }
                    if(onSelectChangedCallbacl  != null) onSelectChangedCallbacl.onSelectChange(selected);
                }
            });

        }
    }

    public List<Object> getInput(){
        return selected;
    }

    public LinearLayout getInputView(){
        return linearLayout;
    }

    public interface OnSelectChangedCallback{
        void onSelectChange(List<Object> selected);
    }

    private OnSelectChangedCallback onSelectChangedCallbacl;
    public void setOnSelectChangedCallback(OnSelectChangedCallback callback){
        this.onSelectChangedCallbacl = callback;
    }
}
