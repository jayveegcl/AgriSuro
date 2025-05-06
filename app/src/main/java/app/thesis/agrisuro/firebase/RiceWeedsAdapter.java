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


public class RiceWeedsAdapter extends RecyclerView.Adapter<RiceWeedsAdapter.ViewHolder> {
    private List<Pair<String, RiceWeeds>> riceWeedList;

    public RiceWeedsAdapter(List<Pair<String, RiceWeeds>> riceWeedList) {
        this.riceWeedList = riceWeedList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, Cultivated, Development, EPPO_code, Family, Local_name, Propagation;
        ImageView riceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rice1);
            Cultivated = itemView.findViewById(R.id.rice2);
            Development = itemView.findViewById(R.id.rice3);
            EPPO_code = itemView.findViewById(R.id.rice4);
            Family = itemView.findViewById(R.id.rice5);
            Local_name = itemView.findViewById(R.id.rice6);
            Propagation = itemView.findViewById(R.id.rice7);
            riceImage = itemView.findViewById(R.id.riceImage);
        }
    }

    @NonNull
    @Override
    public RiceWeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, RiceWeeds> pair = riceWeedList.get(position);
        RiceWeeds variant = pair.second;

        holder.name.setText(pair.first);
        holder.Cultivated.setText("Cultivated: " + variant.Cultivated);
        holder.Development.setText("Development: " + variant.Development);
        holder.EPPO_code.setText("EPPO Code: " + variant.EPPO_code);
        holder.Family.setText("Family: " + variant.Family);
        holder.Local_name.setText("Local Name: " + variant.Local_name);
        holder.Propagation.setText("Propagation: " + variant.Propagation);

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
        return riceWeedList.size();
    }
}
