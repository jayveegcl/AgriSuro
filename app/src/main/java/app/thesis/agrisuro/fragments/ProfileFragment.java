package app.thesis.agrisuro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.thesis.agrisuro.LoginActivity;
import app.thesis.agrisuro.R;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private Button logoutButton;
    private Button aboutButton;
    private Button referencesButton;
    private Button deleteAccountButton;
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
        referencesButton = view.findViewById(R.id.references_btn);
        deleteAccountButton = view.findViewById(R.id.delete_account);
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

        // Set up references button
        referencesButton.setOnClickListener(v -> {
            showReferencesDialog();
        });

        // Set up delete account button
        deleteAccountButton.setOnClickListener(v -> {
            showDeleteAccountConfirmationDialog();
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

    /**
     * Shows a dialog with references and sources used in the application
     */
    private void showReferencesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("References & Sources");
        builder.setMessage(
                "Agricultural data and information in this application were sourced from:\n\n" +
                        "• Department of Agriculture - Philippines (DA)\n" +
                        "• Philippine Rice Research Institute (PhilRice)\n" +
                        "• International Rice Research Institute (IRRI)\n" +
                        "• Food and Agriculture Organization (FAO)\n" +
                        "• Bureau of Agricultural Statistics\n" +
                        "• Bureau of Plant Industry\n" +
                        "\n" +
                        "Weather data and forecast:\n" +
                        "• OpenWeatherMap\n" +
                        "\n" +
                        "Images and icons:\n" +
                        "• Flaticon\n" +
                        "• Material Design Icons\n\n" +
                        "For full details, please visit our website."
        );

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });

        // Add website button for more detailed references
        builder.setNeutralButton("Visit Website", (dialog, which) -> {
            // Open browser with your website for more detailed references
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("https://github.com/jayveegcl/AgriSuro"));
            startActivity(intent);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Shows an initial warning dialog for account deletion
     * This is the first step in a multi-step verification process
     */
    private void showDeleteAccountConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Warning: Account Deletion");
        builder.setMessage("WARNING: You are about to permanently delete your account.\n\n" +
                "• All your data will be permanently erased\n" +
                "• This action CANNOT be reversed\n" +
                "• You will lose access to all features\n" +
                "• You will need to create a new account to use AgriSuro again");

        // Cancel button - made more prominent
        builder.setNegativeButton("Keep My Account", (dialog, which) -> {
            dialog.dismiss();
            Toast.makeText(requireContext(), "Account deletion cancelled", Toast.LENGTH_SHORT).show();
        });

        // If user still wants to proceed, show the second verification step
        builder.setPositiveButton("I Want To Delete My Account", (dialog, which) -> {
            showDeleteAccountSecondVerification();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Shows a second verification dialog with password confirmation
     * This is the second step in the account deletion process
     */
    private void showDeleteAccountSecondVerification() {
        // Inflate the dialog layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_account, null);

        // Get references to the views
        final EditText passwordEditText = dialogView.findViewById(R.id.password_confirmation);
        final CheckBox confirmationCheckBox = dialogView.findViewById(R.id.confirmation_checkbox);
        final EditText confirmationText = dialogView.findViewById(R.id.confirmation_text);
        final Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        final Button deleteButton = dialogView.findViewById(R.id.delete_button);

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Confirm Account Deletion");
        builder.setView(dialogView);
        builder.setCancelable(false); // Prevent dismissing by tapping outside

        // Create and show the dialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Initially disable the delete button
        deleteButton.setEnabled(false);

        // Add text watcher to check for the matching confirmation phrase
        confirmationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Only enable delete button if both password is entered, checkbox is checked,
                // and the confirmation text matches exactly
                deleteButton.setEnabled(
                        !passwordEditText.getText().toString().isEmpty() &&
                                confirmationCheckBox.isChecked() &&
                                "DELETE MY ACCOUNT".equals(s.toString())
                );
            }
        });

        // Add listener for the checkbox
        confirmationCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Check all conditions when checkbox state changes
            deleteButton.setEnabled(
                    !passwordEditText.getText().toString().isEmpty() &&
                            isChecked &&
                            "DELETE MY ACCOUNT".equals(confirmationText.getText().toString())
            );
        });

        // Add text watcher for password field
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Check all conditions when password text changes
                deleteButton.setEnabled(
                        !s.toString().isEmpty() &&
                                confirmationCheckBox.isChecked() &&
                                "DELETE MY ACCOUNT".equals(confirmationText.getText().toString())
                );
            }
        });

        // Set up cancel button
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(requireContext(), "Account deletion cancelled", Toast.LENGTH_SHORT).show();
        });

        // Set up delete button
        deleteButton.setOnClickListener(v -> {
            // Verify the password
            String password = passwordEditText.getText().toString();

            // Attempt to reauthenticate the user to ensure recent authentication
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

            user.reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> {
                        dialog.dismiss();
                        // Password verified, show final countdown dialog
                        showFinalDeletionCountdown();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Incorrect password. Please try again.", Toast.LENGTH_LONG).show();
                        passwordEditText.setText("");
                        passwordEditText.requestFocus();
                    });
        });
    }

    /**
     * Shows a final countdown dialog with a timer before deletion
     * This is the third and final step in the account deletion process
     */
    private void showFinalDeletionCountdown() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Final Warning");
        builder.setMessage("Your account will be permanently deleted in 10 seconds.\n\nTap CANCEL to keep your account.");
        builder.setCancelable(false); // Prevent dismissing by tapping outside

        // Create countdown timer
        CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (builder.create().isShowing()) {
                    // Update countdown message
                    builder.setMessage("Your account will be permanently deleted in " +
                            (millisUntilFinished / 1000) + " seconds.\n\nTap CANCEL to keep your account.");
                }
            }

            @Override
            public void onFinish() {
                // Time's up, proceed with deletion
                deleteUserAccount();
            }
        };

        // Cancel button - Last chance to abort
        builder.setNegativeButton("CANCEL", (dialog, which) -> {
            countDownTimer.cancel();
            dialog.dismiss();
            Toast.makeText(requireContext(), "Account deletion cancelled", Toast.LENGTH_SHORT).show();
        });

        // No positive button as countdown will automatically trigger deletion

        AlertDialog dialog = builder.create();
        dialog.show();

        // Start the countdown
        countDownTimer.start();
    }

    /**
     * Handles the actual account deletion process
     */
    private void deleteUserAccount() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Delete the user account
            currentUser.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Account deleted successfully
                            Toast.makeText(requireContext(), "Account has been permanently deleted", Toast.LENGTH_LONG).show();

                            // Navigate back to login screen
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            requireActivity().finish();
                        } else {
                            // If delete fails, inform the user
                            String errorMessage = task.getException() != null ?
                                    task.getException().getMessage() :
                                    "Unknown error occurred";

                            Toast.makeText(requireContext(),
                                    "Failed to delete account: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}