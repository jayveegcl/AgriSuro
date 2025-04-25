package app.thesis.agrisuro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private List<Income> incomes = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_income, parent, false);
        return new IncomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Income currentIncome = incomes.get(position);
        holder.bind(currentIncome);
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Income income);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class IncomeViewHolder extends RecyclerView.ViewHolder {
        private TextView descriptionTextView;
        private TextView amountTextView;
        private TextView sourceTextView;
        private TextView dateTextView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.income_description);
            amountTextView = itemView.findViewById(R.id.income_amount);
            sourceTextView = itemView.findViewById(R.id.income_source);
            dateTextView = itemView.findViewById(R.id.income_date);

            // Set click listener for item to trigger deletion dialog
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(incomes.get(position));
                }
            });
        }

        public void bind(Income income) {
            descriptionTextView.setText(income.getDescription());
            amountTextView.setText(String.format(Locale.getDefault(), "₱%.2f", income.getAmount()));
            sourceTextView.setText(income.getSource());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String dateString = sdf.format(new Date(income.getDate()));
            dateTextView.setText(dateString);
        }
    }
}