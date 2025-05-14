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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.adapter.FeaturesAdapter;
import app.thesis.agrisuro.adapter.NewsAdapter;
import app.thesis.agrisuro.cropcalendar.CropPlannerActivity;
import app.thesis.agrisuro.firebase.RiceDiseasesActivity;
import app.thesis.agrisuro.firebase.RiceFertilizersActivity;
import app.thesis.agrisuro.firebase.RiceVariantsActivity;
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

        // Create sample news items with URLs
        List<NewsItem> newsItems = new ArrayList<>();
        newsItems.add(new NewsItem(
                "Philippine government resumes P20/kilo rice project in Cebu",
                "The National Food Authority (NFA) has started delivering well-milled rice to Cebu province in preparation for the resumption of the government’s sale of P20 per kilogram rice project through the Department of Agriculture (DA).",
                "2025-05-14",
                R.drawable.inquirer,
                "https://business.inquirer.net/525216/govt-resumes-p20-kilo-rice-project-in-cebu")); // Sample IRRI URL

        newsItems.add(new NewsItem(
                "PhilRice ramps up zinc-rich rice production",
                "PhilRice Negros plants zinc-rich rice varieties NSIC Rc 460 and NSIC Rc 648 to boost seed supply for the Visayas.",
                "2025-05-07",
                R.drawable.philrice,
                "https://www.philrice.gov.ph/philrice-ramps-up-zinc-rich-rice-production/")); // Sample Department of Agriculture URL

        newsItems.add(new NewsItem(
                "NIA and IRRI promote investment-targeting solutions for sustainable rice and water management in PH",
                "The National Irrigation Administration (NIA) and the International Rice Research Institute (IRRI) signed a Memorandum of Agreement to implement digital innovations for increasing rice yields, optimizing water management, and reducing greenhouse gas emissions in the Philippine rice sector.",
                "2025-04-22",
                R.drawable.irri,
                "https://www.irri.org/news-and-events/news/nia-and-irri-promote-investment-targeting-solutions-sustainable-rice-and-water")); // Sample PhilRice news URL

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
        Toast.makeText(getContext(), "Rice Varieties", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), RiceVariantsActivity.class);
        startActivity(intent);
    }

    private void navigateToCropGuide() {
        // Navigate to crop guide fragment
        Toast.makeText(getContext(), "Rice Diseases", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), RiceDiseasesActivity.class);
        startActivity(intent);
    }

    private void navigateToDiseaseDetection() {
        // Navigate to disease detection fragment
        Toast.makeText(getContext(), "Fertilizers", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), RiceFertilizersActivity.class);
        startActivity(intent);
    }

    private void navigateToExpenseTracker() {
        // Navigate to market price fragment
        Toast.makeText(getContext(), "Crop Calendar", Toast.LENGTH_SHORT).show();
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