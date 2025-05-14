package  app.thesis.agrisuro;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import app.thesis.agrisuro.expense.FinanceTrackerFragment;
import app.thesis.agrisuro.fragments.HomeFragment;
import app.thesis.agrisuro.fragments.ProfileFragment;
import app.thesis.agrisuro.fragments.ResourceCenterFragment;
import app.thesis.agrisuro.fragments.WeatherFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        // Initialize bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Set up floating action button for resources
        {
            // Navigate to Resource Center
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ResourceCenterFragment())
                    .commit();
            // Update selected item in bottom navigation
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        };


        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_weather) {
            selectedFragment = new WeatherFragment();
        } else if (itemId == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        } else if (itemId == R.id.nav_resources) {
            selectedFragment = new ResourceCenterFragment();
        } else if (itemId == R.id.nav_expenses) {
            selectedFragment = new FinanceTrackerFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }

        return false;
    }
}