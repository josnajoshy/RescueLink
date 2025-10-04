package rescuelink;

import java.time.LocalDateTime;

public class Alert {
    private int alertId;
    private User recipient; // Can be Volunteer or Victim
    private String message;
    private LocalDateTime sentAt;
    private boolean isRead;

    // Constructor for existing alert from DB
    public Alert(int alertId, User recipient, String message, LocalDateTime sentAt, boolean isRead) {
        this.alertId = alertId;
        this.recipient = recipient;
        this.message = message;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    // Constructor for new alert to be sent
    public Alert(User recipient, String message) {
        this(0, recipient, message, LocalDateTime.now(), false);
    }

    // Getters and setters
    public int getAlertId() {
        return alertId;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }
}