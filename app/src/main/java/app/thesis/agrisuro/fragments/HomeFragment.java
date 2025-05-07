package app.thesis.agrisuro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.adapter.FeaturesAdapter;
import app.thesis.agrisuro.adapter.NewsAdapter;
import app.thesis.agrisuro.cropcalendar.CropPlannerActivity;
import app.thesis.agrisuro.models.Feature;
import app.thesis.agrisuro.models.NewsItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView featuresRecyclerView;
    private RecyclerView newsRecyclerView;
    private FeaturesAdapter featuresAdapter;
    private NewsAdapter newsAdapter;
    private MaterialCardView weatherCard, cropGuideCard, diseaseDetectionCard, marketPriceCard;
    private TextView versionInfoText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        //featuresRecyclerView = root.findViewById(R.id.features_recycler_view);
        newsRecyclerView = root.findViewById(R.id.news_recycler_view);
        weatherCard = root.findViewById(R.id.weather_card);
        cropGuideCard = root.findViewById(R.id.crop_guide_card);
        diseaseDetectionCard = root.findViewById(R.id.disease_detection_card);
        marketPriceCard = root.findViewById(R.id.market_price_card);
        versionInfoText = root.findViewById(R.id.version_info);

        // Set up features recycler view
        //setupFeaturesRecyclerView();

        // Set up news recycler view
        setupNewsRecyclerView();

        // Set up card click listeners
        setupCardClickListeners();

        // Set version info
        setVersionInfo();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Any additional setup once the view is created
    }

    private void setupFeaturesRecyclerView() {
        featuresRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Create sample features
        List<Feature> features = new ArrayList<>();
        features.add(new Feature(R.drawable.ic_weather, "Weather Forecast", "Get real-time weather updates and forecasts for your area"));
        features.add(new Feature(R.drawable.ic_plant, "Rice Planting Guide", "Learn the best practices for planting rice varieties"));
        features.add(new Feature(R.drawable.ic_pesticide, "Disease Detection", "Identify rice diseases by uploading photos"));
        features.add(new Feature(R.drawable.ic_expense, "Expense Tracker", "Track your farming expenses and income"));

        // Set adapter
        featuresAdapter = new FeaturesAdapter(features);
        featuresRecyclerView.setAdapter(featuresAdapter);
    }

    private void setupNewsRecyclerView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create sample news items
        List<NewsItem> newsItems = new ArrayList<>();
        newsItems.add(new NewsItem("New Rice Variety Released",
                "A new drought-resistant rice variety has been released that can increase yields by 15%",
                "2025-05-01", R.drawable.ic_plant));
        newsItems.add(new NewsItem("Farming Subsidy Program Announced",
                "The government has announced a new subsidy program for rice farmers",
                "2025-04-28", R.drawable.ic_circle));
        newsItems.add(new NewsItem("Climate Change Impact on Rice Production",
                "New study reveals how climate change is affecting rice production in Southeast Asia",
                "2025-04-25", R.drawable.ic_add));

        // Set adapter
        newsAdapter = new NewsAdapter(newsItems);
        newsRecyclerView.setAdapter(newsAdapter);
    }

    private void setupCardClickListeners() {
        // Weather forecast card click listener
        weatherCard.setOnClickListener(v -> {
            navigateToWeatherForecast();
        });

        // Crop guide card click listener
        cropGuideCard.setOnClickListener(v -> {
            navigateToCropGuide();
        });

        // Disease detection card click listener
        diseaseDetectionCard.setOnClickListener(v -> {
            navigateToDiseaseDetection();
        });

        // Market price card click listener
        marketPriceCard.setOnClickListener(v -> {
            navigateToExpenseTracker();
        });
    }

    private void navigateToWeatherForecast() {
        // Navigate to weather forecast fragment
        Toast.makeText(getContext(), "Weather Forecast", Toast.LENGTH_SHORT).show();
        // Implement navigation logic using Navigation Component
        // NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_weatherFragment);
    }

    private void navigateToCropGuide() {
        // Navigate to crop guide fragment
        Toast.makeText(getContext(), "Planting Guide", Toast.LENGTH_SHORT).show();
        // Implement navigation logic using Navigation Component
        // NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_cropGuideFragment);
    }

    private void navigateToDiseaseDetection() {
        // Navigate to disease detection fragment
        Toast.makeText(getContext(), "Rice Disease Detection", Toast.LENGTH_SHORT).show();
        // Implement navigation logic using Navigation Component
        // NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_diseaseDetectionFragment);
    }

    private void navigateToExpenseTracker() {
        // Navigate to market price fragment
        Toast.makeText(getContext(), "Expense Tracker", Toast.LENGTH_SHORT).show();
        // Implement navigation logic using Navigation Component
        // NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_expenseTrackerFragment);
        // Launch crop calendar activity
        Intent intent = new Intent(getActivity(), CropPlannerActivity.class);
        startActivity(intent);
    }

    private void setVersionInfo() {
        // Set version info
        String versionName = "1.00";
        try {
            versionName = requireActivity().getPackageManager().getPackageInfo(
                    requireActivity().getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        versionInfoText.setText("Version " + versionName);
    }
}