package app.thesis.agrisuro.firebase;

public class RiceGrowth {
    public String A;
    public String Tagalog_name;
    public String imagePath;

    public RiceGrowth() {}  // Needed for Firestore deserialization

    public RiceGrowth(String imagePath, String A, String Tagalog_name) {
        this.imagePath = imagePath;
        this.A = A;
        this.Tagalog_name = Tagalog_name;
    }
}