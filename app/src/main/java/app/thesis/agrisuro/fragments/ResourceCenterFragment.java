package app.thesis.agrisuro.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.adapter.ResourceItemAdapter;
import app.thesis.agrisuro.models.ResourceItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceCenterFragment extends Fragment implements ResourceItemAdapter.OnResourceItemClickListener {

    private TabLayout tabLayout;
    private EditText etSearch;
    private ImageButton btnFilter;
    private RecyclerView recyclerResources;
    private LinearLayout layoutEmptyState;
    private FrameLayout containerDetail;
    private Button btnBack;

    private List<ResourceItem> allResources;
    private List<ResourceItem> filteredResources;
    private ResourceItemAdapter adapter;
    private String currentCategory = "rice-variants";
    private String searchQuery = "";
    private List<String> favorites = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_resource_center, container, false);

        // Initialize UI components
        tabLayout = view.findViewById(R.id.tab_layout);
        etSearch = view.findViewById(R.id.etSearch);
        btnFilter = view.findViewById(R.id.btnFilter);
        recyclerResources = view.findViewById(R.id.recyclerResources);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        containerDetail = view.findViewById(R.id.containerDetail);
        btnBack = view.findViewById(R.id.btnBack);

        // Initialize data
        allResources = getMockResources();
        filteredResources = new ArrayList<>(allResources);

        // Set up RecyclerView
        adapter = new ResourceItemAdapter(getContext(), filteredResources, favorites, this);
        recyclerResources.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerResources.setAdapter(adapter);

        // Set up search
        etSearch.addTextChangedListener(new TextWatcher() {
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
                        currentCategory = "rice-diseases";
                        break;
                    case 2:
                        currentCategory = "rice-weeds";
                        break;
                    case 3:
                        currentCategory = "insects";
                        break;
                    case 4:
                        currentCategory = "pesticide";
                        break;
                    case 5:
                        currentCategory = "fertilizer";
                        break;
                }
                filterResources();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set up filter button
        btnFilter.setOnClickListener(v -> {
            // Implement filter functionality here
            // For example, show a dialog with filter options
        });

        // Set up back button
        btnBack.setOnClickListener(v -> {
            containerDetail.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            recyclerResources.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
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
            layoutEmptyState.setVisibility(View.VISIBLE);
            recyclerResources.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            recyclerResources.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResourceItemClick(ResourceItem item) {
        // Show detail view
        containerDetail.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        recyclerResources.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);

        // Set detail data
        DetailCardFragment detailFragment = new DetailCardFragment();
        Bundle args = new Bundle();
        args.putString("resourceId", item.getId());
        args.putBoolean("isFavorite", favorites.contains(item.getId()));
        detailFragment.setArguments(args);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.containerDetail, detailFragment)
                .commit();
    }

    public void onToggleFavorite(String id) {
        if (favorites.contains(id)) {
            favorites.remove(id);
        } else {
            favorites.add(id);
        }
        adapter.notifyDataSetChanged();

        // Update detail view if open
        if (containerDetail.getVisibility() == View.VISIBLE) {
            DetailCardFragment detailFragment = (DetailCardFragment) getChildFragmentManager()
                    .findFragmentById(R.id.containerDetail);
            if (detailFragment != null) {
                detailFragment.updateFavoriteStatus(favorites.contains(id));
            }
        }
    }

    private List<ResourceItem> getMockResources() {
        // This would come from a database or API in a real app
        List<ResourceItem> resources = new ArrayList<>();

        // Rice variants
        resources.add(new ResourceItem(

        ));

        resources.add(new ResourceItem(

        ));

        resources.add(new ResourceItem(

        ));

        // Rice diseases
        resources.add(new ResourceItem(

        ));

        resources.add(new ResourceItem(

        ));

        resources.add(new ResourceItem(

        ));

        // Rice weeds
        resources.add(new ResourceItem(

        ));

        resources.add(new ResourceItem(

        ));

        // Insects
        resources.add(new ResourceItem(

        ));

        resources.add(new ResourceItem(

        ));

        // Pesticides
        resources.add(new ResourceItem(

        ));

        resources.add(new ResourceItem(

        ));

        // Fertilizers
        resources.add(new ResourceItem(

        ));

        resources.add(new ResourceItem(

        ));

        return resources;
    }
}