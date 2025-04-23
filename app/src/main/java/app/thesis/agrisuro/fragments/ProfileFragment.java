package app.thesis.agrisuro.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.thesis.agrisuro.Login;
import app.thesis.agrisuro.R;

/**
 * ProfileFragment displays user profile information and settings options
 * including edit profile, about, settings, help & support, and logout functionality.
 */
public class ProfileFragment extends Fragment {

    private TextView tvUserName;
    private TextView tvUserEmail;
    private Button btnEditProfile;
    private LinearLayout layoutAbout;
    private LinearLayout layoutSettings;
    private LinearLayout layoutHelp;
    private Button btnLogout;
    private SharedPreferences sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        initViews(view);

        // Load user data
        loadUserData();

        // Set up click listeners
        setupClickListeners();
    }

    private void initViews(View view) {
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        layoutAbout = view.findViewById(R.id.layoutAbout);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("AgriSuroPrefs", Context.MODE_PRIVATE);
    }

    private void loadUserData() {
        // Load user data from SharedPreferences
        String userName = sharedPreferences.getString("user_name", "User");
        String userEmail = sharedPreferences.getString("user_email", "user@example.com");

        // Set user data to views
        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
    }

    private void setupClickListeners() {
        // Edit Profile button click listener
        btnEditProfile.setOnClickListener(v -> {
            // TODO: Implement edit profile functionality
            // For now, just show a toast message
            Toast.makeText(requireContext(), "Edit Profile clicked", Toast.LENGTH_SHORT).show();
            // You would typically start an EditProfileActivity here
            // Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
            // startActivity(intent);
        });

        // About section click listener
        layoutAbout.setOnClickListener(v -> {
            // TODO: Implement about screen navigation
            Toast.makeText(requireContext(), "About AgriSuro clicked", Toast.LENGTH_SHORT).show();
            // You would typically start an AboutActivity here
            // Intent intent = new Intent(requireActivity(), AboutActivity.class);
            // startActivity(intent);
        });

        // Settings section click listener
        layoutSettings.setOnClickListener(v -> {
            // TODO: Implement settings screen navigation
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show();
            // You would typically start a SettingsActivity here
            // Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            // startActivity(intent);
        });

        // Help & Support section click listener
        layoutHelp.setOnClickListener(v -> {
            // TODO: Implement help & support screen navigation
            Toast.makeText(requireContext(), "Help & Support clicked", Toast.LENGTH_SHORT).show();
            // You would typically start a HelpActivity here
            // Intent intent = new Intent(requireActivity(), HelpActivity.class);
            // startActivity(intent);
        });

        // Logout button click listener
        btnLogout.setOnClickListener(v -> {
            // Clear user session data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Navigate to login screen
            Intent intent = new Intent(requireActivity(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}
