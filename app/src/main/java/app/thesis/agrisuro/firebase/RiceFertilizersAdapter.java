package app.thesis.agrisuro.firebase;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import app.thesis.agrisuro.R;


public class RiceFertilizersAdapter extends RecyclerView.Adapter<RiceFertilizersAdapter.ViewHolder> {
    private List<Pair<String, RiceFertilizers>> riceFertilizerList;

    public RiceFertilizersAdapter(List<Pair<String, RiceFertilizers>> riceFertilizerList) {
        this.riceFertilizerList = riceFertilizerList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, Common_name, Fertilizer, Per_hectare, Purpose;
        ImageView riceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rice1);
            Common_name = itemView.findViewById(R.id.rice2);
            Fertilizer = itemView.findViewById(R.id.rice3);
            Per_hectare = itemView.findViewById(R.id.rice4);
            Purpose = itemView.findViewById(R.id.rice5);
            riceImage = itemView.findViewById(R.id.riceImage);
        }
    }

    @NonNull
    @Override
    public RiceFertilizersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, RiceFertilizers> pair = riceFertilizerList.get(position);
        RiceFertilizers variant = pair.second;

        holder.name.setText(pair.first);
        holder.Common_name.setText("Common Name: " + variant.Common_name);
        holder.Fertilizer.setText("Fertilizer: " + variant.Fertilizer);
        holder.Per_hectare.setText("Per Hectare: " + variant.Per_hectare);
        holder.Purpose.setText("Purpose: " + variant.Purpose);

        // Load image from Firebase Storage using Glide
        if (variant.imagePath != null && !variant.imagePath.isEmpty()) {
            FirebaseStorage.getInstance().getReference()
                    .child(variant.imagePath)
                    .getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        Glide.with(holder.itemView.getContext())
                                .load(uri)
                                .into(holder.riceImage);
                    })
                    .addOnFailureListener(e -> {
                        // Set a placeholder or handle failure
                        holder.riceImage.setImageResource(R.drawable.ic_launcher_background);
                    });
        } else {
            // Set default image if path is missing
            holder.riceImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return riceFertilizerList.size();
    }
}
