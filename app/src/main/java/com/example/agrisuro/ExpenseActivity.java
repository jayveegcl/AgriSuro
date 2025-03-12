package com.example.agrisuro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {
    private RecyclerView expenseRecyclerView; // ✅ Changed from ListView to RecyclerView
    private TextView totalExpenseTextView;
    private Button addExpenseButton;
    private ExpenseAdapter adapter;
    private List<Expense> expenseList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        expenseRecyclerView = findViewById(R.id.expenseRecyclerView); // ✅ Initialize RecyclerView
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        addExpenseButton = findViewById(R.id.expenseButton);

        db = FirebaseFirestore.getInstance();
        expenseList = new ArrayList<>();
        adapter = new ExpenseAdapter(this, expenseList);

        // ✅ Set RecyclerView properties
        expenseRecyclerView.setAdapter(adapter);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadExpenses();

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpenseActivity.this, AddExpenseActivity.class));
            }
        });
    }

    private void loadExpenses() {
        db.collection("expenses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                expenseList.clear();
                double totalExpense = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Expense expense = document.toObject(Expense.class);
                    expenseList.add(expense);
                    totalExpense += expense.getAmount();
                }
                adapter.notifyDataSetChanged();
                totalExpenseTextView.setText("Total: ₱" + totalExpense);
            }
        });
    }
}
