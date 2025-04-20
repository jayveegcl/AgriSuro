package app.thesis.agrisuro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.adapter.ResourceItemAdapter;
import app.thesis.agrisuro.models.ResourceItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceCenterFragment extends Fragment implements ResourceItemAdapter.OnResourceItemClickListener {

    private TabLayout tabLayout;
    private EditText searchInput;
    private ImageButton filterButton;
    private RecyclerView resourcesRecyclerView;
    private TextView noResultsText;
    private View detailContainer;
    private View resourcesContainer;
    private Button backButton;

    private List<ResourceItem> allResources;
    private List<ResourceItem> filteredResources;
    private ResourceItemAdapter adapter;
    private String currentCategory = "rice-variants";
    private String searchQuery = "";
    private List<String> favorites = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource_center, container, false);

        // Initialize UI components
        tabLayout = view.findViewById(R.id.resource_tabs);
        searchInput = view.findViewById(R.id.search_input);
        filterButton = view.findViewById(R.id.filter_button);
        resourcesRecyclerView = view.findViewById(R.id.resources_recycler_view);
        noResultsText = view.findViewById(R.id.no_results_text);
        detailContainer = view.findViewById(R.id.detail_container);
        resourcesContainer = view.findViewById(R.id.resources_container);
        backButton = view.findViewById(R.id.back_button);

        // Initialize data
        allResources = getMockResources();
        filteredResources = new ArrayList<>(allResources);

        // Set up RecyclerView
        adapter = new ResourceItemAdapter(getContext(), filteredResources, favorites, this);
        resourcesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        resourcesRecyclerView.setAdapter(adapter);

        // Set up search
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchQuery = s.toString().toLowerCase();
                filterResources();
            }
        });

        // Set up tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentCategory = "rice-variants";
                        break;
                    case 1:
                        currentCategory = "crop-diseases";
                        break;
                    case 2:
                        currentCategory = "weed-management";
                        break;
                }
                filterResources();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set up back button
        backButton.setOnClickListener(v -> {
            detailContainer.setVisibility(View.GONE);
            resourcesContainer.setVisibility(View.VISIBLE);
        });

        return view;
    }

    private void filterResources() {
        filteredResources.clear();

        filteredResources.addAll(allResources.stream()
                .filter(item -> item.getCategory().equals(currentCategory))
                .filter(item -> searchQuery.isEmpty() ||
                        item.getTitle().toLowerCase().contains(searchQuery) ||
                        item.getDescription().toLowerCase().contains(searchQuery))
                .collect(Collectors.toList()));

        adapter.notifyDataSetChanged();

        if (filteredResources.isEmpty()) {
            noResultsText.setVisibility(View.VISIBLE);
        } else {
            noResultsText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResourceItemClick(ResourceItem item) {
        // Show detail view
        detailContainer.setVisibility(View.VISIBLE);
        resourcesContainer.setVisibility(View.GONE);

        // Set detail data
        DetailCardFragment detailFragment = (DetailCardFragment) getChildFragmentManager()
                .findFragmentById(R.id.detail_fragment);
        if (detailFragment != null) {
            detailFragment.setResourceItem(item, favorites.contains(item.getId()));
        }
    }

    @Override
    public void onToggleFavorite(String id) {
        if (favorites.contains(id)) {
            favorites.remove(id);
        } else {
            favorites.add(id);
        }
        adapter.notifyDataSetChanged();

        // Update detail view if open
        if (detailContainer.getVisibility() == View.VISIBLE) {
            DetailCardFragment detailFragment = (DetailCardFragment) getChildFragmentManager()
                    .findFragmentById(R.id.detail_fragment);
            if (detailFragment != null) {
                detailFragment.updateFavoriteStatus(favorites.contains(detailFragment.getCurrentItemId()));
            }
        }
    }

    private List<ResourceItem> getMockResources() {
        // This would come from a database or API in a real app
        List<ResourceItem> resources = new ArrayList<>();

        // Rice variants
        resources.add(new ResourceItem(
                "1",
                "NSIC Rc 222",
                "High-yielding rice variety suitable for irrigated lowlands with resistance to blast and bacterial leaf blight.",
                "https://images.unsplash.com/photo-1536054695850-0c8f91f68a56?w=800&q=80",
                "rice-variants",
                new String[]{"High-yield", "Disease-resistant", "Irrigated", "120-days"}
        ));

        resources.add(new ResourceItem(
                "2",
                "NSIC Rc 160",
                "Drought-tolerant rice variety ideal for rainfed areas with moderate resistance to common pests.",
                "https://images.unsplash.com/photo-1626016750647-215e49b44fc7?w=800&q=80",
                "rice-variants",
                new String[]{"Drought-tolerant", "Rainfed", "110-days"}
        ));

        resources.add(new ResourceItem(
                "3",
                "NSIC Rc 216",
                "Salt-tolerant rice variety suitable for coastal areas with good grain quality.",
                "https://images.unsplash.com/photo-1559684459-af4974cc2145?w=800&q=80",
                "rice-variants",
                new String[]{"Salt-tolerant", "Coastal", "115-days"}
        ));

        // Crop diseases
        resources.add(new ResourceItem(
                "4",
                "Bacterial Leaf Blight",
                "A serious bacterial disease causing wilting of seedlings and yellowing and drying of leaves.",
                "https://images.unsplash.com/photo-1530836369250-ef72a3f5cda8?w=800&q=80",
                "crop-diseases",
                new String[]{"Bacterial", "Leaves", "Wilting", "Yellowing"}
        ));

        resources.add(new ResourceItem(
                "5",
                "Rice Blast",
                "Fungal disease causing lesions on leaves, stems, and grains, potentially leading to significant yield loss.",
                "https://images.unsplash.com/photo-1574323347407-f5e1c5a1ec21?w=800&q=80",
                "crop-diseases",
                new String[]{"Fungal", "Lesions", "Yield-loss"}
        ));

        resources.add(new ResourceItem(
                "6",
                "Sheath Blight",
                "Fungal disease affecting the sheath of rice plants, causing irregular lesions and potential lodging.",
                "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800&q=80",
                "crop-diseases",
                new String[]{"Fungal", "Sheath", "Lesions", "Lodging"}
        ));

        // Weed management
        resources.add(new ResourceItem(
                "7",
                "Barnyard Grass Control",
                "Methods to manage barnyard grass, a common weed in rice fields that competes for nutrients.",
                "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800&q=80",
                "weed-management",
                new String[]{"Grassy", "Competitive", "Pre-emergence"}
        ));

        resources.add(new ResourceItem(
                "8",
                "Water Hyacinth Management",
                "Techniques to control water hyacinth, an invasive aquatic weed affecting irrigation channels.",
                "https://images.unsplash.com/photo-1621928372414-30e144d51d49?w=800&q=80",
                "weed-management",
                new String[]{"Aquatic", "Invasive", "Irrigation"}
        ));

        resources.add(new ResourceItem(
                "9",
                "Integrated Weed Management",
                "Comprehensive approach combining multiple methods for effective and sustainable weed control.",
                "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800&q=80",
                "weed-management",
                new String[]{"Integrated", "Sustainable", "Multiple-methods"}
        ));

        return resources;
    }
}
