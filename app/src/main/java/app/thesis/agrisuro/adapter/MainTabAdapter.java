package app.thesis.agrisuro.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import app.thesis.agrisuro.fragments.ResourceCenterFragment;
import app.thesis.agrisuro.fragments.ExpenseTrackerFragment;
import app.thesis.agrisuro.fragments.PesticideFertilizerFragment;

public class MainTabAdapter extends FragmentStateAdapter {

    public MainTabAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ResourceCenterFragment();
            case 1:
                return new ExpenseTrackerFragment();
            case 2:
                return new PesticideFertilizerFragment();
            default:
                return new ResourceCenterFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}