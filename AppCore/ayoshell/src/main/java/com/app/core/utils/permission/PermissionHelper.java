package com.app.core.utils.permission;

import android.app.Activity;

import org.ayo.core.Lang;
import org.ayo.log.Trace;

import java.util.List;

/**
 * Created by qiaoliang on 2018/1/12.
 */

public class PermissionHelper {

    public static void requestStorage(Activity activity, final OnPermissionGrantedListener listener) {
        request(activity, listener, PermissionConstants.STORAGE);
    }

    public static void requestPhone(Activity activity, final OnPermissionGrantedListener listener) {
        request(activity, listener, PermissionConstants.PHONE);
    }

    public static void requestPhone(Activity activity, final OnPermissionGrantedListener grantedListener,
                                    final OnPermissionDeniedListener deniedListener) {
        request(activity, grantedListener, deniedListener, PermissionConstants.PHONE);
    }

    public static void requestSms(Activity activity, final OnPermissionGrantedListener listener) {
        request(activity, listener, PermissionConstants.SMS);
    }

    public static void requestLocation(Activity activity, final OnPermissionGrantedListener listener) {
        request(activity, listener, PermissionConstants.LOCATION);
    }

    private static void request(Activity activity, final OnPermissionGrantedListener grantedListener,
                                final @PermissionConstants.Permission String... permissions) {
        request(activity, grantedListener, null, permissions);
    }

    public static void request(final Activity activity, final OnPermissionGrantedListener grantedListener,
                                final OnPermissionDeniedListener deniedListener,
                                final @PermissionConstants.Permission String... permissions) {
        PermissionUtils.permission(permissions)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(ShouldRequest shouldRequest) {
                        DialogHelper.showRationaleDialog(activity, shouldRequest);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        if (grantedListener != null) {
                            grantedListener.onPermissionGranted();
                        }
                        Trace.d("permission", Lang.fromList(permissionsGranted, ", ", false, new Lang.StringConverter<String>() {
                            @Override
                            public String convert(String s) {
                                return s;
                            }
                        }));
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog(activity);
                        }
                        if (deniedListener != null) {
                            deniedListener.onPermissionDenied();
                        }
//                        LogUtils.d(permissionsDeniedForever, permissionsDenied);
                    }
                })
                .request();
    }

    public interface OnPermissionGrantedListener {
        void onPermissionGranted();
    }

    public interface OnPermissionDeniedListener {
        void onPermissionDenied();
    }
}