package how.wow.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class ChatRoom {
    @Getter
    private final String name;
    private final Set<User> users = ConcurrentHashMap.newKeySet();
    private final CopyOnWriteArrayList<Message> messageHistory = new CopyOnWriteArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addMessage(Message message) {
        messageHistory.add(message);
    }

    public Set<User> getUsers() { return Set.copyOf(users); }
}