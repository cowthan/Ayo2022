
package org.ayo.social.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 请求微信access_token的数据实体 Created by guojun on 16/5/4 13:48.
 */
public class WeiChatAccessTokenEntity implements Parcelable {
    private String access_token;

    private int expires_in;

    @Override
    public String toString() {
        return "WeiChatAccessTokenEntity{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }

    private String refresh_token;

    private String openid;

    private String scope;

    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.access_token);
        dest.writeInt(this.expires_in);
        dest.writeString(this.refresh_token);
        dest.writeString(this.openid);
        dest.writeString(this.scope);
        dest.writeString(this.unionid);
    }

    public WeiChatAccessTokenEntity() {
    }

    protected WeiChatAccessTokenEntity(Parcel in) {
        this.access_token = in.readString();
        this.expires_in = in.readInt();
        this.refresh_token = in.readString();
        this.openid = in.readString();
        this.scope = in.readString();
        this.unionid = in.readString();
    }

    public static final Creator<WeiChatAccessTokenEntity> CREATOR = new Creator<WeiChatAccessTokenEntity>() {
        @Override
        public WeiChatAccessTokenEntity createFromParcel(Parcel source) {
            return new WeiChatAccessTokenEntity(source);
        }

        @Override
        public WeiChatAccessTokenEntity[] newArray(int size) {
            return new WeiChatAccessTokenEntity[size];
        }
    };
}
