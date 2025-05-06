package app.thesis.agrisuro.firebase;

public class RicePesticides {
    public String Brand_name;
    public String Ingredient;
    public String Tagalog;
    public String Use;
    public String imagePath;

    public RicePesticides() {}  // Needed for Firestore deserialization

    public RicePesticides(String imagePath, String Brand_name, String Ingredient, String Tagalog, String Use) {
        this.imagePath = imagePath;
        this.Brand_name = Brand_name;
        this.Ingredient = Ingredient;
        this.Tagalog = Tagalog;
        this.Use = Use;
    }
}
