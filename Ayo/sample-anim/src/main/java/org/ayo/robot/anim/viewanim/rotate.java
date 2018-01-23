package org.ayo.robot.anim.viewanim;


import android.view.View;
import android.view.animation.Animation;

import org.ayo.animate.TweenAnimation;
import org.ayo.robot.anim.DemoBaseForViewAnim;

public class rotate extends DemoBaseForViewAnim {

    @Override
    protected String getType() {
        return "rotate";
    }

    @Override
    protected float getMinValue() {
        return 0;
    }

    @Override
    protected float getMaxValue() {
        return 0;
    }

    @Override
    protected float getDefaultFrom() {
        return 0;
    }

    @Override
    protected float getDefaultTo() {
        return 0;
    }

    @Override
    protected float parseProgress(int progress) {
        return super.parseProgress(progress);
    }

    @Override
    protected Animation createAnimator(View target) {
        return TweenAnimation.rotate();
    }

    @Override
    protected View createTestView() {
        return null;
    }
}