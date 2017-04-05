package com.charliealbright.gbooks.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlie on 4/4/17.
 */

public class VolumeImages implements Parcelable {

    @SerializedName("smallThumbnail")
    private String mSmallThumbnailImage;

    @SerializedName("thumbnail")
    private String mThumbnailImage;

    @SerializedName("small")
    private String mSmallImage;

    @SerializedName("medium")
    private String mMediumImage;

    @SerializedName("large")
    private String mLargeImage;

    @SerializedName("extraLarge")
    private String mExtraLargeImage;

    protected VolumeImages(Parcel in) {
        mSmallThumbnailImage = in.readString();
        mThumbnailImage = in.readString();
        mSmallImage = in.readString();
        mMediumImage = in.readString();
        mLargeImage = in.readString();
        mExtraLargeImage = in.readString();
    }

    public static final Creator<VolumeImages> CREATOR = new Creator<VolumeImages>() {
        @Override
        public VolumeImages createFromParcel(Parcel in) {
            return new VolumeImages(in);
        }

        @Override
        public VolumeImages[] newArray(int size) {
            return new VolumeImages[size];
        }
    };

    public String getSmallThumbnailImage() {
        if (mSmallThumbnailImage != null) {
            return mSmallThumbnailImage;
        } else {
            return "";
        }
    }

    public String getThumbnailImage() {
        if (mThumbnailImage != null) {
            return mThumbnailImage;
        } else {
            return getSmallThumbnailImage();
        }
    }

    public String getSmallImage() {
        if (mSmallImage != null) {
            return mSmallImage;
        } else {
            return getThumbnailImage();
        }
    }

    public String getMediumImage() {
        if (mMediumImage != null) {
            return mMediumImage;
        } else {
            return getSmallImage();
        }
    }

    public String getLargeImage() {
        if (mLargeImage != null) {
            return mLargeImage;
        } else {
            return getMediumImage();
        }
    }

    public String getExtraLargeImage() {
        if (mExtraLargeImage != null) {
            return mExtraLargeImage;
        } else {
            return getLargeImage();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mSmallThumbnailImage);
        parcel.writeString(mThumbnailImage);
        parcel.writeString(mSmallImage);
        parcel.writeString(mMediumImage);
        parcel.writeString(mLargeImage);
        parcel.writeString(mExtraLargeImage);
    }
}
