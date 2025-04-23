package app.thesis.agrisuro;

public class RiceVariants {
    public String Average_Yield;
    public String Environment;
    public String Height;
    public String Maturity;
    public String Maximum_Yield;
    public String Season;

    public RiceVariants() {}  // Needed for Firestore deserialization

    public RiceVariants(String Average_Yield, String Environment, String Height, String Maturity, String Maximum_Yield, String Season) {
        this.Average_Yield = Average_Yield;
        this.Environment = Environment;
        this.Height = Height;
        this.Maturity = Maturity;
        this.Maximum_Yield = Maximum_Yield;
        this.Season = Season;
    }
}