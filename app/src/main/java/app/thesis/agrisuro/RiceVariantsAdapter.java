package app.thesis.agrisuro;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RiceVariantsAdapter extends RecyclerView.Adapter<RiceVariantsAdapter.ViewHolder> {
    private List<Pair<String, RiceVariants>> riceList;

    public RiceVariantsAdapter(List<Pair<String, RiceVariants>> riceList) {
        this.riceList = riceList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, Average_Yield, Environment, Height, Maturity, Maximum_Yield, Season;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.riceName);
            Average_Yield = itemView.findViewById(R.id.riceType);
            Environment = itemView.findViewById(R.id.riceMaturity);
            Height = itemView.findViewById(R.id.riceYield);
            Maturity = itemView.findViewById(R.id.riceResistance);
            Maximum_Yield = itemView.findViewById(R.id.riceArea);
            Season = itemView.findViewById(R.id.riceSeason);
        }
    }

    @NonNull
    @Override
    public RiceVariantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rice_variants, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pair<String, RiceVariants> pair = riceList.get(position);
        RiceVariants variant = pair.second;

        holder.name.setText(pair.first);
        holder.Average_Yield.setText("Average Yield: " + variant.Average_Yield);
        holder.Environment.setText("Environment: " + variant.Environment + " days");
        holder.Height.setText("Height: " + variant.Height);
        holder.Maturity.setText("Maturity: " + variant.Maturity);
        holder.Maximum_Yield.setText("Maximum_Yield: " + variant.Maximum_Yield);
        holder.Season.setText("Season: " + variant.Season);
    }

    @Override
    public int getItemCount() {
        return riceList.size();
    }
}
