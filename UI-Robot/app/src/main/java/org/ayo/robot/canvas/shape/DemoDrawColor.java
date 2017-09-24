package org.ayo.robot.canvas.shape;

import android.content.Intent;

import org.ayo.colorpicker.ui.ColorPickerActivity;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;
import org.ayo.sample.menu.notify.ToasterDebug;

/**
 * Created by Administrator on 2016/12/15.
 */

public class DemoDrawColor extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("Color.Yellow");
        enableLeftTouchBoard(false);
        enableRightTouchBoard(false);
        setTestViewBackgroundStroke();
        return new ColorView(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {
        ColorPickerActivity.startForResult(getActivity(), getTestView().getPaint().getColor());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(ColorPickerActivity.isRequestToMe(requestCode)){
            int color = ColorPickerActivity.getColor(data);
            if(color != ColorPickerActivity.INVALID_COLOR){
                setNotify(ColorPickerActivity.parse(color));
                ColorView v = (ColorView) getTestView();
                v.setPaintColor(color);
            }else{
                ToasterDebug.toastShort("非法color--也就是取消选择了");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
