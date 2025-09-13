package rescuelink;

public class Badge {
    private int badgeId;
    private String name;
    private String description;

    public Badge(int badgeId, String name, String description) {
        this.badgeId = badgeId;
        this.name = name;
        this.description = description;
    }

    // Getters
    public int getBadgeId() {
        return badgeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setters (optional, if badges are editable)
    public void setBadgeId(int badgeId) {
        this.badgeId = badgeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
