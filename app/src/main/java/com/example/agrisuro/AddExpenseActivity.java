package com.example.agrisuro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText expenseNameEditText, expenseAmountEditText;
    private Button saveExpenseButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseNameEditText = findViewById(R.id.expenseNameEditText);
        expenseAmountEditText = findViewById(R.id.expenseAmountEditText);
        saveExpenseButton = findViewById(R.id.saveExpenseButton);

        db = FirebaseFirestore.getInstance();

        saveExpenseButton.setOnClickListener(v -> addExpense());
    }

    private void addExpense() {
        String name = expenseNameEditText.getText().toString().trim();
        String amountStr = expenseAmountEditText.getText().toString().trim();

        if (name.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        Expense expense = new Expense(name, amount);

        db.collection("expenses").add(expense).addOnSuccessListener(documentReference -> {
            Toast.makeText(AddExpenseActivity.this, "Expense added successfully", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(AddExpenseActivity.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
        });
    }
}
