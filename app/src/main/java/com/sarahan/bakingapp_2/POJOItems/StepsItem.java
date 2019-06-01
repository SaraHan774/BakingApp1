package com.sarahan.bakingapp_2.POJOItems;

import android.os.Parcel;
import android.os.Parcelable;

public class StepsItem implements Parcelable {
    private int id;
    private String shortDes;
    private String longDes;
    private String videoURL;
    private String thumbnailURL;

    public StepsItem(int id, String shortDes, String longDes, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDes = shortDes;
        this.longDes = longDes;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    protected StepsItem(Parcel in) { //read data in the constructor
        this.id = in.readInt();
        this.shortDes = in.readString();
        this.longDes= in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getShortDes() {
        return shortDes;
    }

    public String getLongDes() {
        return longDes;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDes);
        dest.writeString(longDes);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    //field name must be public, CREATOR
    //generates instances of your Parcelable class from a Parcel.
    public static final Creator<StepsItem> CREATOR = new Creator<StepsItem>() {
        @Override
        public StepsItem createFromParcel(Parcel parcel) {
            return new StepsItem(parcel); //Parcelable 을 구현한 클래스의 인스턴스 생성
        } //un-marshalling the parcel.

        @Override
        public StepsItem[] newArray(int size) {
            return new StepsItem[size]; //Parcelable 을 구현한 클래스 객체의 어레이 생성
        }
    };
}
