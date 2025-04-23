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
        TextView name, type, maturity, yield, resistance, area;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.riceName);
            type = itemView.findViewById(R.id.riceType);
            maturity = itemView.findViewById(R.id.riceMaturity);
            yield = itemView.findViewById(R.id.riceYield);
            resistance = itemView.findViewById(R.id.riceResistance);
            area = itemView.findViewById(R.id.riceArea);
        }
    }

    @NonNull
    @Override
    public RiceVariantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rice_variants, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RiceVariantsAdapter.ViewHolder holder, int position) {
        Pair<String, RiceVariants> pair = riceList.get(position);
        RiceVariants variant = pair.second;

        holder.name.setText(pair.first);
        holder.type.setText("Type: " + variant.type);
        holder.maturity.setText("Maturity: " + variant.maturity_days + " days");
        holder.yield.setText("Yield: " + variant.yield);
        holder.resistance.setText("Resistance: " + variant.resistance);
        holder.area.setText("Area: " + variant.suitable_area);
    }

    @Override
    public int getItemCount() {
        return riceList.size();
    }
}
