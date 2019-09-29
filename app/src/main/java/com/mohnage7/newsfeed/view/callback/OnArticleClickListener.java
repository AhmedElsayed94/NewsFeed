package com.mohnage7.newsfeed.view.callback;

import android.view.View;

import com.mohnage7.newsfeed.repository.model.Article;

public interface OnArticleClickListener {
    void onArticleClick(Article article, View view);
}
