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


public class RicePesticidesAdapter extends RecyclerView.Adapter<RicePesticidesAdapter.ViewHolder> {
    private List<Pair<String, RicePesticides>> ricePesticideList;

    public RicePesticidesAdapter(List<Pair<String, RicePesticides>> ricePesticideList) {
        this.ricePesticideList = ricePesticideList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, Type, Ingredient, Tagalog, Use, Application, Precautions;
        ImageView riceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rice1);
            Tagalog = itemView.findViewById(R.id.rice2);
            Ingredient = itemView.findViewById(R.id.rice3);
            Use = itemView.findViewById(R.id.rice4);
            Type = itemView.findViewById(R.id.rice5);
            Application = itemView.findViewById(R.id.rice6);
            Precautions = itemView.findViewById(R.id.rice7);
            riceImage = itemView.findViewById(R.id.riceImage);
        }
    }

    @NonNull
    @Override
    public RicePesticidesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, RicePesticides> pair = ricePesticideList.get(position);
        RicePesticides variant = pair.second;

        holder.name.setText(pair.first);
        holder.Tagalog.setText("\uD83C\uDFF7\uFE0F Karaniwang Pangalan: " + variant.Tagalog);
        holder.Type.setText("\uD83E\uDDEA Uri: " + variant.Type);
        holder.Ingredient.setText("⚗\uFE0F Aktibong Sangkap: " + variant.Ingredient);
        holder.Application.setText("\uD83E\uDDF4 Paraan ng Paggamit: " + variant.Application);
        holder.Use.setText("\uD83C\uDFAF Gamit: " + variant.Use);
        holder.Precautions.setText("⚠\uFE0F Pag-iingat: " + variant.Precautions);

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
        return ricePesticideList.size();
    }
}
