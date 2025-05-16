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


public class RiceSoilAdapter extends RecyclerView.Adapter<RiceSoilAdapter.ViewHolder> {
    private List<Pair<String, RiceSoil>> riceSoilList;

    public RiceSoilAdapter(List<Pair<String, RiceSoil>> riceSoilList) {
        this.riceSoilList = riceSoilList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, A, B, C, Tagalog;
        ImageView riceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rice1);
            Tagalog = itemView.findViewById(R.id.rice2);
            A = itemView.findViewById(R.id.rice3);
            B = itemView.findViewById(R.id.rice4);
            C = itemView.findViewById(R.id.rice5);
            riceImage = itemView.findViewById(R.id.riceImage);
        }
    }

    @NonNull
    @Override
    public RiceSoilAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, RiceSoil> pair = riceSoilList.get(position);
        RiceSoil variant = pair.second;

        holder.name.setText(pair.first);
        holder.Tagalog.setText("\uD83C\uDFF7\uFE0F Tagalog: " + variant.Tagalog);
        holder.A.setText("" + variant.A);
        holder.B.setText("" + variant.B);
        holder.C.setText("" + variant.C);

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
        return riceSoilList.size();
    }
}
