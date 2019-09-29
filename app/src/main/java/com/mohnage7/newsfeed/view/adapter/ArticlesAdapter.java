package com.mohnage7.newsfeed.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohnage7.newsfeed.R;
import com.mohnage7.newsfeed.repository.model.Article;
import com.mohnage7.newsfeed.view.callback.OnArticleClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.newsfeed.utils.DateParser.getDateWithNewFormat;

public class ArticlesAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Article> articleList;
    private OnArticleClickListener onArticleClickListener;

    public ArticlesAdapter(List<Article> articleList, OnArticleClickListener onArticleClickListener) {
        this.articleList = articleList;
        this.onArticleClickListener = onArticleClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindViews(position);
    }

    @Override
    public int getItemCount() {
        if (articleList != null && !articleList.isEmpty()) {
            return articleList.size();
        } else {
            return 0;
        }
    }


    protected class ArticlesViewHolder extends BaseViewHolder {
        @BindView(R.id.title_tv)
        TextView titleTxtView;
        @BindView(R.id.author_tv)
        TextView authorTxtView;
        @BindView(R.id.date_tv)
        TextView dateTxtView;
        @BindView(R.id.article_iv)
        ImageView articleImageView;


        ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(int position) {
            super.bindViews(position);
            Article article = articleList.get(position);
            titleTxtView.setText(article.getTitle());
            authorTxtView.setText(String.format("%s %s", itemView.getContext().getString(R.string.by), article.getAuthor()));
            dateTxtView.setText(getDateWithNewFormat(article.getPublishedAt()));
            displayArticleImage(article);
            itemView.setOnClickListener(v -> onArticleClickListener.onArticleClick(article,itemView));
        }

        private void displayArticleImage(Article article) {
            if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty())
                Picasso.get().load(article.getUrlToImage()).resize(600, 300).placeholder(R.drawable.placeholder).into(articleImageView);
            else
                Picasso.get().load(R.drawable.placeholder).into(articleImageView);
        }


        @Override
        void clear() {
            titleTxtView.setText("");
            authorTxtView.setText("");
            dateTxtView.setText("");
            articleImageView.setImageDrawable(null);
        }
    }
}
