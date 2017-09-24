
package org.ayo.editor.emoj;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by liujinhua on 15/10/25.
 */
public class SoftKeyBoardListener {
    private View rootView;// activity的根视图

    int rootViewVisibleHeight;// 纪录根视图的显示高度

    private Vector<SoftReference<OnSoftKeyBoardChangeListener>> listeners;

    public SoftKeyBoardListener(Activity activity) {
        // 获取activity的根视图
        rootView = activity.getWindow().getDecorView();

        // 监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // 获取当前根视图在屏幕上显示的大小
                        Rect r = new Rect();
                        rootView.getWindowVisibleDisplayFrame(r);
                        int visibleHeight = r.height();
                        if (rootViewVisibleHeight == 0) {
                            rootViewVisibleHeight = visibleHeight;
                            return;
                        }

                        // 根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                        if (rootViewVisibleHeight == visibleHeight) {
                            return;
                        }

                        // 根视图显示高度变小超过200，可以看作软键盘显示了
                        if (rootViewVisibleHeight - visibleHeight > 200) {
                            if (listeners != null) {
                                Iterator<SoftReference<OnSoftKeyBoardChangeListener>> it = listeners
                                        .iterator();
                                while (it.hasNext()) {
                                    SoftReference<OnSoftKeyBoardChangeListener> ref = it.next();
                                    if (ref != null) {
                                        if (ref.get() != null) {
                                            ref.get().keyBoardShow(
                                                    rootViewVisibleHeight - visibleHeight);
                                            continue;
                                        }
                                    }
                                    it.remove();
                                }
                            }
                            rootViewVisibleHeight = visibleHeight;
                            return;
                        }

                        // 根视图显示高度变大超过200，可以看作软键盘隐藏了
                        if (visibleHeight - rootViewVisibleHeight > 200) {
                            Iterator<SoftReference<OnSoftKeyBoardChangeListener>> it = listeners
                                    .iterator();
                            while (it.hasNext()) {
                                SoftReference<OnSoftKeyBoardChangeListener> ref = it.next();
                                if (ref != null) {
                                    if (ref.get() != null) {
                                        ref.get().keyBoardHide(
                                                visibleHeight - rootViewVisibleHeight);
                                        continue;
                                    }
                                }
                                it.remove();
                            }
                            rootViewVisibleHeight = visibleHeight;
                            return;
                        }

                    }
                });
    }

    private void setOnSoftKeyBoardChangeListener(
            OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        if (listeners == null)
            listeners = new Vector<>();
        listeners.add(new SoftReference<>(onSoftKeyBoardChangeListener));
    }

    public interface OnSoftKeyBoardChangeListener {
        void keyBoardShow(int height);

        void keyBoardHide(int height);
    }

    public OnSoftKeyBoardChangeListener setListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
        return onSoftKeyBoardChangeListener;
    }

    public void deleteListener(OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        if (listeners != null) {
            for (int i = 0, j = listeners.size(); i < j; i++) {
                SoftReference<OnSoftKeyBoardChangeListener> ref = listeners.get(i);
                if (ref != null && ref.get() != null && ref.get() == onSoftKeyBoardChangeListener) {
                    ref.clear();
                    break;
                }

            }
        }
    }
}
