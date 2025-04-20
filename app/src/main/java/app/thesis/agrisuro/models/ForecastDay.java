package app.thesis.agrisuro.models;
public class ForecastDay {
    private String day;
    private int highTemp;
    private int lowTemp;
    private String condition;
    private int rainProbability;

    public ForecastDay(String day, int highTemp, int lowTemp, String condition, int rainProbability) {
        this.day = day;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.condition = condition;
        this.rainProbability = rainProbability;
    }

    public String getDay() {
        return day;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public String getCondition() {
        return condition;
    }

    public int getRainProbability() {
        return rainProbability;
    }
}
