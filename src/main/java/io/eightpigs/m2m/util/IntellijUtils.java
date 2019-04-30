package io.eightpigs.m2m.util;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * Utils for Intellij.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-04-30
 */
public class IntellijUtils {

    private static Notification notification = new Notification("io.eightpigs.m2m", AllIcons.General.Balloon, NotificationType.INFORMATION);

    public static void errorMsg(String str) {
        notification.setIcon(AllIcons.General.Error);
        notification.setContent(str);
        Notifications.Bus.notify(notification);
    }

    public static void message(String str) {
        notification.setIcon(AllIcons.General.Information);
        notification.setContent(str);
        Notifications.Bus.notify(notification);
    }
}
