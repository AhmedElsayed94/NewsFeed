package com.mohnage7.newsfeed.di.component;


import com.mohnage7.newsfeed.NewsFeedApplication;
import com.mohnage7.newsfeed.di.module.DataModule;
import com.mohnage7.newsfeed.viewmodel.ArticlesViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class})
public interface DataComponent {


    void inject(ArticlesViewModel articlesViewModel);

    void inject(NewsFeedApplication newsFeedApplication);
}
