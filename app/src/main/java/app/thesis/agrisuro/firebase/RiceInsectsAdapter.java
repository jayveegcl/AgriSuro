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


public class RiceInsectsAdapter extends RecyclerView.Adapter<RiceInsectsAdapter.ViewHolder> {
    private List<Pair<String, RiceInsects>> riceInsectList;

    public RiceInsectsAdapter(List<Pair<String, RiceInsects>> riceInsectList) {
        this.riceInsectList = riceInsectList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, Damage, Identifying_Marks, Location, Tagalog_name;
        ImageView riceImage;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rice1);
            Damage = itemView.findViewById(R.id.rice2);
            Identifying_Marks = itemView.findViewById(R.id.rice3);
            Location = itemView.findViewById(R.id.rice4);
            Tagalog_name = itemView.findViewById(R.id.rice5);
            riceImage = itemView.findViewById(R.id.riceImage);
        }
    }

    @NonNull
    @Override
    public RiceInsectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, RiceInsects> pair = riceInsectList.get(position);
        RiceInsects variant = pair.second;

        holder.name.setText(pair.first);
        holder.Damage.setText("Damage: " + variant.Damage);
        holder.Identifying_Marks.setText("Identifying Marks: " + variant.Identifying_Marks);
        holder.Location.setText("Location: " + variant.Location);
        holder.Tagalog_name.setText("Tagalog Name: " + variant.Tagalog_name);

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
        return riceInsectList.size();
    }
}
