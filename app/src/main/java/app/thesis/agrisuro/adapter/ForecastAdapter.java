package app.thesis.agrisuro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.models.ForecastDay;
import app.thesis.agrisuro.utils.WeatherUtils;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private final List<ForecastDay> forecastList;

    public ForecastAdapter(List<ForecastDay> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast_day, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastDay forecastDay = forecastList.get(position);
        holder.dayText.setText(forecastDay.getDay());
        holder.highTempText.setText(String.format("%d°", forecastDay.getHighTemp()));
        holder.lowTempText.setText(String.format("%d°", forecastDay.getLowTemp()));
        holder.rainProbabilityText.setText(String.format("%d%%", forecastDay.getRainProbability()));

        // Set text color based on condition
        int conditionColor = WeatherUtils.getConditionColor(holder.itemView.getContext(), forecastDay.getCondition());
        holder.highTempText.setTextColor(conditionColor);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;
        TextView highTempText;
        TextView lowTempText;
        TextView rainProbabilityText;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.day_text);
            highTempText = itemView.findViewById(R.id.high_temp_text);
            lowTempText = itemView.findViewById(R.id.low_temp_text);
            rainProbabilityText = itemView.findViewById(R.id.rain_probability_text);
        }
    }
}
