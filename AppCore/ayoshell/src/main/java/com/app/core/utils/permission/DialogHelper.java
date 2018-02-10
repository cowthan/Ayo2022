package com.app.core.utils.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by qiaoliang on 2018/1/12.
 */

public class DialogHelper {

    public static void showRationaleDialog(Activity activity, final PermissionUtils.OnRationaleListener.ShouldRequest shouldRequest) {
//        Activity topActivity = ActivityUtils.getTopActivity();
//        if (topActivity == null) return;
        new AlertDialog.Builder(activity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage("您拒绝了授权，麻烦您还是通过吧，要不应用根本用不了")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shouldRequest.again(true);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shouldRequest.again(false);
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }

    public static void showOpenAppSettingDialog(Activity activity) {
//        Activity topActivity = ActivityUtils.getTopActivity();
//        if (topActivity == null) return;
        new AlertDialog.Builder(activity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage("您或系统拒绝了我们的权限申请，麻烦您还是通过吧，要不用不了了")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.openAppSettings();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

}
