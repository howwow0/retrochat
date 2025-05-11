package how.wow.domain.ports;

import how.wow.domain.model.Message;

@FunctionalInterface
public interface MessageHandler {
    void handle(Message message);
}