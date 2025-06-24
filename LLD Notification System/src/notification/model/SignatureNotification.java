package notification.model;

public class SignatureNotification extends NotificationDecorator {
    private final String signature;

    public SignatureNotification(Notification notification, String signature) {
        super(notification);
        this.signature = signature;
    }

    @Override
    public String getContent() {
        return notification.getContent() + "\n" + signature;
    }
}
