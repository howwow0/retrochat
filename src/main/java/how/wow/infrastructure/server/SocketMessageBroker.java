package how.wow.infrastructure.server;

import how.wow.domain.enums.EmoteType;
import how.wow.domain.model.ChatRoom;
import how.wow.domain.model.Message;
import how.wow.domain.model.User;
import how.wow.domain.ports.MessageBroker;
import how.wow.domain.ports.MessageHandler;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class SocketMessageBroker implements MessageBroker {
    private final Map<User, MessageHandler> handlers = new ConcurrentHashMap<>();
    private final ChatRoom chatRoom;

    @Override
    public void broadcast(Message message) {
        chatRoom.addMessage(message);
        handlers.values().forEach(handler -> handler.handle(message));
    }

    @Override
    public void listUsers(User user) {
        MessageHandler handler = handlers.get(user);
        if (handler != null) {
            StringBuilder userList = new StringBuilder("Пользователи в чате: ");
            chatRoom.getUsers().forEach(u -> userList.append(u.getNickname()).append(", "));
            userList.setLength(userList.length() - 2);
            handler.handle(new Message(User.SYSTEM, userList.toString(), Instant.now()));
        }
    }

    @Override
    public void sendEmotes(User user, EmoteType emoteType) {
        broadcast(new Message(user, emoteType.getText(), Instant.now()));
    }

    @Override
    public void registerUser(User user, MessageHandler handler) {
        handlers.put(user, handler);
        chatRoom.addUser(user);
        broadcast(new Message(User.SYSTEM,
                user.getNickname() + " присоединился к чату",
                Instant.now()));
    }

    @Override
    public void unregisterUser(User user) {
        MessageHandler handler = handlers.remove(user);
        chatRoom.removeUser(user);
        if (handler != null) {
            broadcast(new Message(User.SYSTEM,
                    user.getNickname() + " покинул чат",
                    Instant.now()));
        }
    }

    @Override
    public void listEmotes(User user) {
        MessageHandler handler = handlers.get(user);
        if (handler != null) {
            handler.handle(new Message(User.SYSTEM, EmoteType.getEmotes(), Instant.now()));
        }
    }
}