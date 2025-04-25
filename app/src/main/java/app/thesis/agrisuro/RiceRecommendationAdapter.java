package app.thesis.agrisuro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.RiceRecommendationItem;

public class RiceRecommendationAdapter extends RecyclerView.Adapter<RiceRecommendationAdapter.RecommendationViewHolder> {

    private List<RiceRecommendationItem> recommendationItems;

    public RiceRecommendationAdapter(List<RiceRecommendationItem> recommendationItems) {
        this.recommendationItems = recommendationItems;
    }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rice_recommendation, parent, false);
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        RiceRecommendationItem item = recommendationItems.get(position);
        holder.dateText.setText(item.getDate());
        holder.recommendationText.setText(item.getRecommendation());
    }

    @Override
    public int getItemCount() {
        return recommendationItems.size();
    }

    static class RecommendationViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, recommendationText;

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.recommendationDate);
            recommendationText = itemView.findViewById(R.id.recommendationText);
        }
    }
}