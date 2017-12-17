/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 daimajia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.ayo.animate.yoyo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.view.View;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 悠悠球，YoYo类是核心类，它只有with方法对外提供创建和启动动画的功能
 * 动画的创建是通过内部的Builder来实现，对动画的控制是通过YoYoString来实现
 */
public class YoYo {

    //move the duplicate fields to Builder class, so YoYo is much simpler

    //YoYo类不可以new，只能通过with方法来创建，可以使用已有的某个Techniques，也可以使用自定义的animator，但是要继承自YoYoAnimator
    public static Builder with(Techniques technique) {
        return new Builder(technique);
    }

    public static Builder with(YoYoAnimator animator) {
        return new Builder(animator);
    }

    /**
     * 创建悠悠球的构造器
     * <p/>
     * YoYo builder
     */
    public static class Builder {

        private static final int DEFAULT_REPEAT = 1;//动画默认只播放一次
        private static final long DEFAULT_DELAY = 0;//动画默认没有启动延时
        private static final boolean DEFAULT_RESET = false;//动画结束默认不恢复原状
        private static final long DEFAULT_DURATION = 1000;//动画时间间隔默认是1秒

        private View target;//作用的view
        private long delay = DEFAULT_DELAY;//动画启动延迟
        private long duration = DEFAULT_DURATION;//动画持续时长
        private int repeat = DEFAULT_REPEAT;//动画重复次数
        private boolean reset = DEFAULT_RESET;//动画结束之后是否恢复到原来的状态，true表示恢复
        private YoYoAnimator animator;//animator类
        private Interpolator interpolator;//interpolator 时间插值类
        private TypeEvaluator<?> evaluator;//interpolator 时间插值类
        private List<Animator.AnimatorListener> listeners = new ArrayList<Animator.AnimatorListener>();//动画的监听器

        //两个构造函数都将使得animator成为一个非空对象
        private Builder(Techniques technique) {
            this.animator = technique.getAnimator();
        }

        private Builder(YoYoAnimator animator) {
            this.animator = animator;
        }

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder delay(long delay) {
            this.delay = delay;
            return this;
        }

        public Builder interpolate(Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }
        public Builder evaluator(TypeEvaluator<?> evaluator) {
            this.evaluator = evaluator;
            return this;
        }

        public Builder listen(AnimatorListenerAdapter listener) {
            this.listeners.add(listener);
            return this;
        }

        public Builder reset(boolean reset) {
            this.reset = reset;
            return this;
        }

        public Builder repeat(int count) {
            this.repeat = count;
            return this;
        }

        //调用play表示动画正式开始，返回结果是可以控制动画的对象YoYoString
        public YoYoString playOn(View target) {
            this.target = target;
            start();
            return new YoYoString(this.animator);
        }

        public Animator getAnimator(View target) {
            this.target = target;
            animator.setTarget(target)
                    .setDuration(duration)
                    .setInterpolator(interpolator)
                    .setStartDelay(delay).setRest(reset).setRepeat(repeat);
            return animator.getRawAnimator();
        }

        //builder内部的start方法，将animatorset启动
        private void start() {
            animator.setTarget(target)
                    .setDuration(duration)
                    .setInterpolator(interpolator)
                    .setStartDelay(delay).setRest(reset).setRepeat(repeat);

            if (listeners.size() > 0) {
                animator.addAllListeners(listeners);
            }

            animator.start();
        }
    }

    /**
     * 可以用来控制悠悠球的悠悠绳
     * <p/>
     * YoYo string, you can use this string to control your YoYo.
     */
    public static class YoYoString {

        private YoYoAnimator animator;

        private YoYoString(YoYoAnimator animator) {
            this.animator = animator;
        }

        public boolean isStarted() {
            return animator.isStarted();
        }

        public boolean isRunning() {
            return animator.isRunning();
        }

        public void stop(boolean reset) {
            animator.cancel();

            if (reset) animator.reset();
        }
    }

}
