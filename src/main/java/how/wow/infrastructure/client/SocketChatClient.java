package how.wow.infrastructure.client;


import how.wow.domain.model.Message;
import how.wow.domain.model.User;
import how.wow.domain.ports.MessageHandler;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class SocketChatClient {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private MessageHandler messageHandler;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = false;

    public void connect(MessageHandler handler) throws IOException {
        this.messageHandler = handler;
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                true);

        running = true;
        executor.submit(this::listenForMessages);
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void listenForMessages() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            String serverMessage;
            while (running && (serverMessage = in.readLine()) != null) {
                messageHandler.handle(new Message(User.CLIENT, serverMessage, Instant.now()));
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Ошибка соединения: " + e.getMessage());
            }
        } finally {
            disconnect();
        }
    }

    public void disconnect() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            executor.shutdownNow();
        } catch (IOException e) {
            System.err.println("Ошибка при отключении: " + e.getMessage());
        }
    }
}