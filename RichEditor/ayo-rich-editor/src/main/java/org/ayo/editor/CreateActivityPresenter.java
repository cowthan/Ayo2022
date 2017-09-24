
package org.ayo.editor;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.Map;

/**
 * Created by hujinghui on 16/3/28.
 */
public class CreateActivityPresenter {
    private Context mContext;

    private ICreateActivityUI mUI;


    public CreateActivityPresenter(Context context, ICreateActivityUI UI) {
        mContext = context;
        mUI = UI;
    }

    /**
     * 发送创建请求
     */
    public void createThread() {
        createThread(mContext, new PicturesUploader.UploadProgressListener() {
            @Override
            public void onProgress(int progress) {
                mUI.onUploadProgress(progress);
            }

            @Override
            public void onError(int errorCode, String errorReason) {
                mUI.onUploadError(errorCode, errorReason);
            }

            @Override
            public void onResponse(String result) {
                mUI.onUploadResponse(result);
            }
        }, mUI.getRequestParams(), mUI.getPicturePaths(), mUI.getUrl());
    }

    private AsyncTask mAsyncTask;

    public void createThread(final Context context,
                             final PicturesUploader.UploadProgressListener listener,
                             final Map<String, String> params, final List<String> files, final String url) {
        mAsyncTask = new AsyncTask() {
            @Override
            protected String doInBackground(Object... objs) {
                PicturesUploader uploader = new PicturesUploader(url, params, files);
                uploader.upload(context, listener);
                return null;
            }
        };
        mAsyncTask.execute();
    }

    public void cancelCreateThread() {
        if (mAsyncTask != null && !mAsyncTask.isCancelled())
            mAsyncTask.cancel(true);
    }
}
