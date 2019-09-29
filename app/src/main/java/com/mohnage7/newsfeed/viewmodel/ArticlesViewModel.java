package com.mohnage7.newsfeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.mohnage7.newsfeed.NewsFeedApplication;
import com.mohnage7.newsfeed.base.DataWrapper;
import com.mohnage7.newsfeed.repository.model.Article;
import com.mohnage7.newsfeed.repository.service.ArticlesRepository;

import java.util.List;

import javax.inject.Inject;

public class ArticlesViewModel extends ViewModel {

    @Inject
    ArticlesRepository repository;

    public ArticlesViewModel() {
        NewsFeedApplication.getInstance().getDataComponent().inject(this);
    }

    public LiveData<DataWrapper<List<Article>>> getArticlesList(String source1, String source2, String apiKey) {
        return repository.getArticles(source1, source2, apiKey);
    }
}
