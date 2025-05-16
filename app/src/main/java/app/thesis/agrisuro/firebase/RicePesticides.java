package app.thesis.agrisuro.firebase;

public class RicePesticides {
    public String Type;
    public String Ingredient;
    public String Tagalog;
    public String Use;
    public String Application;
    public String Precautions;
    public String imagePath;

    public RicePesticides() {}  // Needed for Firestore deserialization

    public RicePesticides(String imagePath, String Type, String Ingredient, String Tagalog, String Use, String Application, String Precautions) {
        this.imagePath = imagePath;
        this.Type = Type;
        this.Ingredient = Ingredient;
        this.Tagalog = Tagalog;
        this.Use = Use;
        this.Application = Application;
        this.Precautions = Precautions;
    }
}
