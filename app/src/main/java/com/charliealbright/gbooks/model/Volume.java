package com.charliealbright.gbooks.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlie on 4/3/17.
 */

public class Volume implements Parcelable {

    @SerializedName("id")
    private String mId;

    @SerializedName("volumeInfo")
    private VolumeInfo mVolumeInfo;


    public Volume(Parcel in) {
        mId = in.readString();
        mVolumeInfo = in.readParcelable(VolumeInfo.class.getClassLoader());
    }

    public String getId() {
        return mId;
    }

    public VolumeInfo getVolumeInfo() {
        return mVolumeInfo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeParcelable(mVolumeInfo, i);
    }

    public static final Parcelable.Creator<Volume> CREATOR
            = new Parcelable.Creator<Volume>() {

        @Override
        public Volume createFromParcel(Parcel in) {
            return new Volume(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Volume[] newArray(int size) {
            return new Volume[size];
        }
    };
}
