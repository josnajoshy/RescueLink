package rescuelink;

public class Donation {
    private int donationId;
    private String donorName;
    private String type;
    private double amount;
    private String status;

    // Constructor for new donation
    public Donation(String donorName, String type, double amount) {
        this.donorName = donorName;
        this.type = type;
        this.amount = amount;
        this.status = "Pending";
    }

    // Constructor for loading from DB
    public Donation(int donationId, String donorName, String type, double amount, String status) {
        this.donationId = donationId;
        this.donorName = donorName;
        this.type = type;
        this.amount = amount;
        this.status = status;
    }

    // Getters and setters
    public int getDonationId() { return donationId; }
    public String getDonorName() { return donorName; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return donorName + " - " + type + " (" + amount + ")";
    }
}