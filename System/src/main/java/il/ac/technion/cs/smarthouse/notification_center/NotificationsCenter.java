package il.ac.technion.cs.smarthouse.notification_center;

import javafx.scene.image.Image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.hansolo.enzo.notification.*;
import il.ac.technion.cs.smarthouse.system.services.alerts_service.EmergencyLevel;
import il.ac.technion.cs.smarthouse.utils.JavaFxHelper;

public enum NotificationsCenter {
    ;
    private static Logger log = LoggerFactory.getLogger(NotificationsCenter.class);

    private static boolean isEnabled;

    public static void sendSensorConnectedNotification(final String sensorCommercialName, final String sensorId,
                    final String sensorAlias) {
        sendNotification("A new sensor has connected!", "Commercial name: " + sensorCommercialName + "\nMac id: "
                        + sensorId + "\nAlias: " + sensorAlias, Notification.INFO_ICON);
    }

    public static void sendAlertNotifications(final String senderName, final String message,
                    final EmergencyLevel eLevel) {
        sendNotification("Alert! " + senderName, message + "\n" + eLevel.toPretty(), Notification.WARNING_ICON);
    }

    public static void sendNewAppInstalled(final String applicationName) {
        sendNotification(applicationName, "Installed successfully", Notification.SUCCESS_ICON);
    }

    public static void sendAppFailedToInstall(final String error) {
        sendNotification("Failed installation", error, Notification.ERROR_ICON);
    }

    public static void sendNotification(final String title, final String msg, final Image icon) {
        if (!isEnabled)
            return;

        log.info("\n\tNotificationsCenter: send notification\n\tTitle: " + title + "\n\tMessage:\n\t\t"
                        + msg.replace("\n", "\n\t\t"));
        if (JavaFxHelper.isJavaFxThreadStarted())
            JavaFxHelper.surroundConsumerWithFx(
                            t -> Notification.Notifier.INSTANCE.notify(new Notification(title, msg, icon)))
                            .accept(null);
    }

    public static void enable() {
        isEnabled = true;
    }

    public static void close() {
        try {
            Notification.Notifier.INSTANCE.stop();
        } catch (Exception e) {
            log.warn("Couldn't close Notification center");
        }
    }
}
