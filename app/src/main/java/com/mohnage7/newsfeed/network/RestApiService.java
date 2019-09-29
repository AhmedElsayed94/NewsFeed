package com.mohnage7.newsfeed.network;


import com.mohnage7.newsfeed.repository.model.ArticlesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiService {

    @GET("articles")
    Observable<ArticlesResponse> getArticles(@Query("source") String source, @Query("apiKey") String apiKey);
}
