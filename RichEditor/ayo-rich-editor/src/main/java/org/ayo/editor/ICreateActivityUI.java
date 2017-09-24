
package org.ayo.editor;

import java.util.List;
import java.util.Map;

/**
 * Created by hujinghui on 16/3/29.
 */
public interface ICreateActivityUI {
    /**
     * 请求的字符串参数
     * @return
     */
    Map<String, String> getRequestParams();

    /**
     * 图片路径列表
     * @return
     */
    List<String> getPicturePaths();

    /**
     * 请求的url
     * @return
     */
    String getUrl();

    /**
     * 上传的进度
     * @param progress
     */
    void onUploadProgress(int progress);

    /**
     * 请求失败返回的错误
     */
    void onUploadError(int errorCode, String errorReason);

    /**
     * 请求成功的回调
     * @param response
     */
    void onUploadResponse(String response);
}
