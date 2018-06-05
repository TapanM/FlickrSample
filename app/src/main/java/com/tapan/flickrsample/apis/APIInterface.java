package com.tapan.flickrsample.apis;

import com.tapan.flickrsample.objects.FeedObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * The APIInterface class for retrofit api interface
 */
public interface APIInterface {

    @GET("/services/feeds/photos_public.gne")
    Call<FeedObject> getFeed(@Query("format") String format,@Query("nojsoncallback") int nojsoncallback);
}
