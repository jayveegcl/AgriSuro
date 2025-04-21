package app.thesis.agrisuro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.models.PlantingRecommendation;
import com.google.android.material.chip.Chip;

import java.util.List;

public class PlantingRecommendationAdapter extends RecyclerView.Adapter<PlantingRecommendationAdapter.RecommendationViewHolder> {

    private final List<PlantingRecommendation> recommendations;

    public PlantingRecommendationAdapter(List<PlantingRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_planting_recommendation, parent, false);
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        PlantingRecommendation recommendation = recommendations.get(position);
        holder.cropText.setText(recommendation.getCrop());
        holder.recommendationText.setText(recommendation.getRecommendation());
        holder.timeframeText.setText(String.format("Best time: %s", recommendation.getOptimalTimeframe()));

        // Set suitability badge
        int score = recommendation.getSuitabilityScore();
        if (score >= 80) {
            holder.suitabilityChip.setText(R.string.excellent);
            holder.suitabilityChip.setChipBackgroundColorResource(R.color.green_500);
        } else if (score >= 60) {
            holder.suitabilityChip.setText(R.string.good);
            holder.suitabilityChip.setChipBackgroundColorResource(R.color.yellow_500);
        } else {
            holder.suitabilityChip.setText(R.string.poor);
            holder.suitabilityChip.setChipBackgroundColorResource(R.color.red_500);
        }
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }

    static class RecommendationViewHolder extends RecyclerView.ViewHolder {
        TextView cropText;
        TextView recommendationText;
        TextView timeframeText;
        Chip suitabilityChip;

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);
            cropText = itemView.findViewById(R.id.crop_text);
            recommendationText = itemView.findViewById(R.id.recommendation_text);
            timeframeText = itemView.findViewById(R.id.timeframe_text);
            suitabilityChip = itemView.findViewById(R.id.suitability_chip);
        }
    }
}

