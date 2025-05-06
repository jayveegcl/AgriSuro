package app.thesis.agrisuro.firebase;

import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import app.thesis.agrisuro.R;

public class RicePesticidesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RicePesticidesAdapter adapter;
    private List<Pair<String, RicePesticides>> ricePesticideList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_rice_variants);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db.collection("rice_pesticides")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        RicePesticides variant = doc.toObject(RicePesticides.class);
                        ricePesticideList.add(new Pair<>(doc.getId(), variant));
                    }

                    adapter = new RicePesticidesAdapter(ricePesticideList);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RicePesticidesActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace(); // Also logs in Logcat
                });
    }
}
