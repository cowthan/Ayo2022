/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.app.core.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public class PermissionCenter {

    private PermissionCenter(){}

    private static final class Holder{
        private static final PermissionCenter instance = new PermissionCenter();
    }

    public static PermissionCenter getDefault(){
        return Holder.instance;
    }

    public interface OnPermissionResultCallback{
        void onResult(boolean granted, boolean fromUser);
    }


    private static final int REQUEST_STORAGE = 101;
    OnPermissionResultCallback callback;

    public void checkStorage(Activity activity, OnPermissionResultCallback callback){
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        this.callback = callback;
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            requestCameraPermission(activity, permission, callback);
        } else {
            // Camera permissions is already available, show the camera preview.
            callback.onResult(true, false);
        }
    }

    private void requestCameraPermission(Activity activity, String permission, OnPermissionResultCallback callback) {
        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
//            Log.i(TAG, "Displaying camera permission rationale to provide additional context.");

            ///这一层是一个用户友好的做法，既然用户主动拒绝过或者取消过该权限，那就应该在再次申请时多说点什么
//            Snackbar.make(mLayout, "之前申请过被拒，或者用户去设置里关闭了该权限，在这里提示",
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(PermissionMainActivity.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    REQUEST_CAMERA);
//                        }
//                    })
//                    .show();
            callback.onResult(false, true);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_STORAGE);
        }
        // END_INCLUDE(camera_permission_request)
    }


    ///----获得用户授权结果
    /**
     * Callback received when a permissions request has been completed.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_STORAGE) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
//            Log.i(TAG, "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
//                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
//                Snackbar.make(mLayout, R.string.permision_available_camera,
//                        Snackbar.LENGTH_SHORT).show();
                callback.onResult(true, true);
            } else {
//                Log.i(TAG, "CAMERA permission was NOT granted.");
//                Snackbar.make(mLayout, R.string.permissions_not_granted,
//                        Snackbar.LENGTH_SHORT).show();
                callback.onResult(false, true);

            }
            // END_INCLUDE(permission_result)

        }
    }
}
