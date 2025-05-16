package app.thesis.agrisuro.firebase;

public class RiceDiseases {
    public String Disease_Management;
    public String Factors;
    public String Local_name;
    public String Symptoms;
    public String Reco;
    public String imagePath;

    public RiceDiseases() {}  // Needed for Firestore deserialization

    public RiceDiseases(String imagePath, String Disease_Management, String Environment, String Local_name, String Symptoms, String Reco) {
        this.imagePath = imagePath;
        this.Disease_Management = Disease_Management;
        this.Factors = Environment;
        this.Local_name = Local_name;
        this.Symptoms = Symptoms;
        this.Reco = Reco;
    }
}