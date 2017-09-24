package org.ayo.colorpicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.ayo.colorpicker.ColorPicker;
import org.ayo.colorpicker.OpacityBar;
import org.ayo.colorpicker.R;
import org.ayo.colorpicker.SVBar;
import org.ayo.colorpicker.SaturationBar;
import org.ayo.colorpicker.ValueBar;

/**
 * Created by Administrator on 2016/12/31.
 */

public class ColorPickerActivity extends AppCompatActivity {

    private static int REQ_CODE = 1534;
    public static final int INVALID_COLOR = -1;

    public static void startForResult(Activity a, int oldColor){
        Intent intent = new Intent(a, ColorPickerActivity.class);
        intent.putExtra("old", oldColor);
        a.startActivityForResult(intent, REQ_CODE);
    }

    public static boolean isRequestToMe(int requesetCode){
        return requesetCode == REQ_CODE;
    }

    public static int getColor(Intent data){
        if(data != null && data.hasExtra("color")){
            return data.getIntExtra("color", INVALID_COLOR);
        }else{
            return INVALID_COLOR;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayo_ac_color_picker);

        final ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        final SVBar svbar = (SVBar) findViewById(R.id.svbar);
        final OpacityBar opacitybar = (OpacityBar) findViewById(R.id.opacitybar);
        final SaturationBar saturationbar = (SaturationBar) findViewById(R.id.saturationbar);
        final  ValueBar valuebar = (ValueBar) findViewById(R.id.valuebar);

        picker.addSVBar(svbar);
        picker.addOpacityBar(opacitybar);
        picker.addSaturationBar(saturationbar);
        picker.addValueBar(valuebar);

        final Button btn_ok = (Button) findViewById(R.id.btn_ok);

        int oldColor = getIntent().getIntExtra("old", 0);
        picker.setOldCenterColor(oldColor);
        picker.setShowOldCenterColor(true);

        picker.setColor(oldColor);

        CheckBox cb_old_color = (CheckBox) findViewById(R.id.cb_old_color);
        cb_old_color.setChecked(true);
        cb_old_color.setText("确定：" + parse(oldColor));
        cb_old_color.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                picker.setShowOldCenterColor(isChecked);
            }
        });

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                btn_ok.setBackgroundColor(picker.getColor());
                btn_ok.setText("确定--" + parse(picker.getColor()));
            }
        });

        opacitybar.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
            @Override
            public void onOpacityChanged(int opacity) {

            }
        });
        valuebar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {

            }
        });
        saturationbar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(int saturation) {

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int color = picker.getColor();
                Intent intent = new Intent();
                intent.putExtra("color", color);
                setResult(200, intent);
                finish();
            }
        });

    }

    public static String parse(int color) {
        String s = "#";
        s += Integer.toHexString(Color.alpha(color));
        s += Integer.toHexString(Color.red(color));
        s += Integer.toHexString(Color.green(color));
        s += Integer.toHexString(Color.blue(color));
        return s;
    }
}
