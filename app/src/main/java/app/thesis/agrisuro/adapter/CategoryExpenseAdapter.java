package app.thesis.agrisuro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.models.CategoryExpense;

public class CategoryExpenseAdapter extends RecyclerView.Adapter<CategoryExpenseAdapter.ViewHolder> {

    private List<CategoryExpense> categoryExpenses;

    public CategoryExpenseAdapter(List<CategoryExpense> categoryExpenses) {
        this.categoryExpenses = categoryExpenses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryExpense categoryExpense = categoryExpenses.get(position);
        holder.categoryTextView.setText(categoryExpense.getCategory());
        holder.amountTextView.setText("₱" + categoryExpense.getTotal());
        holder.percentageTextView.setText(String.format("%.1f%%", categoryExpense.getPercentage() * 100));
        holder.progressBar.setProgress((int) (categoryExpense.getPercentage() * 100));
    }

    @Override
    public int getItemCount() {
        return categoryExpenses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView;
        TextView amountTextView;
        TextView percentageTextView;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.category_name);
            amountTextView = itemView.findViewById(R.id.category_amount);
            percentageTextView = itemView.findViewById(R.id.category_percentage);
            progressBar = itemView.findViewById(R.id.category_progress);
        }
    }
}