
package org.ayo.imagepicker;

import android.content.ContentUris;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

/**
 * Created by hujinghui on 14/11/15.
 */
public class ThumbModel implements Comparable<ThumbModel>, Parcelable {

    public interface ThumbType {
        /**
         * 本地相册
         */
        int ALBUM = 0;

        /**
         * 本地视频
         */
        int LOCAL_VIDEOS = 1;

        /**
         * 相机
         */
        int CAMERA = 2;
    }

    public int bucketId;

    public int id;

    public String path;

    public String fileName;

    public String content;

    /**
     * 视频时长
     */
    public long duration;

    /**
     * 视频类型
     */
    public String videoType;

    public int videoWidth;

    public int videoHeight;

    public boolean isVideo;

    public long videoSize;

    public int orientation;

    public boolean isSelect;

    public int type;

    public int uiType;

    /**
     * 视频地址
     */
    public String videoUrl;

    public String thumb;

    private String source;

    private String image;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ThumbModel() {
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUri() {
        if (bucketId > 0)
            return ContentUris
                    .withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, bucketId)
                    .toString();
        return null;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        return !(o == null || !(o instanceof ThumbModel)) && ((ThumbModel) o).bucketId == bucketId;
    }

    @Override
    public int compareTo(ThumbModel thumbModel) {
        if (thumbModel == null)
            return -1;
        if (thumbModel.bucketId == bucketId)
            return 0;
        if (thumbModel.bucketId > bucketId)
            return -1;
        else
            return 1;
    }


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bucketId);
        dest.writeInt(this.id);
        dest.writeString(this.path);
        dest.writeString(this.fileName);
        dest.writeString(this.content);
        dest.writeDouble(this.duration);
        dest.writeString(this.videoType);
        dest.writeInt(this.videoWidth);
        dest.writeInt(this.videoHeight);
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
        dest.writeLong(this.videoSize);
        dest.writeInt(this.orientation);
        dest.writeString(this.videoUrl);
        dest.writeString(this.thumb);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeInt(this.uiType);
        dest.writeString(this.source);
        dest.writeString(this.image);
    }

    protected ThumbModel(Parcel in) {
        this.bucketId = in.readInt();
        this.id = in.readInt();
        this.path = in.readString();
        this.fileName = in.readString();
        this.content = in.readString();
        this.duration = in.readLong();
        this.videoType = in.readString();
        this.videoWidth = in.readInt();
        this.videoHeight = in.readInt();
        this.isVideo = in.readByte() != 0;
        this.videoSize = in.readLong();
        this.orientation = in.readInt();
        this.videoUrl = in.readString();
        this.thumb = in.readString();
        this.isSelect = in.readByte() != 0;
        this.type = in.readInt();
        this.uiType = in.readInt();
        this.source = in.readString();
        this.image = in.readString();
    }

    public static final Creator<ThumbModel> CREATOR = new Creator<ThumbModel>() {
        @Override
        public ThumbModel createFromParcel(Parcel source) {
            return new ThumbModel(source);
        }

        @Override
        public ThumbModel[] newArray(int size) {
            return new ThumbModel[size];
        }
    };
}
