package app.thesis.agrisuro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import app.thesis.agrisuro.R;


import app.thesis.agrisuro.RiceVariantsActivity;

public class ResourceCenterFragment extends Fragment {

    private Button btnRiceVariants, btnRiceDiseases, btnRiceWeeds, btnPesticides, btnFertilizers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource_center, container, false);

        btnRiceVariants = view.findViewById(R.id.btnRiceVariants);
        btnRiceDiseases = view.findViewById(R.id.btnRiceDiseases);
        btnRiceWeeds = view.findViewById(R.id.btnRiceWeeds);
        btnPesticides = view.findViewById(R.id.btnPesticides);
        btnFertilizers = view.findViewById(R.id.btnFertilizers);

        btnRiceVariants.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceVariantsActivity.class));
        });

        /*btnRiceDiseases.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceDiseasesActivity.class));
        });*/

        // You can uncomment when those activities are ready
        /*
        btnRiceWeeds.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RiceWeedsActivity.class));
        });

        btnPesticides.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PesticidesActivity.class));
        });

        btnFertilizers.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), FertilizersActivity.class));
        });
        */

        return view;
    }
}
