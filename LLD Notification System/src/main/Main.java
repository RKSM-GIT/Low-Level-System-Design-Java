package main;

import notification.logger.NotificationLogger;
import notification.model.BaseNotification;
import notification.model.Notification;
import notification.model.SignatureNotification;
import notification.model.TimestampNotification;
import notification.service.NotificationService;
import notification.subscriber.EmailSubscriber;
import notification.subscriber.SmsSubscriber;

public class Main {
    public static void main(String[] args) {
        NotificationService notificationService = new NotificationService();

        notificationService.addSubscriber(new EmailSubscriber("mehul@gmail.com"));
        notificationService.addSubscriber(new EmailSubscriber("xyza@gmail.com"));
        notificationService.addSubscriber(new SmsSubscriber("9784574563"));
        notificationService.addSubscriber(new SmsSubscriber("9784553263"));

        Notification notification = new BaseNotification("Your account has been hacked.");
        notification = new TimestampNotification(notification);
        notification = new SignatureNotification(notification, "Thanks,\nAccount Team\n\n");

        notificationService.notify(notification);

        System.out.println("All Logs: " + NotificationLogger.getInstance().getAllNotifications());
    }
}
