package notification.service;

import notification.model.Notification;
import notification.subscriber.NotificationSubscriber;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<NotificationSubscriber> subscribers;

    public NotificationService() {
        subscribers = new ArrayList<>();
    }

    public void addSubscriber(NotificationSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(NotificationSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notify(Notification notification) {
        subscribers.forEach(subscriber -> subscriber.sendNotification(notification));
    }
}
