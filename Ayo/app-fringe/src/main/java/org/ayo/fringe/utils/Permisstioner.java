package org.ayo.fringe.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import org.ayo.notify.toaster.Toaster;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public class Permisstioner {

    public interface Callback{
        void onGranted(int i, String permission, boolean hasBeenGranted);

    }


    private Permisstioner(){}

    public static Permisstioner newInstance(){
        return new Permisstioner();
    }

    private Activity a;
    private String[] permissions;
    private String[] intros;
    private int requestCode = 1;
    private Callback callback;

    public Permisstioner with(Activity a){
        this.a = a;
        return this;
    }

    public Permisstioner id(int id){
        this.requestCode = id;
        return this;
    }

    public Permisstioner forPermission(String...permissions){
        this.permissions = permissions;
        return this;
    }

    public Permisstioner withSpeech(String...intros){
        this.intros = intros;
        return this;
    }

    public void ask(Callback callback){
        this.callback = callback;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            boolean hasAllPermission = true;
            for(int i = 0; i < permissions.length; i++){
                if(!checkPermission(permissions[i])){
                    hasAllPermission = false;
                    break;
                }
            }

            if (hasAllPermission) {
                for(int i = 0; i < permissions.length; i++){
                    String permission = permissions[i];
                    int result = PackageManager.PERMISSION_GRANTED;
                    if(callback != null){
                        callback.onGranted(i, permission, result == PackageManager.PERMISSION_GRANTED);
                    }
                }
            } else {
                requestPermissions();
            }
        }else{
            for(int i = 0; i < permissions.length; i++){
                String permission = permissions[i];
                int result = PackageManager.PERMISSION_GRANTED;
                if(callback != null){
                    callback.onGranted(i, permission, result == PackageManager.PERMISSION_GRANTED);
                }
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == this.requestCode) {
            for(int i = 0; i < permissions.length; i++){
                String permission = permissions[i];
                int result = grantResults[i];
                if(callback != null){
                    callback.onGranted(i, permission, result == PackageManager.PERMISSION_GRANTED);
                }
            }
        }

    }


    private boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(a, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(a, permissions[0])){
//            Snackbar.make(mLayout, R.string.permission_camera_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    REQUEST_CAMERA);
//                        }
//                    })
//                    .show();
            Toaster.toastShort(intros[0]);
            ActivityCompat.requestPermissions(a, permissions, requestCode);
        }else{
            ActivityCompat.requestPermissions(a, permissions, requestCode);
        }

    }

    public static void openSettingActivity(final Activity activity, String message) {

        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
    }

    private static void showMessageOKCancel(final Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

}
