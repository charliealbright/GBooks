package com.charliealbright.gbooks.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Charlie on 4/4/17.
 */

public class VolumeInfo implements Parcelable {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("authors")
    private List<String> mAuthors;

    @SerializedName("publisher")
    private String mPublisher;

    @SerializedName("publishedDate")
    private String mPublishedDate;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("pageCount")
    private int mPageCount;

    @SerializedName("printType")
    private String mPrintType;

    @SerializedName("averageRating")
    private float mAverageRating;

    @SerializedName("ratingsCount")
    private int mRatingsCount;

    @SerializedName("imageLinks")
    private VolumeImages mImageSet;

    protected VolumeInfo(Parcel in) {
        mTitle = in.readString();
        mAuthors = in.createStringArrayList();
        mPublisher = in.readString();
        mPublishedDate = in.readString();
        mDescription = in.readString();
        mPageCount = in.readInt();
        mPrintType = in.readString();
        mAverageRating = in.readFloat();
        mRatingsCount = in.readInt();
        mImageSet = in.readParcelable(VolumeImages.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeStringList(mAuthors);
        dest.writeString(mPublisher);
        dest.writeString(mPublishedDate);
        dest.writeString(mDescription);
        dest.writeInt(mPageCount);
        dest.writeString(mPrintType);
        dest.writeDouble(mAverageRating);
        dest.writeInt(mRatingsCount);
        dest.writeParcelable(mImageSet, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VolumeInfo> CREATOR = new Creator<VolumeInfo>() {
        @Override
        public VolumeInfo createFromParcel(Parcel in) {
            return new VolumeInfo(in);
        }

        @Override
        public VolumeInfo[] newArray(int size) {
            return new VolumeInfo[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        String authorString = "";
        if (mAuthors != null && !mAuthors.isEmpty()) {
            authorString += mAuthors.get(0);
            if (mAuthors.size() > 1) {
                for (int i = 1; i < mAuthors.size(); i++) {
                    authorString += "\n" + mAuthors.get(i);
                }
            }
        }
        return authorString;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public String getPrintType() {
        return mPrintType;
    }

    public float getAverageRating() {
        return mAverageRating;
    }

    public int getRatingsCount() {
        return mRatingsCount;
    }

    public VolumeImages getImageSet() {
        return mImageSet;
    }
}
