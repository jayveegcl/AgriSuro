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

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense currentExpense = expenses.get(position);
        holder.bind(currentExpense);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Expense expense);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView descriptionTextView;
        private TextView amountTextView;
        private TextView categoryTextView;
        private TextView dateTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.expense_description);
            amountTextView = itemView.findViewById(R.id.expense_amount);
            categoryTextView = itemView.findViewById(R.id.expense_category);
            dateTextView = itemView.findViewById(R.id.expense_date);

            // Set click listener for item to trigger deletion dialog
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(expenses.get(position));
                }
            });
        }

        public void bind(Expense expense) {
            descriptionTextView.setText(expense.getDescription());
            amountTextView.setText(String.format(Locale.getDefault(), "₱%.2f", expense.getAmount()));
            categoryTextView.setText(expense.getCategory());

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String dateString = sdf.format(new Date(expense.getDate()));
            dateTextView.setText(dateString);
        }
    }
}