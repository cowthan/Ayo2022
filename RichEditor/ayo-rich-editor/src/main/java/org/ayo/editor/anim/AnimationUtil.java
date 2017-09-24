
package org.ayo.editor.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

/**
 * Created by lishuai on 15/11/18.
 */
public class AnimationUtil {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void showAction(View view, float values, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", values, 0f)
                .setDuration(200);
        animator.addListener(listener);
        animator.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void closeAction(View view, float values, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0f, values)
                .setDuration(200);
        animator.addListener(listener);
        animator.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void showAction(View view, float values) {
        ObjectAnimator.ofFloat(view, "translationY", values, 0f).setDuration(200).start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void closeAction(View view, float values) {
        ObjectAnimator.ofFloat(view, "translationY", 0f, values).setDuration(200).start();
    }

}
