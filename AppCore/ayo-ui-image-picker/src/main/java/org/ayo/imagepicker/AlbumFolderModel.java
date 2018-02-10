package org.ayo.imagepicker;


import android.os.Parcel;
import android.os.Parcelable;

public class AlbumFolderModel implements Parcelable {

    private String dir;
    private String firstImgPath;
    private String dirName;
    private int size;
    private boolean isSelected;
    private int type;//0全部 非0文件夹

    protected AlbumFolderModel() {

    }

    protected AlbumFolderModel(Parcel in) {
        dir = in.readString();
        firstImgPath = in.readString();
        dirName = in.readString();
        size = in.readInt();
        isSelected = in.readByte() != 0;
        type = in.readInt();
    }

    public static final Creator<AlbumFolderModel> CREATOR = new Creator<AlbumFolderModel>() {
        @Override
        public AlbumFolderModel createFromParcel(Parcel in) {
            return new AlbumFolderModel(in);
        }

        @Override
        public AlbumFolderModel[] newArray(int size) {
            return new AlbumFolderModel[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dir);
        parcel.writeString(firstImgPath);
        parcel.writeString(dirName);
        parcel.writeInt(size);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
        parcel.writeInt(type);
    }
}
