package app.thesis.agrisuro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.thesis.agrisuro.models.ResourceItem;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class DetailCardFragment extends Fragment {

    private ImageView imageView;
    private TextView titleText;
    private TextView descriptionText;
    private ChipGroup characteristicsChipGroup;
    private Button favoriteButton;
    private Button shareButton;
    private TextView typeText;

    private app.thesis.agrisuro.fragments.ResourceItem currentItem;
    private boolean isFavorite;
    private OnFavoriteToggleListener favoriteToggleListener;

    public interface OnFavoriteToggleListener {
        void onFavoriteToggled(String id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_card, container, false);

        // Initialize UI components
        imageView = view.findViewById(R.id.detail_image);
        titleText = view.findViewById(R.id.detail_title);
        descriptionText = view.findViewById(R.id.detail_description);
        characteristicsChipGroup = view.findViewById(R.id.characteristics_chip_group);
        favoriteButton = view.findViewById(R.id.favorite_button);
        shareButton = view.findViewById(R.id.share_button);
        typeText = view.findViewById(R.id.type_badge);

        // Set up buttons
        favoriteButton.setOnClickListener(v -> {
            if (currentItem != null && favoriteToggleListener != null) {
                favoriteToggleListener.onFavoriteToggled(currentItem.getId());
            }
        });

        shareButton.setOnClickListener(v -> {
            // In a real app, this would open a share dialog
            Toast.makeText(getContext(), "Share dialog would open here", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    public void setResourceItem(ResourceItem item, boolean isFavorite) {
        this.currentItem = item;
        this.isFavorite = isFavorite;

        if (getContext() == null) return;

        // Set image
        Glide.with(this)
                .load(item.getImage())
                .centerCrop()
                .into(imageView);

        // Set text
        titleText.setText(item.getTitle());
        descriptionText.setText(item.getDescription());

        // Set type badge
        String category = item.getCategory();
        if (category.equals("rice-variants")) {
            typeText.setText(R.string.rice);
            typeText.setBackgroundResource(R.drawable.badge_default);
        } else if (category.equals("crop-diseases")) {
            typeText.setText(R.string.disease);
            typeText.setBackgroundResource(R.drawable.badge_destructive);
        } else if (category.equals("weed-management")) {
            typeText.setText(R.string.weed);
            typeText.setBackgroundResource(R.drawable.badge_secondary);
        }

        // Set characteristics chips
        characteristicsChipGroup.removeAllViews();
        for (String tag : item.getTags()) {
            Chip chip = new Chip(getContext());
            chip.setText(tag);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setChipStrokeWidth(1);
            chip.setChipStrokeColorResource(R.color.gray_300);
            characteristicsChipGroup.addView(chip);
        }

        // Update favorite button
        updateFavoriteStatus(isFavorite);
    }

    public void updateFavoriteStatus(boolean isFavorite) {
        this.isFavorite = isFavorite;

        if (isFavorite) {
            favoriteButton.setText(R.string.saved);
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark_check, 0, 0, 0);
        } else {
            favoriteButton.setText(R.string.save);
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark, 0, 0, 0);
        }
    }

    public String getCurrentItemId() {
        return currentItem != null ? currentItem.getId() : null;
    }

    public void setOnFavoriteToggleListener(OnFavoriteToggleListener listener) {
        this.favoriteToggleListener = listener;
    }
}
