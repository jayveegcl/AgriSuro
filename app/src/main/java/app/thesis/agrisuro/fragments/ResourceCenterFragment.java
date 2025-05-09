package app.thesis.agrisuro.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;

import app.thesis.agrisuro.R;
import app.thesis.agrisuro.firebase.RiceVariantsActivity;
import app.thesis.agrisuro.firebase.RiceDiseasesActivity;
import app.thesis.agrisuro.firebase.RiceInsectsActivity;
import app.thesis.agrisuro.firebase.RiceWeedsActivity;
import app.thesis.agrisuro.firebase.RicePesticidesActivity;
import app.thesis.agrisuro.firebase.RiceFertilizersActivity;

public class ResourceCenterFragment extends Fragment {

    private MaterialCardView cardRiceVariants, cardRiceDiseases, cardRiceWeeds, cardRiceInsects, cardPesticides, cardFertilizers;
    private FloatingActionButton fabHelp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource_center, container, false);

        // Initialize MaterialCardViews instead of Buttons
        cardRiceVariants = view.findViewById(R.id.cardRiceVariants);
        cardRiceDiseases = view.findViewById(R.id.cardRiceDiseases);
        cardRiceWeeds = view.findViewById(R.id.cardRiceWeeds);
        cardRiceInsects = view.findViewById(R.id.cardRiceInsects);
        cardPesticides = view.findViewById(R.id.cardPesticides);
        cardFertilizers = view.findViewById(R.id.cardFertilizers);
        fabHelp = view.findViewById(R.id.fabHelp);

        // Set click listeners for MaterialCardViews
        cardRiceVariants.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceVariantsActivity.class));
        });

        cardRiceDiseases.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceDiseasesActivity.class));
        });

        cardRiceWeeds.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceWeedsActivity.class));
        });

        cardRiceInsects.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceInsectsActivity.class));
        });
        
        cardPesticides.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RicePesticidesActivity.class));
        });

        cardFertilizers.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceFertilizersActivity.class));
        });

        // Setup FAB for help
        fabHelp.setOnClickListener(v -> {
            showHelpDialog();
        });

        return view;
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Help Center")
                .setMessage("This Resource Center provides information about rice cultivation, " +
                        "including rice variants, common diseases, weed management, " +
                        "recommended pesticides, and fertilizers. " +
                        "Tap on any card to learn more about that topic.")
                .setPositiveButton("Got it", (dialog, which) -> dialog.dismiss())
                .show();
    }
}