package app.thesis.agrisuro.models;

public class NewsItem {
    private String title;
    private String description;
    private String date;
    private int imageResId;

    public NewsItem(String title, String description, String date, int imageResId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public int getImageResId() {
        return imageResId;
    }
}
