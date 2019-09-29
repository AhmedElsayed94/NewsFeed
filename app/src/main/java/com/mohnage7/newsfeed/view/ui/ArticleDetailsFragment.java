package com.mohnage7.newsfeed.view.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohnage7.newsfeed.R;
import com.mohnage7.newsfeed.base.BaseFragment;
import com.mohnage7.newsfeed.repository.model.Article;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mohnage7.newsfeed.utils.Constants.ARTICLE_EXTRA;
import static com.mohnage7.newsfeed.utils.DateParser.getDateWithNewFormat;

public class ArticleDetailsFragment extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView titleTxtView;
    @BindView(R.id.author_tv)
    TextView authorTxtView;
    @BindView(R.id.details_date_tv)
    TextView dateTxtView;
    @BindView(R.id.article_iv)
    ImageView articleImageView;
    @BindView(R.id.description_tv)
    TextView descriptionTxtView;

    private Article article;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(ARTICLE_EXTRA)) {
            article = bundle.getParcelable(ARTICLE_EXTRA);
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_article_details;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTxtView.setText(article.getTitle());
        authorTxtView.setText(String.format("%s %s", getString(R.string.by), article.getAuthor()));
        dateTxtView.setVisibility(View.VISIBLE);
        dateTxtView.setText(getDateWithNewFormat(article.getPublishedAt()));
        descriptionTxtView.setText(article.getDescription());
        displayArticleImage(article);
    }

    @OnClick(R.id.open_article_button)
    void openArticle() {
        if (article.getUrl() != null && !article.getUrl().isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
            startActivity(browserIntent);
        }
    }

    private void displayArticleImage(Article article) {
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty())
            Picasso.get().load(article.getUrlToImage()).resize(600, 300).placeholder(R.drawable.placeholder).into(articleImageView);
        else
            Picasso.get().load(R.drawable.placeholder).into(articleImageView);
    }
}
