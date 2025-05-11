package how.wow.domain.ports;

import how.wow.domain.enums.EmoteType;
import how.wow.domain.model.Message;
import how.wow.domain.model.User;

public interface MessageBroker {
    void broadcast(Message message);
    void registerUser(User user, MessageHandler handler);
    void unregisterUser(User user);
    //Нарушение I в SOLID, желательно выделить в отдельный сервис
    void sendEmotes(User user, EmoteType emoteType);
    void listUsers(User user);
    void listEmotes(User user);
}