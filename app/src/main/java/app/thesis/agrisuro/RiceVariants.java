package app.thesis.agrisuro;

public class RiceVariants {
    public String type;
    public int maturity_days;
    public String yield;
    public String resistance;
    public String suitable_area;

    public RiceVariants() {}  // Needed for Firestore deserialization

    public RiceVariants(String type, int maturity_days, String yield, String resistance, String suitable_area) {
        this.type = type;
        this.maturity_days = maturity_days;
        this.yield = yield;
        this.resistance = resistance;
        this.suitable_area = suitable_area;
    }
}