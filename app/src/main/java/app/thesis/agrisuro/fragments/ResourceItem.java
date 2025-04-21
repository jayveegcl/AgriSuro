package app.thesis.agrisuro.fragments;

public class ResourceItem {
    private String id;
    private String title;
    private String description;
    private String image;
    private String category;
    private String[] tags;

    public ResourceItem(String id, String title, String description, String image, String category, String[] tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.category = category;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String[] getTags() {
        return tags;
    }
}
