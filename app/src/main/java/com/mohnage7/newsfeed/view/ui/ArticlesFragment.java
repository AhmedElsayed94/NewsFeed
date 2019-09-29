package com.mohnage7.newsfeed.view.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mohnage7.newsfeed.R;
import com.mohnage7.newsfeed.base.BaseError;
import com.mohnage7.newsfeed.base.BaseFragment;
import com.mohnage7.newsfeed.repository.model.Article;
import com.mohnage7.newsfeed.view.adapter.ArticlesAdapter;
import com.mohnage7.newsfeed.view.callback.OnArticleClickListener;
import com.mohnage7.newsfeed.viewmodel.ArticlesViewModel;

import java.util.List;

import butterknife.BindView;

import static com.mohnage7.newsfeed.utils.Constants.API_KEY;
import static com.mohnage7.newsfeed.utils.Constants.ARTICLE_EXTRA;

public class ArticlesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnArticleClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_data_layout)
    RelativeLayout noDataLayout;
    @BindView(R.id.no_data_tv)
    TextView noDataTxtView;

    private ArticlesViewModel articlesViewModel;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_articles;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        articlesViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
        swipeRefreshLayout.setOnRefreshListener(this);
        getArticles();
    }

    private void getArticles() {
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(true);
        articlesViewModel.getArticlesList("the-next-web", "associated-press", API_KEY).observe(this, dataWrapper -> {
            swipeRefreshLayout.setRefreshing(false);
            if (dataWrapper.getBaseError() != null) {
                handleError(dataWrapper.getBaseError());
            } else {
                setupArticlesRecycler(dataWrapper.getData());
            }
        });
    }

    private void setDataViewsVisibility(boolean dataAvailable) {
        if (dataAvailable) {
            recyclerView.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }
    }


    private void handleError(BaseError baseError) {
        setDataViewsVisibility(false);
        if (baseError.getErrorCode() == 500) {
            noDataTxtView.setText(getString(R.string.server_error));
        } else {
            noDataTxtView.setText(baseError.getErrorMessage());
        }
    }

    private void setupArticlesRecycler(List<Article> articles) {
        setDataViewsVisibility(true);
        ArticlesAdapter adapter = new ArticlesAdapter(articles, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        getArticles();
    }

    @Override
    public void onArticleClick(Article article, View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARTICLE_EXTRA, article);
        Navigation.findNavController(view).navigate(R.id.action_articlesFragment_to_articleDetailsFragment, bundle);
    }

}
