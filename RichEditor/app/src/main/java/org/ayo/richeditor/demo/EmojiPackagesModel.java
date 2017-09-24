
package org.ayo.richeditor.demo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lhy on 15/11/7.
 */
public class EmojiPackagesModel implements Parcelable {
    public int id;

    public String name;

    public int ecount;

    public String icon;

    public String pkg;

    public String note;

    public String deleted_at;

    public long version;
    /**
     * type : 1 大表情 其它:小表情
     */
    public int type;

    public int sort;
    /**
     * 作用范围(0-全局; 1-聊天室)
     */
    public int scenario;

    public int retry;

    public EmojiPackagesModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    public int getSort() {
        return sort;
    }

    public int getScenario() {
        return scenario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEcount() {
        return ecount;
    }

    public void setEcount(int ecount) {
        this.ecount = ecount;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.ecount);
        dest.writeString(this.icon);
        dest.writeString(this.pkg);
        dest.writeString(this.note);
        dest.writeString(this.deleted_at);
        dest.writeLong(this.version);
        dest.writeInt(this.type);
        dest.writeInt(this.sort);
        dest.writeInt(this.scenario);
        dest.writeInt(this.retry);
    }

    protected EmojiPackagesModel(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ecount = in.readInt();
        this.icon = in.readString();
        this.pkg = in.readString();
        this.note = in.readString();
        this.deleted_at = in.readString();
        this.version = in.readLong();
        this.type = in.readInt();
        this.sort = in.readInt();
        this.scenario = in.readInt();
        this.retry = in.readInt();
    }

    public static final Creator<EmojiPackagesModel> CREATOR = new Creator<EmojiPackagesModel>() {
        @Override
        public EmojiPackagesModel createFromParcel(Parcel source) {
            return new EmojiPackagesModel(source);
        }

        @Override
        public EmojiPackagesModel[] newArray(int size) {
            return new EmojiPackagesModel[size];
        }
    };
}
