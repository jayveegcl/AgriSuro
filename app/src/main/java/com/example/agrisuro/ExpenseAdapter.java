package com.example.agrisuro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.DecimalFormat;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private Context context;
    private List<Expense> expenseList;
    private static final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");

    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.expenseName.setText(expense.getName());
        holder.expenseAmount.setText("₱" + currencyFormat.format(expense.getAmount())); // Format for currency
    }

    @Override
    public int getItemCount() {
        return (expenseList != null) ? expenseList.size() : 0;
    }

    public void updateExpenseList(List<Expense> newList) {
        this.expenseList = newList;
        notifyDataSetChanged(); // Refresh UI after data update
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseAmount;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.expenseName);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
        }
    }
}
