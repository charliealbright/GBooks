package com.charliealbright.gbooks.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Charlie on 4/3/17.
 */

public class GBooksResponse {

    @SerializedName("kind")
    private String mKind;

    @SerializedName("totalItems")
    private int mItemCount;

    @SerializedName("items")
    private List<Volume> mVolumes;


    public String getKind() {
        return mKind;
    }

    public int getItemCount() {
        return mItemCount;
    }

    public List<Volume> getVolumes() {
        return mVolumes;
    }
}
