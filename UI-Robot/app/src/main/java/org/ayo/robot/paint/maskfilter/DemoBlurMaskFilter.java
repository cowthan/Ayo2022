package org.ayo.robot.paint.maskfilter;

import android.graphics.BlurMaskFilter;
import android.util.Pair;

import org.ayo.observe.Observable;
import org.ayo.observe.Observer;
import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoBlurMaskFilter extends DemoBase {

    private String radius = "radius = 20";
    private String blur = "No BlurMasterFilter";

    @Override
    protected BaseView createTestView() {
        setNotify(blur + "   " + radius);
        BlurMaskFilterView v =  new BlurMaskFilterView(getActivity());
        v.getObservable().addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object src, Object data) {
                radius = data.toString();
                setNotify(blur + "   " + radius);
            }
        });
        return v;
    }

    private int currentPosition = -1;

    @Override
    protected void onViewClicked(int x, int y) {
        currentPosition += 1;
        if(currentPosition == blurs.length) currentPosition = -1;
        BlurMaskFilterView v = (BlurMaskFilterView) getTestView();
        if(currentPosition == -1){
            v.setBlur(null);
            blur = "No BlurMasterFilter";
            setNotify(blur + "   " + radius);
        }else{
            v.setBlur(blurs[currentPosition].second);
            blur = blurs[currentPosition].first;
            setNotify(blur + "   " + radius);
        }
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {
        BlurMaskFilterView v = (BlurMaskFilterView) getTestView();
        v.changeRadiusBy(dx);
    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {

    }

    public static Pair<String, BlurMaskFilter.Blur>[] blurs = new Pair[]{
            new Pair("BlurMaskFilter.Blur.INNER", BlurMaskFilter.Blur.INNER),
            new Pair("BlurMaskFilter.Blur.OUTER", BlurMaskFilter.Blur.OUTER),
            new Pair("BlurMaskFilter.Blur.NORMAL", BlurMaskFilter.Blur.NORMAL),
            new Pair("BlurMaskFilter.Blur.SOLID", BlurMaskFilter.Blur.SOLID),
    };

}
