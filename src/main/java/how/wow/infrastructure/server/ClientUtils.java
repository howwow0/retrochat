package how.wow.infrastructure.server;

import how.wow.domain.model.Message;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ClientUtils {
    public static String formatMessage(Message message) {
        return String.format("[%s] %s: %s",
                DateTimeFormatter.ofPattern("HH:mm:ss")
                        .format(message.timestamp().atZone(ZoneId.systemDefault())),
                message.author().getNickname(),
                message.text());
    }
}
