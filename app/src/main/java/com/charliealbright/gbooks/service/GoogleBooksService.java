package com.charliealbright.gbooks.service;

import com.charliealbright.gbooks.model.GBooksResponse;
import com.charliealbright.gbooks.model.Volume;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Charlie on 4/3/17.
 */

public interface GoogleBooksService {

    @GET("volumes")
    Call<GBooksResponse> search(@Query("q") String searchQuery, @Query("key") String apiKey, @Query("startIndex") int startIndex);

    @GET("volumes/{id}")
    Call<Volume> getVolume(@Path("id") String volumeID);
}
