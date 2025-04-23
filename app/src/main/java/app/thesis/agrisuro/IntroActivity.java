package app.thesis.agrisuro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private static final String PREF_NAME = "AgriSuroPrefs";
    private static final String FIRST_TIME_LAUNCH = "isFirstTimeLaunch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Initialize the Get Started button
        Button getStartedButton = findViewById(R.id.get_started_button);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set first time launch to false
                setFirstTimeLaunchToFalse();

                // Navigate to login/signup activity
                // For now, we'll navigate to MainActivity
                // In a real app, you would navigate to a LoginActivity
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setFirstTimeLaunchToFalse() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FIRST_TIME_LAUNCH, false);
        editor.apply();
    }

    // Method to check if it's the first time launch
    public static boolean isFirstTimeLaunch(SharedPreferences preferences) {
        return preferences.getBoolean(FIRST_TIME_LAUNCH, true);
    }
}
