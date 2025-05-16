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


public class RiceGrowthAdapter extends RecyclerView.Adapter<RiceGrowthAdapter.ViewHolder> {
    private List<Pair<String, RiceGrowth>> riceGrowthList;

    public RiceGrowthAdapter(List<Pair<String, RiceGrowth>> riceGrowthList) {
        this.riceGrowthList = riceGrowthList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, A, Tagalog_name;
        ImageView riceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rice1);
            Tagalog_name = itemView.findViewById(R.id.rice2);
            A = itemView.findViewById(R.id.rice3);
            riceImage = itemView.findViewById(R.id.riceImage);
        }
    }

    @NonNull
    @Override
    public RiceGrowthAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_card2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, RiceGrowth> pair = riceGrowthList.get(position);
        RiceGrowth variant = pair.second;

        holder.name.setText(pair.first);
        holder.Tagalog_name.setText("\uD83C\uDFF7\uFE0F Tagalog: " + variant.Tagalog_name);
        holder.A.setText("" + variant.A);

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
        return riceGrowthList.size();
    }
}
