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
import app.thesis.agrisuro.firebase.RiceSoilActivity;
import app.thesis.agrisuro.firebase.RiceGrowthActivity;

public class ResourceCenterFragment extends Fragment {

    private MaterialCardView cardRiceVariants, cardRiceDiseases, cardRiceWeeds, cardRiceInsects, cardPesticides, cardFertilizers, cardSoil, cardGrowth;
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
        cardSoil = view.findViewById(R.id.cardSoil);
        cardGrowth = view.findViewById(R.id.cardGrowth);
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

        cardSoil.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceSoilActivity.class));
        });

        cardGrowth.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceGrowthActivity.class));
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
                .setMessage("Ang Resource Center na ito ay nagbibigay ng impormasyon tungkol sa pagtatanim ng palay, " +
                        "kabilang ang mga uri ng palay, karaniwang sakit, pamamahala ng mga damo, " +
                        "inirerekumendang pestisidyo, pataba, atbp. " +
                        "Pindutin and anumang card para matuto pa tungkol sa paksang iyon.")
                .setPositiveButton("Got it", (dialog, which) -> dialog.dismiss())
                .show();
    }
}