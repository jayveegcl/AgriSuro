package app.thesis.agrisuro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.models.NewsItem;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsItems;

    public NewsAdapter(List<NewsItem> newsItems) {
        this.newsItems = newsItems;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsItems.get(position);
        holder.newsTitleTextView.setText(newsItem.getTitle());
        holder.newsDescriptionTextView.setText(newsItem.getDescription());
        holder.newsDateTextView.setText(newsItem.getDate());
        holder.newsImageView.setImageResource(newsItem.getImageResId());
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImageView;
        TextView newsTitleTextView;
        TextView newsDescriptionTextView;
        TextView newsDateTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImageView = itemView.findViewById(R.id.news_image);
            newsTitleTextView = itemView.findViewById(R.id.news_title);
            newsDescriptionTextView = itemView.findViewById(R.id.news_description);
            newsDateTextView = itemView.findViewById(R.id.news_date);
        }
    }
}
