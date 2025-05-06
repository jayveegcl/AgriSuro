package app.thesis.agrisuro.firebase;

public class RiceFertilizers {
    public String Common_name;
    public String Fertilizer;
    public String Per_hectare;
    public String Purpose;
    public String imagePath;

    public RiceFertilizers() {}  // Needed for Firestore deserialization

    public RiceFertilizers(String imagePath, String Common_name, String Fertilizer, String Per_hectare, String Purpose) {
        this.imagePath = imagePath;
        this.Common_name = Common_name;
        this.Fertilizer = Fertilizer;
        this.Per_hectare = Per_hectare;
        this.Purpose = Purpose;
    }
}
