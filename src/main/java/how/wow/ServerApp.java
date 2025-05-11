package how.wow;

import how.wow.config.CoreConfig;
import how.wow.config.NetworkProperties;
import how.wow.domain.model.ChatRoom;
import how.wow.domain.ports.MessageBroker;
import how.wow.infrastructure.server.ChatService;
import how.wow.infrastructure.server.SocketMessageBroker;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        try {
            int port = NetworkProperties.getPort();
            int maxUsers = NetworkProperties.getMaxUsers();
            String chatName = CoreConfig.getName();

            ChatRoom chatRoom = new ChatRoom(chatName);
            MessageBroker broker = new SocketMessageBroker(chatRoom);
            ChatService chatService = new ChatService(broker, port, maxUsers);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Получен сигнал завершения...");
                chatService.stop();
            }));

            chatService.start();

        } catch (IOException e) {
            System.err.println("Не удалось запустить сервер: " + e.getMessage());
            e.printStackTrace();
        }
    }
}