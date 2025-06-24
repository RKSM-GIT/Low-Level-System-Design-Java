package notification.subscriber;

import notification.model.Notification;

public interface NotificationSubscriber {
    void sendNotification(Notification notification);
}
