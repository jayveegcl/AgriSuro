package app.thesis.agrisuro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.adapter.MainTabAdapter;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button weatherButton;
    private Button resourceButton;
    private Button expenseButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        weatherButton = view.findViewById(R.id.weather_button);
        resourceButton = view.findViewById(R.id.resource_button);
        expenseButton = view.findViewById(R.id.expense_button);

        // Set up ViewPager with adapter
        MainTabAdapter tabAdapter = new MainTabAdapter(this);
        viewPager.setAdapter(tabAdapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.resource_center);
                    break;
                case 1:
                    tab.setText(R.string.expense_tracker);
                    break;
                case 2:
                    tab.setText(R.string.pesticide_fertilizer);
                    break;
            }
        }).attach();

        // Set up button click listeners
        weatherButton.setOnClickListener(v -> {
            // Navigate to Weather Fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new WeatherFragment())
                    .addToBackStack(null)
                    .commit();
        });

        resourceButton.setOnClickListener(v -> {
            // Set tab to Resource Center
            viewPager.setCurrentItem(0);
        });

        expenseButton.setOnClickListener(v -> {
            // Set tab to Expense Tracker
            viewPager.setCurrentItem(1);
        });

        return view;
    }
}
