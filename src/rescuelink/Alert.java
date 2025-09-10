
package rescuelink;



import java.time.LocalDateTime;

public class Alert {
    private int id;
    private User recipient; // Volunteer or Victim
    private String message; // Full details in message
    private LocalDateTime createdAt;
    private boolean isRead;

    // Constructor with ID
    public Alert(int id, User recipient, String message, LocalDateTime createdAt, boolean isRead) {
        this.id = id;
        this.recipient = recipient;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    // Constructor for new alert
    public Alert(User recipient, String message) {
        this(0, recipient, message, LocalDateTime.now(), false);
    }

    // Getters and setters
    public int getId() { return id; }
    public User getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
