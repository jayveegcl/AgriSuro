package com.example.agrisuro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private Button weatherButton, expenseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); // Initialize Firebase
        setContentView(R.layout.activity_main);

        weatherButton = findViewById(R.id.weatherButton);
        expenseButton = findViewById(R.id.expenseTrackerButton);

        weatherButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WeatherActivity.class)));
        expenseButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ExpenseActivity.class)));
    }
}
