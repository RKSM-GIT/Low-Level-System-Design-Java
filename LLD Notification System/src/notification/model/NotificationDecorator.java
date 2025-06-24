package notification.model;

public abstract class NotificationDecorator implements Notification {
    protected Notification notification;

    protected NotificationDecorator(Notification notification) {
        this.notification = notification;
    }

    public abstract String getContent();
}
