package app.thesis.agrisuro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    private List<SummaryItem> items;

    public SummaryAdapter(List<SummaryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary, parent, false);
        return new SummaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        SummaryItem item = items.get(position);
        holder.nameTextView.setText(item.getName());
        holder.amountTextView.setText(String.format(Locale.getDefault(), "₱%.2f", item.getAmount()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class SummaryViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView amountTextView;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.summary_name);
            amountTextView = itemView.findViewById(R.id.summary_amount);
        }
    }
}