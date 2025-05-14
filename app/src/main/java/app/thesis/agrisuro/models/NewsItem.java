package app.thesis.agrisuro.models;

public class NewsItem {
    private String title;
    private String description;
    private String date;
    private int imageResId;
    private String url; // Added URL field

    public NewsItem(String title, String description, String date, int imageResId, String url) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageResId = imageResId;
        this.url = url;
    }

    // Constructor without URL for backward compatibility
    public NewsItem(String title, String description, String date, int imageResId) {
        this(title, description, date, imageResId, "");
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

    public String getUrl() {
        return url;
    }

    public boolean hasUrl() {
        return url != null && !url.isEmpty();
    }
}