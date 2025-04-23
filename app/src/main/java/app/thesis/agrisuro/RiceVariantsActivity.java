package app.thesis.agrisuro;

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

public class RiceVariantsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RiceVariantsAdapter adapter;
    private List<Pair<String, RiceVariants>> riceList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_rice_variants);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db.collection("rice_variants")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        RiceVariants variant = doc.toObject(RiceVariants.class);
                        riceList.add(new Pair<>(doc.getId(), variant));
                    }

                    adapter = new RiceVariantsAdapter(riceList);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RiceVariantsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace(); // Also logs in Logcat
                });
    }
}
