package notification.logger;

import notification.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationLogger {
    private final List<String> notificationStore;
    private NotificationLogger notificationLogger;

    private NotificationLogger() {
        notificationStore = new ArrayList<>();
    }

    private static class HolderClass {
        static {
            System.out.println("NotificationLogger Instance Created");
        }
        public static NotificationLogger instance = new NotificationLogger();
    }

    public static NotificationLogger getInstance() {
        return HolderClass.instance;
    }

    public void log(String notificationLog) {
        notificationStore.add(notificationLog);
    }

    public List<String> getAllNotifications() {
        return notificationStore;
    }
}
