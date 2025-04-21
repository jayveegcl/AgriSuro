package app.thesis.agrisuro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.models.ResourceItem;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class ResourceItemAdapter extends RecyclerView.Adapter<ResourceItemAdapter.ResourceViewHolder> {

    private final Context context;
    private final List<ResourceItem> resources;
    private final List<String> favorites;
    private final OnResourceItemClickListener listener;

    public interface OnResourceItemClickListener {
        void onResourceItemClick(ResourceItem item);
        void onToggleFavorite(String id);
    }

    public ResourceItemAdapter(Context context, List<ResourceItem> resources, List<String> favorites, OnResourceItemClickListener listener) {
        this.context = context;
        this.resources = resources;
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resource, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        ResourceItem item = resources.get(position);

        // Load image
        Glide.with(context)
                .load(item.getImage())
                .centerCrop()
                .into(holder.imageView);

        // Set text
        holder.titleText.setText(item.getTitle());
        holder.descriptionText.setText(item.getDescription());

        // Set favorite badge visibility
        holder.favoriteChip.setVisibility(favorites.contains(item.getId()) ? View.VISIBLE : View.GONE);

        // Set tags
        holder.tagsChipGroup.removeAllViews();
        String[] tags = item.getTags();
        int maxTags = Math.min(3, tags.length);

        for (int i = 0; i < maxTags; i++) {
            Chip chip = new Chip(context);
            chip.setText(tags[i]);
            chip.setTextSize(10);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setChipStrokeWidth(1);
            chip.setChipStrokeColorResource(R.color.gray_300);
            holder.tagsChipGroup.addView(chip);
        }

        if (tags.length > 3) {
            Chip moreChip = new Chip(context);
            moreChip.setText(String.format("+%d more", tags.length - 3));
            moreChip.setTextSize(10);
            moreChip.setChipBackgroundColorResource(R.color.white);
            moreChip.setChipStrokeWidth(1);
            moreChip.setChipStrokeColorResource(R.color.gray_300);
            holder.tagsChipGroup.addView(moreChip);
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onResourceItemClick(item);
            }
        });

        // Long press to toggle favorite
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onToggleFavorite(item.getId());
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    static class ResourceViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText;
        TextView descriptionText;
        ChipGroup tagsChipGroup;
        Chip favoriteChip;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.resource_image);
            titleText = itemView.findViewById(R.id.resource_title);
            descriptionText = itemView.findViewById(R.id.resource_description);
            tagsChipGroup = itemView.findViewById(R.id.tags_chip_group);
            favoriteChip = itemView.findViewById(R.id.favorite_chip);
        }
    }
}
