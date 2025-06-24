package notification.subscriber;

import notification.logger.NotificationLogger;
import notification.model.Notification;

public class SmsSubscriber implements NotificationSubscriber {

    private final String phoneNumber;

    public SmsSubscriber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void sendNotification(Notification notification) {
        String messageBody = generateBody(notification.getContent());
        System.out.println(messageBody);
        NotificationLogger.getInstance().log(messageBody);
    }

    private String generateBody(String content) {
        return "[SMS Notification " + phoneNumber + "] " + content;
    }
}
