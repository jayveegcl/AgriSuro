package app.thesis.agrisuro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.models.Feature;

import java.util.List;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder> {

    private List<Feature> features;

    public FeaturesAdapter(List<Feature> features) {
        this.features = features;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature, parent, false);
        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        Feature feature = features.get(position);
        holder.iconImageView.setImageResource(feature.getIconResId());
        holder.titleTextView.setText(feature.getTitle());
        holder.descriptionTextView.setText(feature.getDescription());
    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    static class FeatureViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.feature_icon);
            titleTextView = itemView.findViewById(R.id.feature_title);
            descriptionTextView = itemView.findViewById(R.id.feature_description);
        }
    }
}
