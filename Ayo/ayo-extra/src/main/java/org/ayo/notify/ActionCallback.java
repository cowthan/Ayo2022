package org.ayo.notify;

/**
 * Created by Administrator on 2016/10/12.
 */

public interface ActionCallback {
    void onOk(Popable pop);
    void onCancel(Popable pop);
    void onSelected(Popable pop, int action, Object extra);
    void onAction(Popable pop, int action, Object extra);
    void onDismiss();
}
