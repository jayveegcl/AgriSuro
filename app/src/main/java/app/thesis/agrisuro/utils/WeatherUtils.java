package app.thesis.agrisuro.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import app.thesis.agrisuro.R;

public class WeatherUtils {

    /**
     * Returns a color resource ID based on the weather condition
     */
    public static int getConditionColor(Context context, String condition) {
        String lowerCondition = condition.toLowerCase();

        if (lowerCondition.contains("sunny")) {
            return ContextCompat.getColor(context, R.color.yellow_500);
        } else if (lowerCondition.contains("partly cloudy")) {
            return ContextCompat.getColor(context, R.color.blue_300);
        } else if (lowerCondition.contains("scattered showers")) {
            return ContextCompat.getColor(context, R.color.blue_400);
        } else if (lowerCondition.contains("rain")) {
            return ContextCompat.getColor(context, R.color.blue_600);
        } else {
            return ContextCompat.getColor(context, R.color.gray_500);
        }
    }
}

