package com.mohnage7.newsfeed;

import android.app.Application;

import com.mohnage7.newsfeed.di.component.DaggerDataComponent;
import com.mohnage7.newsfeed.di.component.DataComponent;
import com.mohnage7.newsfeed.di.module.DataModule;

public class NewsFeedApplication extends Application {

    private static NewsFeedApplication instance;
    DataComponent dataComponent;

    public static NewsFeedApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDataComponent();
    }

    private void initDataComponent() {
        dataComponent = DaggerDataComponent.builder().
                dataModule(new DataModule(this))
                .build();
        dataComponent.inject(this);
    }

    public DataComponent getDataComponent() {
        return dataComponent;
    }
}
