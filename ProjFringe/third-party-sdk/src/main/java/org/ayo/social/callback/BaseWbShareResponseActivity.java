package org.ayo.social.callback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import org.ayo.social.SocialCenter;

/**
 * Created by qiaoliang on 2017/6/21.
 */

public class BaseWbShareResponseActivity extends Activity implements IWeiboHandler.Response {
    private IWeiboShareAPI mWeiboShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, SocialCenter.getDefault().appIdWb);
        Intent intent = getIntent();
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this); // 当前应用唤起微博分享后,返回当前应用
    }

    @Override
    public void onResponse(BaseResponse baseResp) {// 接收微客户端博请求的数据。

        if(SocialCenter.getDefault().getShareCallback() != null){
            if(baseResp.errCode == WBConstants.ErrorCode.ERR_OK){
                SocialCenter.getDefault().getShareCallback().onSuccess();
            }else if(baseResp.errCode == WBConstants.ErrorCode.ERR_CANCEL){
                SocialCenter.getDefault().getShareCallback().onCancel();
            }else if(baseResp.errCode == WBConstants.ErrorCode.ERR_FAIL){
                SocialCenter.getDefault().getShareCallback().onFail(null, baseResp.errMsg);
            }else{
                SocialCenter.getDefault().getShareCallback().onFail(null, "未知原因");
            }
            SocialCenter.getDefault().setShareCallback(null);
            finish();
            return;
        }
    }
}
