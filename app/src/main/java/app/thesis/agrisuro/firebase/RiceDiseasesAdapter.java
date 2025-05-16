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

public class RiceDiseasesAdapter extends RecyclerView.Adapter<RiceDiseasesAdapter.ViewHolder> {
    private List<Pair<String, RiceDiseases>> riceDiseaseList;

    public RiceDiseasesAdapter(List<Pair<String, RiceDiseases>> riceDiseaseList) {
        this.riceDiseaseList = riceDiseaseList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, Disease_Management, Factors, Local_name, Symptoms, Reco;
        ImageView riceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rice1);
            Local_name = itemView.findViewById(R.id.rice2);
            Symptoms = itemView.findViewById(R.id.rice3);
            Factors = itemView.findViewById(R.id.rice4);
            Reco = itemView.findViewById(R.id.rice5);
            Disease_Management = itemView.findViewById(R.id.rice6);
            riceImage = itemView.findViewById(R.id.riceImage);
        }
    }

    @NonNull
    @Override
    public RiceDiseasesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, RiceDiseases> pair = riceDiseaseList.get(position);
        RiceDiseases variant = pair.second;

        holder.name.setText(pair.first);
        holder.Local_name.setText("\uD83C\uDFF7\uFE0F Karaniwang Pangalan: " + variant.Local_name);
        holder.Disease_Management.setText("\uD83D\uDC8A Pamamahala ng Sakit: " + variant.Disease_Management);
        holder.Factors.setText("⚠\uFE0F Sanhi: " + variant.Factors);
        holder.Symptoms.setText("\uD83E\uDD12 Sintomas: " + variant.Symptoms);
        holder.Reco.setText("\uD83C\uDF3E Rekomendadong Barayti: " + variant.Reco);

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
        return riceDiseaseList.size();
    }
}
