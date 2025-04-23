package app.thesis.agrisuro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import app.thesis.agrisuro.R;
import app.thesis.agrisuro.models.ForecastItem;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<ForecastItem> forecastItems;

    public ForecastAdapter(List<ForecastItem> forecastItems) {
        this.forecastItems = forecastItems;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastItem item = forecastItems.get(position);

        holder.dateText.setText(item.getDate());
        holder.tempHighText.setText(item.getMaxTemp());
        holder.tempLowText.setText(item.getMinTemp());
        holder.descriptionText.setText(item.getDescription());

        // Load weather icon
        Glide.with(holder.itemView.getContext())
                .load(item.getIconUrl())
                .into(holder.iconView);
    }

    @Override
    public int getItemCount() {
        return forecastItems.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, tempHighText, tempLowText, descriptionText;
        ImageView iconView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.forecastDate);
            tempHighText = itemView.findViewById(R.id.forecastTempHigh);
            tempLowText = itemView.findViewById(R.id.forecastTempLow);
            descriptionText = itemView.findViewById(R.id.forecastDescription);
            iconView = itemView.findViewById(R.id.forecastIcon);
        }
    }
}