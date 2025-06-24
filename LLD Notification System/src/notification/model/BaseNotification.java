package notification.model;

public class BaseNotification implements Notification {
    private final String content;

    public BaseNotification(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
