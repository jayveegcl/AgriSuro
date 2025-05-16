package app.thesis.agrisuro;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail;
    private Button buttonResetPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView backToLogin;
    private AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        // Initialize fade-in animation
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        editTextEmail = findViewById(R.id.email);
        buttonResetPassword = findViewById(R.id.btn_reset_password);
        progressBar = findViewById(R.id.progressBar);
        backToLogin = findViewById(R.id.back_to_login);

        // Apply animations
        findViewById(R.id.logo).startAnimation(fadeIn);
        findViewById(R.id.forgot_password_title).startAnimation(fadeIn);
        findViewById(R.id.forgot_password_subtitle).startAnimation(fadeIn);
        findViewById(R.id.email_layout).startAnimation(fadeIn);
        buttonResetPassword.startAnimation(fadeIn);
        backToLogin.startAnimation(fadeIn);

        // Set up back to login click listener
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous activity (LoginActivity)
            }
        });

        // Set up reset password button click listener
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void resetPassword() {
        String email = String.valueOf(editTextEmail.getText());

        // Validate email
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);

        // Send password reset email
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Password reset email sent. Please check your email.",
                                    Toast.LENGTH_LONG).show();
                            // Optionally clear the email field
                            editTextEmail.setText("");
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "Failed to send reset email: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}