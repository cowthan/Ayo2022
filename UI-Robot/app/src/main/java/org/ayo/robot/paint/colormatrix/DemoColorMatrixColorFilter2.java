package org.ayo.robot.paint.colormatrix;

import android.graphics.ColorMatrix;
import android.util.Pair;

import org.ayo.robot.BaseView;
import org.ayo.robot.DemoBase;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DemoColorMatrixColorFilter2 extends DemoBase {

    @Override
    protected BaseView createTestView() {
        setNotify("原图");
        return new ColorMatrixColorFilterView2(getActivity());
    }

    @Override
    protected void onViewClicked(int x, int y) {
        ColorMatrixColorFilterView2 v = (ColorMatrixColorFilterView2) getTestView();
        currentPosition++;
        if(currentPosition == colorMatrices.length){
            currentPosition = -1;
        }
        if(currentPosition == -1){
            v.setColorMatrix(null);
            setNotify("原图");
        }else{
            v.setColorMatrix(colorMatrices[currentPosition].second);
            setNotify(colorMatrices[currentPosition].first);
        }
    }

    @Override
    protected void onLeftTouchBoardMove(int x, int y, int dx, int dy) {

    }

    @Override
    protected void onRightTouchBoardMove(int x, int y, int dx, int dy) {

    }


    private int currentPosition = -1;

    public static Pair<String, ColorMatrix>[] colorMatrices = new Pair[]{
            new Pair("梦幻效果：去饱和，提高，色相矫正", new ColorMatrix(new float[] {
                    0.8587F, 0.2940F, -0.0927F, 0,
                    6.79F, 0.0821F, 0.9145F, 0.0634F,
                    0, 6.79F, 0.2019F, 0.1097F, 0.7483F,
                    0, 6.79F, 0, 0, 0, 1, 0
            })),
            new Pair<>("颜色变深", new ColorMatrix(new float[]{
                    0.5F, 0, 0, 0, 0,
                    0, 0.5F, 0, 0, 0,
                    0, 0, 0.5F, 0, 0,
                    0, 0, 0, 1, 0,
            })),
            new Pair<>("颜色变暗",  new ColorMatrix(new float[]{
                    0.5F, 0, 0, 0, 0,
                    0, 0.5F, 0, 0, 0,
                    0, 0, 0.5F, 0, 0,
                    0, 0, 0, 1, 0,
            })),
            new Pair<>("黑白照",  new ColorMatrix(new float[]{
                    0.33F, 0.59F, 0.11F, 0, 0,
                    0.33F, 0.59F, 0.11F, 0, 0,
                    0.33F, 0.59F, 0.11F, 0, 0,
                    0, 0, 0, 1, 0,
            })),
            new Pair<>("反相",  new ColorMatrix(new float[]{
                    -1, 0, 0, 1, 1,
                    0, -1, 0, 1, 1,
                    0, 0, -1, 1, 1,
                    0, 0, 0, 1, 0,
            })),

            new Pair<>("蓝变红，红变蓝",  new ColorMatrix(new float[]{
                    0, 0, 1, 0, 0,
                    0, 1, 0, 0, 0,
                    1, 0, 0, 0, 0,
                    0, 0, 0, 1, 0,
            })),

            new Pair<>("老照片",  new ColorMatrix(new float[]{
                    0.393F, 0.769F, 0.189F, 0, 0,
                    0.349F, 0.686F, 0.168F, 0, 0,
                    0.272F, 0.534F, 0.131F, 0, 0,
                    0, 0, 0, 1, 0,
            })),

            new Pair<>("去色后高对比度",  new ColorMatrix(new float[]{
                    1.5F, 1.5F, 1.5F, 0, -1,
                    1.5F, 1.5F, 1.5F, 0, -1,
                    1.5F, 1.5F, 1.5F, 0, -1,
                    0, 0, 0, 1, 0,
            })),

            new Pair<>("饱和度加强",   new ColorMatrix(new float[]{
                    1.438F, -0.122F, -0.016F, 0, -0.03F,
                    -0.062F, 1.378F, -0.016F, 0, 0.05F,
                    -0.062F, -0.122F, 1.483F, 0, -0.02F,
                    0, 0, 0, 1, 0,
            })),



    };
}
