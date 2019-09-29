package com.mohnage7.newsfeed.di.module;

import android.app.Application;

import com.mohnage7.newsfeed.network.RestApiService;
import com.mohnage7.newsfeed.repository.service.ArticlesRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mohnage7.newsfeed.utils.Constants.BASE_URL;

@Module
public class DataModule {
    private Application application;

    public DataModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                //converts Retrofit response into Observable
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient providesHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add request time out
        httpClient.connectTimeout(20, TimeUnit.SECONDS);
        httpClient.readTimeout(40, TimeUnit.SECONDS);
        httpClient.writeTimeout(40, TimeUnit.SECONDS);
        // Add logging into
        httpClient.interceptors().add(httpLoggingInterceptor);
        return httpClient.build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor providesHttpLogging(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @Provides
    @Singleton
    RestApiService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestApiService.class);
    }

    @Provides
    ArticlesRepository providesRepository(RestApiService apiService) {
        return new ArticlesRepository(apiService);
    }
}
