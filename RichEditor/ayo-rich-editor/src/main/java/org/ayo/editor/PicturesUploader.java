
package org.ayo.editor;


import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * 文件上传 Created by hujinghui on 16/3/29.
 */
public class PicturesUploader {
	private final String TAG = "PicturesUploader";

	/**
	 * 传递的参数
	 */
	private Map<String, String> mParams;

	/**
	 * 请求接口
	 */
	private String mUrl;

	/**
	 * 图片地址列表
	 */
	private List<String> mPictures;

	/**
	 * 已经上传完成的总文件长度
	 */
	private long mUploadLength;

	/**
	 * 上传的文件的索引
	 */
	private int mIndex;

	/**
	 * 文件长度
	 */
	private List<Long> mSizes;

	/**
	 * @param url 请求的接口
	 * @param params 传递的字符串参数
	 * @param pics 上传的文件地址列表
	 */
	public PicturesUploader(String url, Map<String, String> params, List<String> pics) {
		this.mParams = params;
		this.mUrl = url;
		this.mPictures = pics;
	}

	/**
	 * @param context
	 * @param listener 回调接口
	 */
	public void upload(Context context, final UploadProgressListener listener) {
//		SimpleMultiPartXRequest req = new SimpleMultiPartXRequest(mUrl, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				listener.onResponse(response);
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				listener.onError(AppUtils.getErrorEntity(error));
//			}
//		});
//		req.setProgressListener(new Response.ProgressListener() {
//			@Override
//			public void onProgress(long transferredBytes, long totalSize) {
//				Trace.d(TAG, "onProgress:" + transferredBytes + "	" + totalSize);
//			}
//		});
//		req.setHeaders(AppUtils.getOAuthMap(context));
//		if (mParams != null && !mParams.isEmpty()) {
//			Iterator<String> it = mParams.keySet().iterator();
//			String key;
//			String value;
//			while (it.hasNext()) {
//				key = it.next();
//				if (TextUtils.isEmpty(key))
//					continue;
//				value = mParams.get(key);
//				if (TextUtils.isEmpty(value))
//					continue;
//				req.addMultipartParam(key, "text/*", value);
//			}
//		}
//		if (mPictures != null && !mPictures.isEmpty()) {
//			Iterator<String> it = mPictures.iterator();
//			String value;
//			int i = 1;
//			MultiPartXRequest.MultiFileParam param;
//			while (it.hasNext()) {
//				value = it.next();
//				String extension = AppUtils.getFileExtensionByFileName(value);
//				if (TextUtils.isEmpty(value))
//					continue;
//				param = new MultiPartXRequest.MultiFileParam(value, "p" + i, "{{p" + i + "}}." + extension,
//						"image/*", "UTF-8");
//				i++;
//				req.addMultiFileParam(param);
//			}
//		}
//
//		HttpTools.getDefault().addToRequestQueue(req);
	}

	public interface UploadProgressListener {
		int PROGRESS_INDEFINITE = -1;

		void onProgress(int progress);

		void onError(int errorCode, String errorReason);

		void onResponse(String result);
	}
}
