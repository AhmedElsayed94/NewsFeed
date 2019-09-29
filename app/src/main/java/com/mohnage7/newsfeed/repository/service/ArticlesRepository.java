package com.mohnage7.newsfeed.repository.service;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mohnage7.newsfeed.base.BaseError;
import com.mohnage7.newsfeed.base.DataWrapper;
import com.mohnage7.newsfeed.base.ErrorBody;
import com.mohnage7.newsfeed.network.RestApiService;
import com.mohnage7.newsfeed.repository.model.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

import static com.mohnage7.newsfeed.utils.Constants.API_KEY;

public class ArticlesRepository {

    private RestApiService apiService;
    private MutableLiveData<DataWrapper<List<Article>>> mutableLiveData = new MutableLiveData<>();
    private BaseError mBaseError;

    @Inject
    public ArticlesRepository(RestApiService apiService) {
        this.apiService = apiService;
    }

    public MutableLiveData<DataWrapper<List<Article>>> getArticles(String source1,String source2,String apiKey) {
        List<Article> articleList = new ArrayList<>();
        Observable<List<Article>> newWebObservable = apiService.getArticles(source1, apiKey)
                .map(result -> Observable.fromIterable(result.getArticles()))
                .flatMap(x -> x).filter(y -> true).toList().toObservable();
        Observable<List<Article>> associatedObservable = apiService.getArticles(source2, apiKey)
                .map(result -> Observable.fromIterable(result.getArticles()))
                .flatMap(x -> x).filter(y -> true).toList().toObservable();
        Observable.merge(newWebObservable, associatedObservable)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Article>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Article> articles) {
                        articleList.addAll(articles);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBaseError = getError(e);
                        mutableLiveData.setValue(new DataWrapper<>(articleList, mBaseError));
                    }

                    @Override
                    public void onComplete() {
                        mutableLiveData.setValue(new DataWrapper<>(articleList, null));
                    }
                });

        return mutableLiveData;
    }

    private BaseError getError(Throwable throwable) {
        BaseError baseError = new BaseError();
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            ResponseBody body = httpException.response().errorBody();
            Gson gson = new Gson();
            TypeAdapter<ErrorBody> adapter = gson.getAdapter(ErrorBody.class);
            try {
                if (body != null) {
                    ErrorBody errorBody = adapter.fromJson(body.string());
                    baseError.setErrorMessage(errorBody.getMessage());
                    baseError.setErrorCode(httpException.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            baseError.setErrorMessage(throwable.getMessage());
            baseError.setErrorCode(0);
        }

        return baseError;
    }

}
