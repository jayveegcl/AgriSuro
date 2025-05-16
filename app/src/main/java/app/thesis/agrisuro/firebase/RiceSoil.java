package app.thesis.agrisuro.firebase;

public class RiceSoil {
    public String A;
    public String B;
    public String C;
    public String Tagalog;
    public String imagePath;

    public RiceSoil() {}  // Needed for Firestore deserialization

    public RiceSoil(String imagePath, String A, String B, String C, String Tagalog) {
        this.imagePath = imagePath;
        this.A = A;
        this.B = B;
        this.C = C;
        this.Tagalog = Tagalog;
    }
}