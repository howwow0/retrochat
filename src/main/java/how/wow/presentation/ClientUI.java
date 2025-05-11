package how.wow.presentation;

import how.wow.domain.model.Message;
import how.wow.domain.ports.MessageHandler;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;

public class ClientUI implements MessageHandler {
    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    public void startChatLoop(Consumer<String> sendMessage) {
        while (true) {
            String message = scanner.nextLine();
            if ("/exit".equalsIgnoreCase(message)) {
                break;
            }
            if (sendMessage != null) {
                sendMessage.accept(message);
            }
        }
    }

    @Override
    public void handle(Message message) {
        System.out.println(message.text());
    }
}