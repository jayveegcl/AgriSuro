package app.thesis.agrisuro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.thesis.agrisuro.LoginActivity;
import app.thesis.agrisuro.R;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private Button logoutButton;
    private Button aboutButton;
    private TextView textView;
    private TextView userEmail;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        logoutButton = view.findViewById(R.id.logout);
        aboutButton = view.findViewById(R.id.about_btn);
        textView = view.findViewById(R.id.user_details);
        userEmail = view.findViewById(R.id.user_email);
        user = auth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        } else {
            // Show welcome message with user's display name or email
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                textView.setText("Welcome, " + displayName + "!");
            } else {
                textView.setText("Welcome!");
            }

            // Set email in the designated TextView
            userEmail.setText(user.getEmail());
        }

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        // Set up about button
        aboutButton.setOnClickListener(v -> {
            showAboutDialog();
        });

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        return view;
    }

    /**
     * Shows an about dialog with information about the application
     */
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("About AgriSuro");
        builder.setMessage("AgriSuro is an agricultural application developed as a thesis project. " +
                "This app provides farmers with easy access to agricultural knowledge.\n\n" +
                "Version 1.0.0\n" +
                "© 2025 AgriSuro Team");

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        // Optional: Add a website or contact info button
        builder.setNeutralButton("Contact Us", (dialog, which) -> {
            // Here you can open email or website
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"agrisuro@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "AgriSuro Inquiry");
            startActivity(Intent.createChooser(intent, "Contact via"));
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}