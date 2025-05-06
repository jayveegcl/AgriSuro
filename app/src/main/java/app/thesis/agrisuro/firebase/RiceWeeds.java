package app.thesis.agrisuro.firebase;

public class RiceWeeds {
    public String Cultivated;
    public String Development;
    public String EPPO_code;
    public String Family;
    public String Local_name;
    public String Propagation;
    public String imagePath;

    public RiceWeeds() {}  // Needed for Firestore deserialization

    public RiceWeeds(String imagePath, String Cultivated, String Development, String EPPO_code, String Family, String Local_name, String Propagation) {
        this.imagePath = imagePath;
        this.Cultivated = Cultivated;
        this.Development = Development;
        this.EPPO_code = EPPO_code;
        this.Family = Family;
        this.Local_name = Local_name;
        this.Propagation = Propagation;
    }
}
