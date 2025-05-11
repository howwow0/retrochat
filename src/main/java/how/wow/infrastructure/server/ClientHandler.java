package how.wow.infrastructure.server;

import how.wow.config.CoreConfig;
import how.wow.domain.enums.EmoteType;
import how.wow.domain.model.Message;
import how.wow.domain.model.User;
import how.wow.domain.ports.MessageBroker;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.Socket;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final MessageBroker broker;
    private User user;

    @Override
    public void run() {
        try (var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(CoreConfig.getMotdText());

            out.println("Введите ваш никнейм: ");
            String nickname = in.readLine();
            this.user = new User(UUID.randomUUID().toString(), nickname, Instant.now());

            broker.registerUser(user, message -> out.println(ClientUtils.formatMessage(message)));

            try {
                String input;
                while ((input = in.readLine()) != null && !input.isEmpty()) {
                    if ("/exit".equalsIgnoreCase(input)) break;
                    if ("/listU".equalsIgnoreCase(input)) {
                        broker.listUsers(user);
                        continue;
                    }
                    if ("/emotes".equalsIgnoreCase(input)) {
                        broker.listEmotes(user);
                        continue;
                    }
                    if (input.startsWith("/emote")) {
                        String emote = input.substring(7).trim();
                        if (emote.isEmpty()) {
                            out.println("Используйте: /emote <эмодзи>");
                            continue;
                        }
                        if (EmoteType.containsEmote(emote)) {
                            broker.sendEmotes(user, EmoteType.fromText(emote));
                        } else {
                            out.println("Неизвестный эмодзи: " + emote);
                        }
                        continue;
                    }
                    if ("/help".equalsIgnoreCase(input)) {
                        out.println("Доступные команды:");
                        out.println("/help - показать это сообщение");
                        out.println("/listU - список пользователей");
                        out.println("/emotes - список доступных эмодзи");
                        out.println("/emote <эмодзи> - отправить эмодзи");
                        out.println("/exit - выйти из чата");
                        continue;
                    }
                    if (input.startsWith("/")) {
                        out.println("Неизвестная команда: " + input);
                        continue;
                    }
                    broker.broadcast(new Message(user, input, Instant.now()));
                    System.out.println("[" + user.getNickname() + "] " + input);

                    //Участок кода выше желательно переработать использовав паттерн "Команда",
                    // для добавления нового функционала...
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения сообщения: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Ошибка в клиентском соединении: " + e.getMessage());
        } finally {
            if (user != null) {
                broker.unregisterUser(user);
            }
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии сокета: " + e.getMessage());
            }
        }
    }
}