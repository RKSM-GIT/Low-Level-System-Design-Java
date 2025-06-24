package notification.model;

import java.util.Date;

public class TimestampNotification extends NotificationDecorator {
    public TimestampNotification(Notification notification) {
        super(notification);
    }

    @Override
    public String getContent() {
        return "[" + new Date() + "] " + notification.getContent();
    }
}
