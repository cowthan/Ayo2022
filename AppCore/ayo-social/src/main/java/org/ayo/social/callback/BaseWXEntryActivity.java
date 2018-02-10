package org.ayo.social.callback;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.ayo.log.Trace;
import org.ayo.social.SocialCenter;

/**
 * Created by qiaoliang on 2017/6/21.
 */

public class BaseWXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String TAG="WXEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, SocialCenter.getDefault().appIdWx, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        this.finish();
    }

    @Override
    public void onResp(BaseResp arg0) {
        if (arg0.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//处理微信登录
            this.finish();
            SendAuth.Resp sendAuth = (SendAuth.Resp) arg0;
            if(sendAuth.errCode==BaseResp.ErrCode.ERR_OK){
                Trace.d(TAG," error code ==ERR_OK   sendAuth code="+sendAuth.code);
                //AppService.getWeiChatAccessToken(getApplicationContext(), sendAuth.code);
                SocialCenter.getDefault().fetchWechatUserInfo(this, sendAuth.code);
            }else if(sendAuth.errCode==BaseResp.ErrCode.ERR_USER_CANCEL){
                Trace.d(TAG,"error code ==ERR_USER_CANCEL=="+sendAuth.errCode);
                if(SocialCenter.getDefault().getShareCallback() != null){
                    SocialCenter.getDefault().getShareCallback().onCancel();
                    SocialCenter.getDefault().setShareCallback(null);
                }
            }else {
                Trace.d(TAG,"error code ==ERR_AUTH_DENIED"+sendAuth.errCode+"   ");
                if(SocialCenter.getDefault().getShareCallback() != null){
                    SocialCenter.getDefault().getShareCallback().onFail(null, "授权失败");
                    SocialCenter.getDefault().setShareCallback(null);
                }
            }
        } else {//处理微信分享
            if(SocialCenter.getDefault().getShareCallback() != null){
                if(arg0.errCode == BaseResp.ErrCode.ERR_OK){
                    if(SocialCenter.getDefault().getShareCallback() != null){
                        SocialCenter.getDefault().getShareCallback().onSuccess();
                        SocialCenter.getDefault().setShareCallback(null);
                    }
                }else if(arg0.errCode == BaseResp.ErrCode.ERR_USER_CANCEL){
                    if(SocialCenter.getDefault().getShareCallback() != null){
                        SocialCenter.getDefault().getShareCallback().onCancel();
                        SocialCenter.getDefault().setShareCallback(null);
                    }
                }else if(arg0.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED){
                    if(SocialCenter.getDefault().getShareCallback() != null){
                        SocialCenter.getDefault().getShareCallback().onFail(null, "授权失败");
                        SocialCenter.getDefault().setShareCallback(null);
                    }
                }else{
                    if(SocialCenter.getDefault().getShareCallback() != null){
                        SocialCenter.getDefault().getShareCallback().onFail(null, "未知原因");
                        SocialCenter.getDefault().setShareCallback(null);
                    }
                }
                SocialCenter.getDefault().setShareCallback(null);
                finish();
                return;
            }
        }
    }

}