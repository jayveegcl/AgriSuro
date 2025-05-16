package app.thesis.agrisuro.firebase;

public class RiceGrowth {
    public String A;
    public String Tagalog;
    public String imagePath;

    public RiceGrowth() {}  // Needed for Firestore deserialization

    public RiceGrowth(String imagePath, String A, String Tagalog) {
        this.imagePath = imagePath;
        this.A = A;
        this.Tagalog = Tagalog;
    }
}