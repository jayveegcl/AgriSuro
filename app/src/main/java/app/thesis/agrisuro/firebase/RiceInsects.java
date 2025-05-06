package app.thesis.agrisuro.firebase;

public class RiceInsects {
    public String Damage;
    public String Identifying_Marks;
    public String Location;
    public String Tagalog_name;
    public String imagePath;

    public RiceInsects() {}  // Needed for Firestore deserialization

    public RiceInsects(String imagePath, String Damage, String Identifying_Marks, String Location, String Tagalog_name) {
        this.imagePath = imagePath;
        this.Damage = Damage;
        this.Identifying_Marks = Identifying_Marks;
        this.Location = Location;
        this.Tagalog_name = Tagalog_name;
    }
}