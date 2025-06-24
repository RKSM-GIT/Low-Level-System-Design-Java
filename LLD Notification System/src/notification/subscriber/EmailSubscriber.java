package notification.subscriber;

import notification.logger.NotificationLogger;
import notification.model.Notification;

public class EmailSubscriber implements NotificationSubscriber {
    private final String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void sendNotification(Notification notification) {
        String messageBody = generateBody(notification.getContent());
        System.out.println(messageBody);
        NotificationLogger.getInstance().log(messageBody);
    }

    private String generateBody(String content) {
        return "[Email Notification " + email + "] " + content;
    }
}
