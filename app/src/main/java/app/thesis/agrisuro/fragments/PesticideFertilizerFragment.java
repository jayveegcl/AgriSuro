package app.thesis.agrisuro.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

public class PesticideFertilizerFragment extends Fragment {

    private CardView pesticidesCard;
    private CardView fertilizersCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesticide_fertilizer, container, false);

        pesticidesCard = view.findViewById(R.id.pesticides_card);
        fertilizersCard = view.findViewById(R.id.fertilizers_card);

        pesticidesCard.setOnClickListener(v -> {
            // Navigate to pesticides detail screen
            // In a real app, this would navigate to a detailed pesticides screen
            Toast.makeText(getContext(), "Pesticides guide would open here", Toast.LENGTH_SHORT).show();
        });

        fertilizersCard.setOnClickListener(v -> {
            // Navigate to fertilizers detail screen
            // In a real app, this would navigate to a detailed fertilizers screen
            Toast.makeText(getContext(), "Fertilizers guide would open here", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
