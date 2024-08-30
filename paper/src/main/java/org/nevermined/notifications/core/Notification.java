package org.nevermined.notifications.core;

import me.wyne.wutils.i18n.I18n;
import me.wyne.wutils.i18n.language.replacement.Placeholder;
import me.wyne.wutils.i18n.language.replacement.TextReplacement;
import org.bukkit.entity.Player;
import org.nevermined.notifications.core.data.NotificationData;
import org.nevermined.worldevents.api.core.EventData;
import org.nevermined.worldevents.api.core.QueueData;

public class Notification implements NotificationApi {

    private final NotificationData notificationData;

    public Notification(NotificationData notificationData)
    {
        this.notificationData = notificationData;
    }

    @Override
    public void broadcast(QueueData queue, EventData event)
    {
        if (!notificationData.filter().isValid(queue.key()))
            return;
        if (!notificationData.filter().isValid(event.key()))
            return;

        TextReplacement[] replacements = {
                Placeholder.replace("queue-key", queue.key()),
                Placeholder.legacy("queue-name", queue.name()),
                Placeholder.legacy("queue-description", I18n.reduceComponent(queue.description())),
                Placeholder.replace("queue-capacity", String.valueOf(queue.capacity())),
                Placeholder.replace("event-key", event.key()),
                Placeholder.legacy("event-name", event.name()),
                Placeholder.legacy("event-description", I18n.reduceComponent(event.description())),
                Placeholder.replace("event-chance", String.valueOf(event.chancePercent())),
                Placeholder.replace("event-duration", String.valueOf(event.durationSeconds())),
                Placeholder.replace("event-cooldown", String.valueOf(event.cooldownSeconds()))
        };

        for (Player player : notificationData.filter().getRecieverList())
        {
            if (notificationData.titleData() != null)
                player.showTitle(notificationData.titleData().buildTitle(player, replacements));
            if (!notificationData.chat().isEmpty())
                player.sendMessage(notificationData.buildChat(player, replacements));
            if (notificationData.soundData() != null)
                player.playSound(notificationData.soundData().sound());
        }
    }

    @Override
    public NotificationData getNotificationData() {
        return notificationData;
    }
}
